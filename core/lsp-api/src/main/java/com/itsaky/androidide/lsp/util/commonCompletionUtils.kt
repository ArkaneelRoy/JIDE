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

package com.willow.androidide.ultra.lsp.util

import com.willow.androidide.ultra.lookup.Lookup
import com.willow.androidide.ultra.projects.IProjectManager
import com.willow.androidide.ultra.projects.android.AndroidModule
import com.willow.androidide.ultra.projects.ModuleProject
import com.willow.androidide.ultra.xml.resources.ResourceTableRegistry
import com.willow.androidide.ultra.xml.versions.ApiVersions
import com.willow.androidide.ultra.xml.widgets.WidgetTable
import java.io.File
import java.nio.file.Path

fun setupLookupForCompletion(file: File) {
  setupLookupForCompletion(file.toPath())
}

fun setupLookupForCompletion(file: Path) {
  val module =
    IProjectManager.getInstance().getWorkspace()?.findModuleForFile(file, false) ?: return
  val lookup = Lookup.getDefault()

  lookup.update(ModuleProject.COMPLETION_MODULE_KEY, module)

  if (module is AndroidModule) {
    val versions = module.getApiVersions()
    if (versions != null) {
      lookup.update(ApiVersions.COMPLETION_LOOKUP_KEY, versions)
    }

    val widgets = module.getWidgetTable()
    if (widgets != null) {
      lookup.update(WidgetTable.COMPLETION_LOOKUP_KEY, widgets)
    }

    val frameworkResources = module.getFrameworkResourceTable()
    if (frameworkResources != null) {
      lookup.update(ResourceTableRegistry.COMPLETION_FRAMEWORK_RES, frameworkResources)
    }

    val moduleResources = module.getSourceResourceTables()
    if (moduleResources.isNotEmpty()) {
      lookup.update(ResourceTableRegistry.COMPLETION_MODULE_RES, moduleResources)
    }

    val depResTables = module.getDependencyResourceTables()
    if (depResTables.isNotEmpty()) {
      lookup.update(ResourceTableRegistry.COMPLETION_DEP_RES, depResTables)
    }

    val manifestAttrTable = module.getManifestAttrTable()
    if (manifestAttrTable != null) {
      lookup.update(ResourceTableRegistry.COMPLETION_MANIFEST_ATTR_RES, manifestAttrTable)
    }
  }
}