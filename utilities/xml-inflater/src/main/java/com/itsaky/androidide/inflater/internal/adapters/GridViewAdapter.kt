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

import android.widget.GridView
import com.willow.androidide.ultra.annotations.inflater.ViewAdapter
import com.willow.androidide.ultra.annotations.uidesigner.IncludeInDesigner
import com.willow.androidide.ultra.annotations.uidesigner.IncludeInDesigner.Group.LAYOUTS
import com.willow.androidide.ultra.inflater.AttributeHandlerScope
import com.willow.androidide.ultra.inflater.models.UiWidget
import com.willow.androidide.ultra.resources.R.drawable
import com.willow.androidide.ultra.resources.R.string

/**
 * Attribute adapter for [GridView].
 *
 * @author Deep Kr. Ghosh
 */
@ViewAdapter(GridView::class)
@IncludeInDesigner(group = LAYOUTS)
open class GridViewAdapter<T : GridView> : AbsListViewAdapter<T>() {

  override fun createAttrHandlers(create: (String, AttributeHandlerScope<T>.() -> Unit) -> Unit) {
    super.createAttrHandlers(create)
    create("columnWidth") { view.columnWidth = parseDimension(context, value) }
    create("gravity") { view.gravity = parseGravity(value) }
    create("horizontalSpacing") { view.horizontalSpacing = parseDimension(context, value) }
    create("numColumns") {
      view.numColumns = if (value == "auto_fit") GridView.AUTO_FIT else parseInteger(value)
    }
    create("stretchMode") { view.stretchMode = parseStretchMode(value) }
    create("verticalSpacing") { view.verticalSpacing = parseDimension(context, value) }
  }

  override fun createUiWidgets(): List<UiWidget> {
    return listOf(
      UiWidget(GridView::class.java, string.widget_grid_view, drawable.ic_widget_grid_view)
    )
  }

  protected fun parseStretchMode(value: String): Int {
    return when (value) {
      "columnWidth" -> GridView.STRETCH_COLUMN_WIDTH
      "none" -> GridView.NO_STRETCH
      "spacingWidth" -> GridView.STRETCH_SPACING
      "spacingWidthUniform" -> GridView.STRETCH_SPACING_UNIFORM
      else -> GridView.NO_STRETCH
    }
  }
}
