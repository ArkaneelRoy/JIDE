package com.willow.androidide.ultra.tooling.impl.internal

import java.io.File
import java.lang.reflect.Method
import java.net.URLClassLoader
import java.util.concurrent.ConcurrentHashMap

/**
 * Loads preview classes dynamically for the native/compose renderer bridge.
 *
 * On Android the bridge uses dalvik.system.DexClassLoader through reflection so
 * this JVM tooling module does not require the Android SDK on its compile classpath.
 * On the JVM it falls back to URLClassLoader for JAR/ZIP inputs, which keeps the
 * tooling API server testable without pretending that a JVM can load DEX files.
 */
class DexClassLoaderBridge(
  private var dexPath: String,
  private val optimizedDirectory: String,
  private val parentClassLoader: ClassLoader = DexClassLoaderBridge::class.java.classLoader,
) {

  private var classLoader: ClassLoader = createClassLoader()
  private val classCache = ConcurrentHashMap<String, Class<*>>()

  private fun createClassLoader(): ClassLoader {
    val dexClassLoader = runCatching {
      Class.forName("dalvik.system.DexClassLoader")
    }.getOrNull()

    if (dexClassLoader != null) {
      val constructor = dexClassLoader.getConstructor(
        String::class.java,
        String::class.java,
        String::class.java,
        ClassLoader::class.java,
      )
      return constructor.newInstance(
        dexPath,
        optimizedDirectory,
        null,
        parentClassLoader,
      ) as ClassLoader
    }

    val file = File(dexPath)
    require(file.isFile) { "Dynamic class archive does not exist: $dexPath" }
    require(!file.extension.equals("dex", ignoreCase = true)) {
      "DEX preview loading requires an Android runtime: $dexPath"
    }
    return URLClassLoader(arrayOf(file.toURI().toURL()), parentClassLoader)
  }

  /** Replaces the current archive when a newer file is supplied. */
  @Synchronized
  fun hotSwap(newDexPath: String) {
    val currentFile = File(dexPath)
    val newFile = File(newDexPath)
    if (dexPath == newDexPath && newFile.lastModified() <= currentFile.lastModified()) {
      return
    }

    dexPath = newDexPath
    classCache.clear()
    classLoader = createClassLoader()
  }

  /** Loads and caches a class by its fully qualified name. */
  fun loadClass(className: String): Class<*> = classCache.getOrPut(className) {
    classLoader.loadClass(className)
  }

  /** Finds methods whose annotation type name contains `Preview`. */
  fun findPreviewMethods(clazz: Class<*>): List<Method> = clazz.declaredMethods.filter { method ->
    method.annotations.any { annotation ->
      val name = annotation.annotationClass.qualifiedName ?: annotation.annotationClass.simpleName
      name?.contains("Preview") == true
    }
  }

  /** Invokes a preview method, supporting both static and instance methods. */
  fun invokePreviewMethod(method: Method, instance: Any? = null, vararg args: Any?): Any? {
    method.isAccessible = true
    return method.invoke(instance, *args)
  }

  /** Returns whether the current archive exists and is readable. */
  fun isValid(): Boolean {
    val file = File(dexPath)
    return file.exists() && file.canRead()
  }
}
