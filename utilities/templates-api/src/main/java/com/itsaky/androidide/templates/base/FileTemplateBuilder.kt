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

package com.willow.androidide.ultra.templates.base

import com.willow.androidide.ultra.templates.FileTemplate
import com.willow.androidide.ultra.templates.FileTemplateRecipeResult
import com.willow.androidide.ultra.templates.TemplateBuilder
import java.io.File

/**
 * [TemplateBuilder] implementation for building files templates.
 *
 * @property dir The directory in which the file must be created.
 * @author Akash Yadav
 */
class FileTemplateBuilder<R: FileTemplateRecipeResult>(val dir: File) : TemplateBuilder<R>() {

  override fun buildInternal(): FileTemplate<R> {
    return FileTemplate(templateName!!, thumb!!, widgets!!, recipe!!)
  }
}