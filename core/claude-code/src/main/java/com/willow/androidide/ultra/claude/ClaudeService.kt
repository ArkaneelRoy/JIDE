/*
 *  This file is part of AndroidIDE Ultra.
 *
 *  AndroidIDE Ultra is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AndroidIDE Ultra is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *   along with AndroidIDE Ultra.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.willow.androidide.ultra.claude

import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.util.concurrent.CompletableFuture

/**
 * Service for interacting with Claude API.
 * This is the foundation for the "Intelligence Layer" in AndroidIDE Ultra.
 */
class ClaudeService(private val apiKey: String) {

    private val client = OkHttpClient()
    private val gson = Gson()
    private val apiEndpoint = "https://api.anthropic.com/v1/messages"

    interface ClaudeCallback {
        fun onSuccess(response: String)
        fun onError(error: Throwable)
    }

    /**
     * Synchronously sends a prompt to Claude.
     *
     * This adapter is intended for callers that already execute off the main thread.
     */
    fun query(prompt: String): String {
        val result = CompletableFuture<String>()
        askClaude(prompt, object : ClaudeCallback {
            override fun onSuccess(response: String) = result.complete(response)
            override fun onError(error: Throwable) = result.completeExceptionally(error)
        })
        return result.get()
    }

    /**
     * Sends a prompt to Claude and returns the response.
     */
    fun askClaude(prompt: String, callback: ClaudeCallback) {
        val json = mapOf(
            "model" to "claude-4.6-sonnet",
            "max_tokens" to 4096,
            "messages" to listOf(
                mapOf("role" to "user", "content" to prompt)
            )
        )

        val body = gson.toJson(json).toRequestBody("application/json".toMediaType())
        
        val request = Request.Builder()
            .url(apiEndpoint)
            .addHeader("x-api-key", apiKey)
            .addHeader("anthropic-version", "2023-06-01")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!it.isSuccessful) {
                        callback.onError(IOException("Unexpected code $it"))
                        return
                    }
                    
                    val responseBody = it.body?.string()
                    if (responseBody != null) {
                        callback.onSuccess(responseBody)
                    } else {
                        callback.onError(IOException("Empty response body"))
                    }
                }
            }
        })
    }
}
