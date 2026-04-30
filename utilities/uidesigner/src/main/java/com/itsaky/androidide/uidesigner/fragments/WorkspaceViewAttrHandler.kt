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

package com.willow.androidide.ultra.uidesigner.fragments

import com.willow.androidide.ultra.uidesigner.models.UiAttribute
import com.willow.androidide.ultra.uidesigner.undo.AttrAddedAction
import com.willow.androidide.ultra.uidesigner.undo.AttrRemovedAction
import com.willow.androidide.ultra.uidesigner.undo.AttrUpdatedAction

/**
 * Handles view attribute changes in [DesignerWorkspaceFragment].
 *
 * @author Akash Yadav
 */
internal class WorkspaceViewAttrHandler : com.willow.androidide.ultra.inflater.IView.AttributeChangeListener {

  private var fragment: DesignerWorkspaceFragment? = null

  internal fun init(fragment: DesignerWorkspaceFragment) {
    this.fragment = fragment
  }

  internal fun release() {
    this.fragment = null
  }

  override fun onAttributeAdded(view: com.willow.androidide.ultra.inflater.IView, attribute: com.willow.androidide.ultra.inflater.IAttribute) {
    val frag = this.fragment ?: return
    frag.undoManager.push(AttrAddedAction(view = view, attr = attribute as UiAttribute))
  }

  override fun onAttributeRemoved(view: com.willow.androidide.ultra.inflater.IView, attribute: com.willow.androidide.ultra.inflater.IAttribute) {
    val frag = this.fragment ?: return
    frag.undoManager.push(AttrRemovedAction(view = view, attr = attribute as UiAttribute))
  }

  override fun onAttributeUpdated(view: com.willow.androidide.ultra.inflater.IView, attribute: com.willow.androidide.ultra.inflater.IAttribute, oldValue: String) {
    val frag = this.fragment ?: return
    frag.undoManager.push(
      AttrUpdatedAction(
        view = view,
        attr = attribute as UiAttribute,
        oldValue = oldValue
      )
    )
  }
}
