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

import com.willow.androidide.ultra.projects.IProject

/**
 * High-level integration for Claude features in the IDE.
 */
class ClaudeIntegration(private val project: IProject, private val service: ClaudeService) {

    /**
     * Suggests a fix for a specific error in a file.
     */
    fun suggestFix(filePath: String, errorMessage: String, callback: ClaudeService.ClaudeCallback) {
        val prompt = """
            I am working on an Android project. 
            In file: $filePath
            I encountered this error: $errorMessage
            
            Please suggest a fix for this issue.
        """.trimIndent()
        
        service.askClaude(prompt, callback)
    }

    /**
     * Refactors code based on natural language instructions.
     */
    fun refactorCode(code: String, instruction: String, callback: ClaudeService.ClaudeCallback) {
        val prompt = """
            Refactor the following code according to these instructions: $instruction
            
            Code:
            $code
        """.trimIndent()
        
        service.askClaude(prompt, callback)
    }

    /**
     * Explains the provided code in simple terms.
     */
    fun explainCode(code: String, callback: ClaudeService.ClaudeCallback) {
        val prompt = """
            Explain the following code in simple terms, highlighting its purpose and key logic:
            
            Code:
            $code
        """.trimIndent()
        
        service.askClaude(prompt, callback)
    }

    /**
     * Generates unit tests for the provided code.
     */
    fun generateTests(code: String, callback: ClaudeService.ClaudeCallback) {
        val prompt = """
            Generate comprehensive unit tests for the following code using JUnit and Mockito where appropriate:
            
            Code:
            $code
        """.trimIndent()
        
        service.askClaude(prompt, callback)
    }
}
