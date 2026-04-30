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

package com.willow.androidide.ultra.adapters

import com.willow.androidide.ultra.models.IconTitleDescriptionItem

/**
 * Simple implementation of [IconTitleDescriptionAdapter].
 *
 * @author Akash Yadav
 */
open class SimpleIconTitleDescriptionAdapter(
  protected val items: List<IconTitleDescriptionItem>
) : IconTitleDescriptionAdapter() {

  override fun getItem(position: Int): IconTitleDescriptionItem {
    return items[position]
  }

  override fun getItemCount(): Int {
    return items.size
  }
}