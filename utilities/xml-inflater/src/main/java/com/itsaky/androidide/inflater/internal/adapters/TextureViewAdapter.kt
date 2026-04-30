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

import android.content.Context
import android.view.TextureView
import android.view.View
import com.willow.androidide.ultra.annotations.uidesigner.IncludeInDesigner
import com.willow.androidide.ultra.annotations.uidesigner.IncludeInDesigner.Group.WIDGETS
import com.willow.androidide.ultra.inflater.AttributeHandlerScope
import com.willow.androidide.ultra.inflater.internal.ui.DesignerTextureView
import com.willow.androidide.ultra.inflater.models.UiWidget
import com.willow.androidide.ultra.resources.R.drawable
import com.willow.androidide.ultra.resources.R.string

/**
 * Attribute adapter for [TextureView].
 *
 * @author Akash Yadav
 */
@com.willow.androidide.ultra.annotations.inflater.ViewAdapter(TextureView::class)
@IncludeInDesigner(group = WIDGETS)
open class TextureViewAdapter<T : TextureView> : ViewAdapter<T>() {

  private val unsupportedAttrs = arrayOf("background", "foreground")

  override fun postCreateAttrHandlers(
    handlers: MutableMap<String, AttributeHandlerScope<T>.() -> Unit>) {

    unsupportedAttrs.forEach(handlers::remove)
  }

  override fun createUiWidgets(): List<UiWidget> {
    return listOf(
      UiWidget(DesignerTextureView::class.java, string.widget_textureview,
        drawable.ic_widget_textureview)
    )
  }

  override fun onCreateView(name: String, context: Context): View? {
    if (name == TextureView::class.java.name) {
      return DesignerTextureView(context)
    }
    return super.onCreateView(name, context)
  }
}
