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

package com.willow.androidide.ultra.lsp.java.providers.definition

import com.willow.androidide.ultra.lsp.api.IServerSettings
import com.willow.androidide.ultra.lsp.java.compiler.JavaCompilerService
import com.willow.androidide.ultra.models.Location
import com.willow.androidide.ultra.models.Position
import com.willow.androidide.ultra.progress.ICancelChecker
import jdkx.lang.model.element.Element
import jdkx.tools.JavaFileObject
import java.nio.file.Path

/**
 * Finds definition of an element in other source locations.
 *
 * @author Akash Yadav
 */
class RemoteDefinitionProvider(
  position: Position,
  completingFile: Path,
  compiler: JavaCompilerService,
  settings: IServerSettings, cancelChecker: ICancelChecker,
) : IJavaDefinitionProvider(position, completingFile, compiler, settings, cancelChecker) {

  private lateinit var otherFile: JavaFileObject

  fun setOtherFile(jfo: JavaFileObject): RemoteDefinitionProvider {
    this.otherFile = jfo
    return this
  }

  override fun doFindDefinition(element: Element): List<Location> {
//    val task = compiler.compile(listOf(SourceFileObject(file), otherFile))
    val provider = LocalDefinitionProvider(position, file, compiler, settings, this)
    return provider.findDefinition(element)
//    return provider
//      .findDefinition(task.get { NavigationHelper.findElement(it, file, line, column) })
  }
}
