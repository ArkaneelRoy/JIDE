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

package com.willow.androidide.ultra.lsp.xml.providers.completion.manifest

import com.android.aaptcompiler.ConfigDescription
import com.android.aaptcompiler.ResourcePathData
import com.android.aaptcompiler.Styleable
import com.willow.androidide.ultra.lsp.api.ICompletionProvider
import com.willow.androidide.ultra.lsp.xml.providers.completion.AttrCompletionProvider
import com.willow.androidide.ultra.lsp.xml.providers.completion.MANIFEST_TAG_PREFIX
import com.willow.androidide.ultra.lsp.xml.providers.completion.canCompleteManifest
import com.willow.androidide.ultra.lsp.xml.providers.completion.manifestResourceTable
import com.willow.androidide.ultra.lsp.xml.providers.completion.transformToEntryName
import com.willow.androidide.ultra.lsp.xml.utils.XmlUtils.NodeType
import com.willow.androidide.ultra.xml.res.IResourceGroup
import org.eclipse.lemminx.dom.DOMNode

/**
 * Provides attribution completion for AndroidManifest.
 *
 * @author Akash Yadav
 */
class ManifestAttrCompletionProvider(provider: ICompletionProvider) :
  AttrCompletionProvider(provider) {

  override fun canProvideCompletions(pathData: ResourcePathData, type: NodeType): Boolean {
    return super.canProvideCompletions(pathData, type) && canCompleteManifest(pathData, type)
  }

  override fun findResourceTables(nsUri: String?) = manifestResourceTable()

  override fun findNodeStyleables(node: DOMNode, styleables: IResourceGroup): Set<Styleable> {
    val name = node.nodeName
    val styleable =
      styleables.findEntry(transformToEntryName(name, MANIFEST_TAG_PREFIX))
        ?.findValue(ConfigDescription())?.value
    if (styleable != null && styleable is Styleable) {
      return setOf(styleable)
    }

    return emptySet()
  }
}
