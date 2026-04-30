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

import android.widget.HorizontalScrollView
import com.willow.androidide.ultra.annotations.uidesigner.IncludeInDesigner
import com.willow.androidide.ultra.annotations.uidesigner.IncludeInDesigner.Group.LAYOUTS
import com.willow.androidide.ultra.inflater.AttributeHandlerScope
import com.willow.androidide.ultra.inflater.IView
import com.willow.androidide.ultra.inflater.IViewGroup
import com.willow.androidide.ultra.inflater.models.UiWidget
import com.willow.androidide.ultra.resources.R

/**
 * View adapter for [HorizontalScrollView].
 *
 * @author Akash Yadav
 */
@com.willow.androidide.ultra.annotations.inflater.ViewAdapter(
  HorizontalScrollView::class)
@IncludeInDesigner(group = LAYOUTS)
open class HorizontalScrollViewAdapter<T : HorizontalScrollView> :
  FrameLayoutAdapter<T>() {

  override fun createAttrHandlers(
    create: (String, AttributeHandlerScope<T>.() -> Unit) -> Unit
  ) {
    super.createAttrHandlers(create)
    create("fillViewPort") { view.isFillViewport = parseBoolean(value, false) }
  }

  override fun createUiWidgets(): List<UiWidget> {
    return listOf(UiWidget(HorizontalScrollView::class.java,
      R.string.widget_horizontal_scrollview,
      R.drawable.ic_widget_horizontal_scroll_view))
  }

  override fun canAcceptChild(view: IViewGroup, child: IView?, name: String
  ): Boolean {
    // scrollview can have only one child
    return view.childCount == 0
  }
}
