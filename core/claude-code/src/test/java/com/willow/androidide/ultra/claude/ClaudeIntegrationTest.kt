package com.willow.androidide.ultra.claude

import com.willow.androidide.ultra.projects.IProject
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify

class ClaudeIntegrationTest {

    @Mock
    lateinit var mockProject: IProject

    @Mock
    lateinit var mockService: ClaudeService

    @Mock
    lateinit var mockCallback: ClaudeService.ClaudeCallback

    private lateinit var integration: ClaudeIntegration

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        integration = ClaudeIntegration(mockProject, mockService)
    }

    @Test
    fun testSuggestFix() {
        val filePath = "MainActivity.kt"
        val errorMessage = "Unresolved reference: R"
        
        integration.suggestFix(filePath, errorMessage, mockCallback)
        
        verify(mockService).askClaude(any(), eq(mockCallback))
    }

    @Test
    fun testRefactorCode() {
        val code = "fun old() {}"
        val instruction = "Rename to new"
        
        integration.refactorCode(code, instruction, mockCallback)
        
        verify(mockService).askClaude(any(), eq(mockCallback))
    }

    @Test
    fun testExplainCode() {
        val code = "fun main() { println(\"Hello\") }"
        
        integration.explainCode(code, mockCallback)
        
        verify(mockService).askClaude(any(), eq(mockCallback))
    }

    @Test
    fun testGenerateTests() {
        val code = "class Calculator { fun add(a: Int, b: Int) = a + b }"
        
        integration.generateTests(code, mockCallback)
        
        verify(mockService).askClaude(any(), eq(mockCallback))
    }

    @Test
    fun testReviewCode() {
        val code = "fun leaky() { val list = mutableListOf<Int>(); while(true) list.add(1) }"
        
        integration.reviewCode(code, mockCallback)
        
        verify(mockService).askClaude(any(), eq(mockCallback))
    }

    @Test
    fun testGenerateDocumentation() {
        val code = "fun process(data: String): Int = data.length"
        
        integration.generateDocumentation(code, mockCallback)
        
        verify(mockService).askClaude(any(), eq(mockCallback))
    }
}
