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

package com.willow.androidide.ultra.lsp.xml

import com.willow.androidide.ultra.lsp.models.CompletionItem
import com.willow.androidide.ultra.lsp.models.CompletionParams
import com.willow.androidide.ultra.progress.ICancelChecker

/** @author Akash Yadav */
class CompletionHelperImpl : CompletionHelper {
  override fun complete(transform: (CompletionItem) -> CharSequence): Pair<Boolean, List<CharSequence>> {
    return XMLLSPTest.run {
      val createCompletionParams = createCompletionParams()
      val result = server.complete(createCompletionParams)
      result.isIncomplete to
        result.items
          .filter { it.ideLabel.isNotBlank() }
          .map { transform(it) }
          .filter { it.isNotBlank() }
          .toList()
    }
  }

  private fun createCompletionParams(): CompletionParams {
    return XMLLSPTest.run {
      val cursor = cursorPosition(true)
      val completionParams = CompletionParams(cursor, file!!, ICancelChecker.NOOP)
      completionParams.position.index = this.cursor
      completionParams.content = contents
      completionParams
    }
  }
}
