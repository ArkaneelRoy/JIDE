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

package com.willow.androidide.ultra.templates.impl

import com.willow.androidide.ultra.templates.BooleanParameter
import com.willow.androidide.ultra.templates.EnumParameter
import com.willow.androidide.ultra.templates.Language
import com.willow.androidide.ultra.templates.ProjectTemplate
import com.willow.androidide.ultra.templates.ProjectVersionData
import com.willow.androidide.ultra.templates.Sdk
import com.willow.androidide.ultra.templates.StringParameter
import com.willow.androidide.ultra.templates.base.AndroidModuleTemplateBuilder
import com.willow.androidide.ultra.templates.base.ProjectTemplateBuilder
import com.willow.androidide.ultra.templates.base.baseProject
import com.willow.androidide.ultra.templates.impl.base.createRecipe
import com.willow.androidide.ultra.templates.minSdkParameter
import com.willow.androidide.ultra.templates.packageNameParameter
import com.willow.androidide.ultra.templates.projectLanguageParameter
import com.willow.androidide.ultra.templates.projectNameParameter
import com.willow.androidide.ultra.templates.useKtsParameter

/**
 * Indents the given string for the given [indentation level][level].
 */
fun String.indentToLevel(level: Int): String {
  val lines = split(Regex("[\r\n]"))
  return StringBuilder().apply {
    for (line in lines) {
      append(line)
      append(" ".repeat(level * 4))
    }
  }.toString()
}

@Suppress("UnusedReceiverParameter")
internal fun AndroidModuleTemplateBuilder.templateAsset(name: String,
                                                        path: String
): String {
  return "templates/${name}/${path}"
}

internal inline fun baseProjectImpl(
  projectName: StringParameter = projectNameParameter(),
  packageName: StringParameter = packageNameParameter(),
  useKts: BooleanParameter = useKtsParameter(),
  minSdk: EnumParameter<Sdk> = minSdkParameter(),
  language: EnumParameter<Language> = projectLanguageParameter(),
  projectVersionData: ProjectVersionData = ProjectVersionData(),
  crossinline block: ProjectTemplateBuilder.() -> Unit
): ProjectTemplate =
  baseProject(projectName = projectName, packageName = packageName,
    useKts = useKts, minSdk = minSdk, language = language,
    projectVersionData = projectVersionData) {
    block()

    // make sure we return a proper result
    if (!isRecipeSet) {
      recipe = createRecipe {}
    }
  }