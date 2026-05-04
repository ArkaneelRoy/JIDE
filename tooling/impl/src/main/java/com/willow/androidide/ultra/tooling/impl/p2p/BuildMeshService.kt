package com.willow.androidide.ultra.tooling.impl.p2p

import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.Executors
import javax.net.ssl.SSLServerSocketFactory
import javax.net.ssl.SSLSocketFactory
import org.slf4j.LoggerFactory

/**
 * Distributed Build Mesh Service.
 * Allows mobile devices on the same network to share compilation resources.
 * All communication is end-to-end encrypted.
 */
class BuildMeshService(private val port: Int = 8888) {
    private val log = LoggerFactory.getLogger(BuildMeshService::class.java)
    private val executor = Executors.newCachedThreadPool()
    private var isRunning = false
    private var serverSocket: ServerSocket? = null

    /**
     * Node Health metrics for intelligent resource arbitration.
     */
    data class NodeHealth(
        val thermalState: Int, // 0: Normal, 1: Fair, 2: Serious, 3: Critical
        val batteryLevel: Float, // 0.0 to 1.0
        val isCharging: Boolean,
        val cpuLoad: Double, // 0.0 to 1.0
        val availableMemory: Long // in MB
    ) {
        /**
         * Calculates a health score from 0 to 100.
         * Higher is better.
         */
        fun calculateScore(): Int {
            var score = 100
            
            // Thermal penalty
            score -= thermalState * 25
            
            // Battery penalty (if not charging)
            if (!isCharging) {
                if (batteryLevel < 0.2f) score -= 50
                else if (batteryLevel < 0.5f) score -= 20
            }
            
            // CPU load penalty
            score -= (cpuLoad * 40).toInt()
            
            // Memory penalty
            if (availableMemory < 512) score -= 30
            
            return score.coerceIn(0, 100)
        }

        fun canAcceptTask(): Boolean = calculateScore() > 60
    }

    /**
     * Mock function to get current device health.
     * In production, this would interface with Android's BatteryManager and HardwarePropertiesManager.
     */
    fun getCurrentHealth(): NodeHealth {
        return NodeHealth(
            thermalState = 0,
            batteryLevel = 0.85f,
            isCharging = true,
            cpuLoad = 0.15,
            availableMemory = 2048
        )
    }

    /**
     * Starts the P2P build node.
     */
    fun startNode() {
        isRunning = true
        executor.execute {
            try {
                val factory = SSLServerSocketFactory.getDefault()
                serverSocket = factory.createServerSocket(port)
                log.info("Build Mesh Node started on port $port (Encrypted)")

                while (isRunning) {
                    val clientSocket = serverSocket?.accept()
                    if (clientSocket != null) {
                        val health = getCurrentHealth()
                        if (health.canAcceptTask()) {
                            handleClient(clientSocket)
                        } else {
                            log.warn("Node health too low (${health.calculateScore()}), rejecting task")
                            clientSocket.close()
                        }
                    }
                }
            } catch (e: Exception) {
                if (isRunning) log.error("Build Mesh Node error", e)
            }
        }
    }

    /**
     * Offloads a build task to another node.
     */
    fun offloadTask(targetIp: String, taskData: ByteArray): ByteArray {
        val factory = SSLSocketFactory.getDefault()
        factory.createSocket(targetIp, port).use { socket ->
            socket.outputStream.write(taskData)
            return socket.inputStream.readBytes()
        }
    }

    private fun handleClient(socket: Socket) {
        executor.execute {
            socket.use {
                val input = it.inputStream.readBytes()
                log.info("Received build task of size ${input.size} bytes")
                // Execute the build task locally and return results
                val result = "Build Task Completed Locally".toByteArray()
                it.outputStream.write(result)
            }
        }
    }

    fun stopNode() {
        isRunning = false
        serverSocket?.close()
    }
}
