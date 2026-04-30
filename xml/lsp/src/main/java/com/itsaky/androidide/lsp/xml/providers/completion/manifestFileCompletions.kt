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

package com.willow.androidide.ultra.lsp.xml.providers.completion

import com.android.SdkConstants.ANDROID_MANIFEST_XML
import com.android.aaptcompiler.ResourcePathData
import com.willow.androidide.ultra.lookup.Lookup
import com.willow.androidide.ultra.lsp.xml.utils.XmlUtils.NodeType
import com.willow.androidide.ultra.utils.VMUtils
import com.willow.androidide.ultra.xml.res.IResourceTable
import com.willow.androidide.ultra.xml.resources.ResourceTableRegistry

const val MANIFEST_TAG_PREFIX = "AndroidManifest"

fun canCompleteManifest(pathData: ResourcePathData, type: NodeType): Boolean {
  return pathData.file.name == ANDROID_MANIFEST_XML ||
    (VMUtils.isJvm() &&
      pathData.file.name.startsWith("Manifest") &&
      pathData.file.name.endsWith("_template.xml"))
}

fun manifestResourceTable(): Set<IResourceTable> {
  return setOf(
    Lookup.getDefault().lookup(ResourceTableRegistry.COMPLETION_MANIFEST_ATTR_RES)
      ?: return emptySet()
  )
}