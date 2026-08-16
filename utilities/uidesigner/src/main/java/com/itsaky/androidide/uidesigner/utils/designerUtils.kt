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

package com.willow.androidide.ultra.uidesigner.utils

import android.content.Context
import android.graphics.PorterDuff.Mode.SRC_ATOP
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat.getDrawable
import com.google.android.material.R as MaterialR
import com.willow.androidide.ultra.resources.R as SharedR
import com.willow.androidide.ultra.uidesigner.drawable.UiViewLayeredForeground
import com.willow.androidide.ultra.utils.resolveAttr

fun layeredForeground(context: Context, drawable: Drawable): Drawable {
  return UiViewLayeredForeground(context, drawable)
}

@JvmOverloads
fun bgDesignerView(
  context: Context,
  color: Int = context.resolveAttr(MaterialR.attr.colorOutline)
): Drawable? {
  return getDrawable(context, SharedR.drawable.bg_designer_view)?.apply {
    colorFilter = PorterDuffColorFilter(color, SRC_ATOP)
  }
}
