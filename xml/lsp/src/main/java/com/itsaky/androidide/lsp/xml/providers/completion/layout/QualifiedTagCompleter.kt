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

package com.willow.androidide.ultra.lsp.xml.providers.completion.layout

import com.android.aaptcompiler.ResourcePathData
import com.willow.androidide.ultra.lookup.Lookup
import com.willow.androidide.ultra.lsp.api.ICompletionProvider
import com.willow.androidide.ultra.lsp.models.CompletionItem
import com.willow.androidide.ultra.lsp.models.CompletionParams
import com.willow.androidide.ultra.lsp.models.CompletionResult
import com.willow.androidide.ultra.lsp.models.MatchLevel.NO_MATCH
import com.willow.androidide.ultra.lsp.xml.providers.completion.match
import com.willow.androidide.ultra.lsp.xml.utils.XmlUtils.NodeType
import com.willow.androidide.ultra.projects.ModuleProject
import com.willow.androidide.ultra.utils.ClassTrie
import com.willow.androidide.ultra.xml.internal.widgets.DefaultWidgetTable
import com.willow.androidide.ultra.xml.widgets.WidgetTable
import org.eclipse.lemminx.dom.DOMDocument

/**
 * Completes tags from
 *
 * @author Akash Yadav
 */
class QualifiedTagCompleter(provider: ICompletionProvider) : LayoutTagCompletionProvider(provider) {

  override fun doComplete(
    params: CompletionParams,
    pathData: ResourcePathData,
    document: DOMDocument,
    type: NodeType,
    prefix: String
  ): CompletionResult {
    val result = mutableListOf<CompletionItem>()
    val (widgets, module) = doLookup()
    var fqn = prefix
    if (prefix.endsWith('.')) {
      fqn = fqn.substringBeforeLast('.')
    }

    widgets.getNode(name = fqn, createIfNotPresent = false)?.children?.values?.forEach {
      val qualifiedName = "$fqn.${it.name}"
      val match = match(it.name, qualifiedName, prefix)
      result.add(createTagCompletionItem(it.name, qualifiedName, match))
    }

    addFromTrie(module.compileClasspathClasses, fqn, prefix, result)
    addFromTrie(module.compileJavaSourceClasses, fqn, prefix, result)

    return CompletionResult(result)
  }

  private fun addFromTrie(
    trie: ClassTrie,
    fqn: String,
    prefix: String,
    result: MutableList<CompletionItem>
  ) {
    val node = trie.findNode(fqn) ?: trie.findNode(fqn.substringBeforeLast('.')) ?: return
    node.children.values.forEach {
      val match = match(it.name, it.qualifiedName, prefix)
      if (match == NO_MATCH) {
        return@forEach
      }
      
      result.add(createTagCompletionItem(it.name, it.qualifiedName, match))
    }
  }

  private fun doLookup(): Pair<DefaultWidgetTable, ModuleProject> {
    val widgets =
      Lookup.getDefault().lookup(WidgetTable.COMPLETION_LOOKUP_KEY)
        ?: throw IllegalStateException("No widget table provided")
    val module =
      Lookup.getDefault().lookup(ModuleProject.COMPLETION_MODULE_KEY)
        ?: throw IllegalStateException("No module project provided")
    return widgets as DefaultWidgetTable to module
  }
}
