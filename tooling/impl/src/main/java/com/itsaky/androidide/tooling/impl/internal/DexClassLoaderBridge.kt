package com.itsaky.androidide.tooling.impl.internal

import dalvik.system.DexClassLoader
import java.io.File
import java.lang.reflect.Method

class DexClassLoaderBridge(private val dexPath: String, private val optimizedDirectory: String) {

    private val classLoader: DexClassLoader

    init {
        classLoader = DexClassLoader(dexPath, optimizedDirectory, null, javaClass.classLoader)
    }

    fun loadClass(className: String): Class<*> {
        return classLoader.loadClass(className)
    }

    fun findPreviewMethods(clazz: Class<*>): List<Method> {
        return clazz.methods.filter { method ->
            method.annotations.any { annotation ->
                annotation.annotationClass.simpleName == "Preview"
            }
        }
    }

    fun invokePreviewMethod(method: Method, instance: Any?): Any? {
        return method.invoke(instance)
    }

    companion object {
        private const val TAG = "DexClassLoaderBridge"
    }
}
