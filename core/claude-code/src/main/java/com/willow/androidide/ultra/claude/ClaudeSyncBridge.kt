package com.willow.androidide.ultra.claude

import java.util.concurrent.ConcurrentHashMap
import org.slf4j.LoggerFactory

/**
 * Real-time synchronization bridge for the Intelligence Layer.
 * Allows multiple developers to interact with the same Claude Code instance across different devices.
 */
class ClaudeSyncBridge(private val claudeService: ClaudeService) {
    private val log = LoggerFactory.getLogger(ClaudeSyncBridge::class.java)
    private val activeSessions = ConcurrentHashMap<String, CollaborativeSession>()

    data class CollaborativeSession(
        val sessionId: String,
        val participants: MutableList<String>,
        val history: MutableList<Pair<String, String>> // User, Message
    )

    /**
     * Creates or joins a collaborative AI session.
     */
    fun joinSession(sessionId: String, userId: String): CollaborativeSession {
        return activeSessions.computeIfAbsent(sessionId) {
            CollaborativeSession(it, mutableListOf(), mutableListOf())
        }.apply {
            if (!participants.contains(userId)) {
                participants.add(userId)
                log.info("User $userId joined collaborative session $sessionId")
            }
        }
    }

    /**
     * Broadcasts a message to Claude and syncs the response across all participants.
     */
    fun broadcastQuery(sessionId: String, userId: String, query: String, callback: ClaudeService.ClaudeCallback) {
        val session = activeSessions[sessionId] ?: throw IllegalStateException("Session not found")
        
        session.history.add(userId to query)
        log.info("Broadcasting query from $userId in session $sessionId: $query")

        claudeService.askClaude(query, object : ClaudeService.ClaudeCallback {
            override fun onSuccess(response: String) {
                session.history.add("Claude" to response)
                callback.onSuccess(response)
                // In a real implementation, this would trigger a WebSocket push to all participants
            }

            override fun onError(error: Throwable) {
                callback.onError(error)
            }
        })
    }
}
