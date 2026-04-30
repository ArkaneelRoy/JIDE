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

package com.willow.androidide.ultra.actions.filetree

import android.content.Context
import android.content.Intent
import com.willow.androidide.ultra.actions.ActionData
import com.willow.androidide.ultra.actions.requireFile
import com.willow.androidide.ultra.resources.R
import com.willow.androidide.ultra.utils.IntentUtils

/**
 * File tree action to open files with external applications.
 *
 * @author Akash Yadav
 */
class OpenWithAction(context: Context, override val order: Int) :
  BaseFileTreeAction(
    context = context,
    labelRes = R.string.open_with,
    iconRes = R.drawable.ic_open_with
  ) {

  override val id: String = "ide.editor.fileTree.openWith"

  override suspend fun execAction(data: ActionData) {
    IntentUtils.startIntent(data.requireActivity(), data.requireFile(), "*/*", Intent.ACTION_VIEW)
  }
}
