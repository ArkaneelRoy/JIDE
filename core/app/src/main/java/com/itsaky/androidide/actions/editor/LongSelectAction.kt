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

package com.willow.androidide.ultra.actions.editor

import android.content.Context
import androidx.core.content.ContextCompat
import com.willow.androidide.ultra.resources.R
import com.willow.androidide.ultra.actions.ActionData
import com.willow.androidide.ultra.actions.ActionItem
import com.willow.androidide.ultra.actions.EditorRelatedAction
import com.willow.androidide.ultra.actions.markInvisible

/**
 * An action to long select text in the editor.
 *
 * @author Akash Yadav
 */
class LongSelectAction(context: Context, override val order: Int) : EditorRelatedAction() {

  override val id: String = "ide.editor.code.text.longSelect"
  override var location: ActionItem.Location = ActionItem.Location.EDITOR_TEXT_ACTIONS

  init {
    label = context.getString(R.string.title_begin_long_select)
    icon = ContextCompat.getDrawable(context, R.drawable.ic_expand_selection)
  }

  override fun prepare(data: ActionData) {
    super.prepare(data)
    data.getEditor() ?: markInvisible()
  }

  override suspend fun execAction(data: ActionData): Any {
    return data.getEditor()?.beginLongSelect() ?: false
  }
}