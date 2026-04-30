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

package com.willow.androidide.ultra.tooling.impl.sync

import com.willow.androidide.ultra.builder.model.IJavaCompilerSettings
import com.willow.androidide.ultra.tooling.api.IJavaProject
import com.willow.androidide.ultra.tooling.api.messages.InitializeProjectParams
import com.willow.androidide.ultra.tooling.api.models.JavaModuleCompilerSettings
import com.willow.androidide.ultra.tooling.impl.internal.JavaProjectImpl
import org.gradle.tooling.model.idea.IdeaModule
import org.gradle.tooling.model.idea.IdeaProject

/**
 * Builds model for Java library projects.
 *
 * @author Akash Yadav
 */
class JavaProjectModelBuilder(initializationParams: InitializeProjectParams) :
  AbstractModelBuilder<JavaProjectModelBuilderParams, IJavaProject>(initializationParams) {

  override fun build(param: JavaProjectModelBuilderParams): IJavaProject {
    val compilerSettings = createCompilerSettings(param.project, param.module)
    return JavaProjectImpl(param.module, compilerSettings, param.modulePaths)
  }

  private fun createCompilerSettings(
    ideaProject: IdeaProject, module: IdeaModule): IJavaCompilerSettings {
    val javaLanguageSettings = module.javaLanguageSettings
      ?: return createCompilerSettings(ideaProject)
    val languageLevel = javaLanguageSettings.languageLevel
    val targetBytecodeVersion = javaLanguageSettings.targetBytecodeVersion
    if (languageLevel == null || targetBytecodeVersion == null) {
      return createCompilerSettings(ideaProject)
    }
    val source = languageLevel.toString()
    val target = targetBytecodeVersion.toString()
    return JavaModuleCompilerSettings(source, target)
  }

  private fun createCompilerSettings(ideaProject: IdeaProject): IJavaCompilerSettings {
    val settings = ideaProject.javaLanguageSettings ?: return JavaModuleCompilerSettings()
    val source = settings.languageLevel
    val target = settings.targetBytecodeVersion
    return if (source == null || target == null) {
      JavaModuleCompilerSettings()
    } else JavaModuleCompilerSettings(source.toString(), target.toString())
  }
}