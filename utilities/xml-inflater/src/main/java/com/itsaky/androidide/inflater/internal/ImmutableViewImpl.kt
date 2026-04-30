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

package com.willow.androidide.ultra.inflater.internal

import com.willow.androidide.ultra.inflater.IAttribute
import com.willow.androidide.ultra.inflater.IView
import com.willow.androidide.ultra.inflater.IView.AttributeChangeListener

/**
 * An immutable implementation of [IView].
 *
 * @author Akash Yadav
 */
class ImmutableViewImpl(private val src: ViewImpl) : IView by src {
  
  override fun addAttribute(attribute: IAttribute, apply: Boolean, update: Boolean) {
    throw UnsupportedOperationException("Immutable!")
  }
  
  override fun removeAttribute(attribute: IAttribute) {
    throw UnsupportedOperationException("Immutable!")
  }
  
  override fun updateAttribute(attribute: IAttribute) {
    throw UnsupportedOperationException("Immutable!")
  }
  
  override fun onHighlightStateUpdated(highlight: Boolean) {
    throw UnsupportedOperationException("Immutable!")
  }
  
  override fun registerAttributeChangeListener(listener: AttributeChangeListener) {
    throw UnsupportedOperationException("Immutable!")
  }
  
  override fun unregisterAttributeChangeListener(listener: AttributeChangeListener) {
    throw UnsupportedOperationException("Immutable!")
  }
}