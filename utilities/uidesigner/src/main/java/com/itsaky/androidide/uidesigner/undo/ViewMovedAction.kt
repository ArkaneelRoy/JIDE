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

package com.willow.androidide.ultra.uidesigner.undo

/**
 * Represents the action when the user moves a view from one view group to another.
 *
 * @author Akash Yadav
 */
class ViewMovedAction(
  private val child: com.willow.androidide.ultra.inflater.IView,
  private val fromParent: com.willow.androidide.ultra.inflater.IViewGroup,
  private val toParent: com.willow.androidide.ultra.inflater.IViewGroup,
  private val fromIndex: Int,
  private val toIndex: Int
) : IUiAction {
  
  override fun undo() {
    child.removeFromParent()
    fromParent.addChild(fromParent.validateIndex(fromIndex), child)
  }

  override fun redo() {
    child.removeFromParent()
    toParent.addChild(toParent.validateIndex(toIndex), child)
  }
}
