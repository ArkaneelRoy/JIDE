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

package com.willow.androidide.ultra.lsp.models

import com.willow.androidide.ultra.lsp.CancellableRequestParams
import com.willow.androidide.ultra.models.Location
import com.willow.androidide.ultra.models.Position
import com.willow.androidide.ultra.progress.ICancelChecker
import java.nio.file.Path

/** @author Akash Yadav */
data class ReferenceParams(
  var file: Path,
  var position: Position,
  var includeDeclaration: Boolean,
  override val cancelChecker: ICancelChecker
) : CancellableRequestParams

data class ReferenceResult(var locations: List<Location>)
