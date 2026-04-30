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

package com.willow.androidide.ultra.inflater.internal.adapters

import android.widget.CheckedTextView
import com.willow.androidide.ultra.annotations.inflater.ViewAdapter
import com.willow.androidide.ultra.annotations.uidesigner.IncludeInDesigner
import com.willow.androidide.ultra.annotations.uidesigner.IncludeInDesigner.Group.WIDGETS
import com.willow.androidide.ultra.inflater.AttributeHandlerScope
import com.willow.androidide.ultra.inflater.models.UiWidget
import com.willow.androidide.ultra.resources.R.drawable
import com.willow.androidide.ultra.resources.R.string

/**
 * Attribute adapter for [CheckedTextView].
 *
 * @author Akash Yadav
 */
@ViewAdapter(CheckedTextView::class)
@IncludeInDesigner(group = WIDGETS)
open class CheckedTextViewAdapter<T : CheckedTextView> : TextViewAdapter<T>() {

  override fun createAttrHandlers(create: (String, AttributeHandlerScope<T>.() -> Unit) -> Unit) {
    super.createAttrHandlers(create)
    create("checkMarkTintMode") { view.checkMarkTintMode = parsePorterDuffMode(value) }
    create("checkMarkTint") { view.checkMarkTintList = parseColorStateList(context, value) }
  }

  override fun createUiWidgets(): List<UiWidget> {
    return listOf(
      UiWidget(
        CheckedTextView::class.java,
        string.widget_checked_textview,
        drawable.ic_widget_checked_textview
      )
    )
  }
}
