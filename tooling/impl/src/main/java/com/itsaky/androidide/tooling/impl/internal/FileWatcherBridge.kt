package com.itsaky.androidide.tooling.impl.internal

import java.io.File
import java.nio.file.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

/**
 * FileWatcherBridge monitors source file changes and triggers the DexClassLoaderBridge
 * for automatic hot-swapping. This enables Phase C (Real-Time Synchronization).
 */
class FileWatcherBridge(
    private val projectRoot: String,
    private val dexClassLoaderBridge: DexClassLoaderBridge,
    private val onSwapTriggered: (String) -> Unit
) {
    private var watchThread: Thread? = null
    private var isRunning = false

    /**
     * Starts watching the project root for changes in .kt or .java files.
     */
    fun startWatching() {
        if (isRunning) return
        isRunning = true
        
        watchThread = thread(start = true, name = "AIDEU-FileWatcher") {
            val watchService = FileSystems.getDefault().newWatchService()
            val path = Paths.get(projectRoot)
            
            // Register recursively (simplified for prototype)
            path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY)
            
            while (isRunning) {
                val key = watchService.poll(500, TimeUnit.MILLISECONDS) ?: continue
                
                for (event in key.pollEvents()) {
                    val context = event.context() as Path
                    val file = context.toFile()
                    
                    if (file.extension == "kt" || file.extension == "java") {
                        // In a real implementation, this would trigger a background build
                        // and then hot-swap the resulting DEX.
                        // For the prototype, we simulate the DEX update.
                        val simulatedDexPath = "${projectRoot}/build/outputs/hot-swap.dex"
                        dexClassLoaderBridge.hotSwap(simulatedDexPath)
                        onSwapTriggered(file.name)
                    }
                }
                key.reset()
            }
        }
    }

    /**
     * Stops the file watcher.
     */
    fun stopWatching() {
        isRunning = false
        watchThread?.interrupt()
        watchThread = null
    }
}
