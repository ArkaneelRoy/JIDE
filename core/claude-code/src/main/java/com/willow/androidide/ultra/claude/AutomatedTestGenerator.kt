package com.willow.androidide.ultra.claude

import java.io.File
import java.util.concurrent.CompletableFuture

/**
 * AutomatedTestGenerator uses Claude to generate unit and integration tests
 * for existing codebases, ensuring high code quality with minimal manual effort.
 */
class AutomatedTestGenerator(private val claudeService: ClaudeService) {

    /**
     * Generates a unit test for the specified source file.
     * 
     * @param sourceFile The file to generate tests for.
     * @return A CompletableFuture containing the generated test code.
     */
    fun generateUnitTest(sourceFile: File): CompletableFuture<String> {
        return CompletableFuture.supplyAsync {
            val content = sourceFile.readText()
            val prompt = """
                Generate a comprehensive JUnit 5 unit test for the following Kotlin/Java class.
                Include edge cases and mock dependencies where appropriate.
                
                Source Code:
                $content
            """.trimIndent()
            
            val testCode = claudeService.query(prompt)
            saveTestFile(sourceFile, testCode)
            testCode
        }
    }

    private fun saveTestFile(sourceFile: File, testCode: String) {
        val testDir = File(sourceFile.parent.replace("main", "test"))
        if (!testDir.exists()) testDir.mkdirs()
        
        val testFileName = "${sourceFile.nameWithoutExtension}Test.kt"
        val testFile = File(testDir, testFileName)
        testFile.writeText(testCode)
    }

    /**
     * Generates an integration test for a specific feature or flow.
     */
    fun generateIntegrationTest(featureDescription: String, relatedFiles: List<File>): CompletableFuture<String> {
        return CompletableFuture.supplyAsync {
            val context = relatedFiles.joinToString("\n\n") { "${it.name}:\n${it.readText()}" }
            val prompt = """
                Generate an integration test for the following feature: $featureDescription
                Use the provided file contexts to understand the interaction between components.
                
                Context:
                $context
            """.trimIndent()
            
            claudeService.query(prompt)
        }
    }
}
