package com.itsaky.androidide.tooling.impl.internal

import dalvik.system.DexClassLoader
import java.io.File
import java.lang.reflect.Method
import java.util.concurrent.ConcurrentHashMap

/**
 * DexClassLoaderBridge handles the dynamic loading of DEX files for the Native Compose Renderer.
 * It enables Phase B (Dynamic Reflection Bridge) by allowing hot-swapping of UI components
 * without requiring a full application restart.
 */
class DexClassLoaderBridge(
    private var dexPath: String,
    private val optimizedDirectory: String,
    private val parentClassLoader: ClassLoader = DexClassLoaderBridge::class.java.classLoader
) {

    private var classLoader: DexClassLoader = createClassLoader()
    private val classCache = ConcurrentHashMap<String, Class<*>>()

    /**
     * Creates a new DexClassLoader instance for the current dexPath.
     */
    private fun createClassLoader(): DexClassLoader {
        return DexClassLoader(dexPath, optimizedDirectory, null, parentClassLoader)
    }

    /**
     * Hot-swaps the current DEX file with a new one.
     * This clears the class cache and re-initializes the ClassLoader.
     * 
     * @param newDexPath The path to the new DEX file.
     */
    @Synchronized
    fun hotSwap(newDexPath: String) {
        if (this.dexPath == newDexPath && File(newDexPath).lastModified() <= File(dexPath).lastModified()) {
            return // No changes detected
        }
        
        this.dexPath = newDexPath
        this.classCache.clear()
        this.classLoader = createClassLoader()
    }

    /**
     * Loads a class by name, using a cache for performance.
     */
    fun loadClass(className: String): Class<*> {
        return classCache.getOrPut(className) {
            classLoader.loadClass(className)
        }
    }

    /**
     * Finds all methods annotated with @Preview in the given class.
     * Uses reflection to check annotation names to avoid direct dependency on Compose libraries.
     */
    fun findPreviewMethods(clazz: Class<*>): List<Method> {
        return clazz.declaredMethods.filter { method ->
            method.annotations.any { annotation ->
                val name = annotation.annotationClass.qualifiedName ?: annotation.annotationClass.simpleName
                name?.contains("Preview") == true
            }
        }
    }

    /**
     * Invokes a preview method dynamically.
     * 
     * @param method The method to invoke.
     * @param instance The instance to invoke on (null for static methods/Composables).
     * @param args Arguments for the method.
     * @return The result of the invocation.
     */
    fun invokePreviewMethod(method: Method, instance: Any? = null, vararg args: Any?): Any? {
        method.isAccessible = true
        return method.invoke(instance, *args)
    }

    /**
     * Checks if the current DEX file exists and is readable.
     */
    fun isValid(): Boolean {
        val file = File(dexPath)
        return file.exists() && file.canRead()
    }

    companion object {
        private const val TAG = "DexClassLoaderBridge"
    }
}
