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

import android.webkit.WebView
import com.willow.androidide.ultra.annotations.inflater.ViewAdapter
import com.willow.androidide.ultra.annotations.uidesigner.IncludeInDesigner
import com.willow.androidide.ultra.annotations.uidesigner.IncludeInDesigner.Group.WIDGETS
import com.willow.androidide.ultra.inflater.models.UiWidget
import com.willow.androidide.ultra.resources.R.drawable
import com.willow.androidide.ultra.resources.R.string

/**
 * Attribute adapter for [WebView].
 *
 * @author Akash Yadav
 */
@ViewAdapter(WebView::class)
@IncludeInDesigner(group = WIDGETS)
open class WebViewAdapter<T : WebView> : AbsoluteLayoutAdapter<T>() {
  override fun createUiWidgets(): List<UiWidget> {
    return listOf(UiWidget(WebView::class.java, string.widget_webview, drawable.ic_widget_webview))
  }
}
