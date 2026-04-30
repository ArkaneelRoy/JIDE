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

import android.widget.RatingBar
import com.willow.androidide.ultra.annotations.inflater.ViewAdapter
import com.willow.androidide.ultra.annotations.uidesigner.IncludeInDesigner
import com.willow.androidide.ultra.annotations.uidesigner.IncludeInDesigner.Group.WIDGETS
import com.willow.androidide.ultra.inflater.AttributeHandlerScope
import com.willow.androidide.ultra.inflater.models.UiWidget
import com.willow.androidide.ultra.resources.R.drawable
import com.willow.androidide.ultra.resources.R.string

/**
 * Attribute adapter for [RatingBar].
 *
 * @author Deep Kr. Ghosh
 */
@ViewAdapter(RatingBar::class)
@IncludeInDesigner(group = WIDGETS)
open class RatingBarAdapter<T : RatingBar> : AbsSeekBarAdapter<T>() {
  override fun createAttrHandlers(create: (String, AttributeHandlerScope<T>.() -> Unit) -> Unit) {
    super.createAttrHandlers(create)
    create("isIndicator") { view.setIsIndicator(parseBoolean(value)) }
    create("numStars") { view.numStars = parseInteger(value) }
    create("rating") { view.rating = parseFloat(value) }
    create("stepSize") { view.stepSize = parseFloat(value) }
  }

  override fun createUiWidgets(): List<UiWidget> {
    return listOf(
      UiWidget(RatingBar::class.java, string.widget_rating_bar, drawable.ic_widget_rating_bar)
    )
  }
}
