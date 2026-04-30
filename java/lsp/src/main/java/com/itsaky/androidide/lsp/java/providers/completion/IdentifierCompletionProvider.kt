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

package com.willow.androidide.ultra.lsp.java.providers.completion

import com.willow.androidide.ultra.lsp.api.IServerSettings
import com.willow.androidide.ultra.lsp.java.compiler.CompileTask
import com.willow.androidide.ultra.lsp.java.compiler.JavaCompilerService
import com.willow.androidide.ultra.lsp.models.CompletionItem
import com.willow.androidide.ultra.lsp.models.CompletionResult
import com.willow.androidide.ultra.progress.ProgressManager.Companion.abortIfCancelled
import openjdk.source.util.TreePath
import java.nio.file.Path

/** @author Akash Yadav */
class IdentifierCompletionProvider(
  completingFile: Path,
  cursor: Long,
  compiler: JavaCompilerService,
  settings: IServerSettings
) : IJavaCompletionProvider(cursor, completingFile, compiler, settings) {

  override fun doComplete(
    task: CompileTask,
    path: TreePath,
    partial: String,
    endsWithParen: Boolean,
  ): CompletionResult {
    val list = mutableListOf<CompletionItem>()

    abortIfCancelled()
    abortCompletionIfCancelled()

    val snippets =
      SnippetCompletionProvider(cursor, file, compiler, settings)
        .complete(task, path, partial, endsWithParen)
    list.addAll(snippets.items)

    val scopeMembers =
      ScopeCompletionProvider(file, cursor, compiler, settings)
        .complete(task, path, partial, endsWithParen)
    list.addAll(scopeMembers.items)

    abortIfCancelled()
    abortCompletionIfCancelled()
    val staticImports =
      StaticImportCompletionProvider(file, cursor, compiler, settings, path.compilationUnit)
        .complete(task, path, partial, endsWithParen)
    list.addAll(staticImports.items)

    if (CompletionResult.TRIM_TO_MAX && list.size < CompletionResult.MAX_ITEMS) {
      val allLower: Boolean = settings.shouldMatchAllLowerCase()
      if (allLower || partial.isNotEmpty() && Character.isUpperCase(partial[0])) {
        abortIfCancelled()
        abortCompletionIfCancelled()
        val classNames =
          ClassNamesCompletionProvider(file, cursor, compiler, settings, path.compilationUnit)
            .complete(task, path, partial, endsWithParen)
        list.addAll(classNames.items)
      }
    }

    abortIfCancelled()
    abortCompletionIfCancelled()
    val keywords =
      KeywordCompletionProvider(file, cursor, compiler, settings)
        .complete(task, path, partial, endsWithParen)
    list.addAll(keywords.items)

    return CompletionResult(list)
  }
}
