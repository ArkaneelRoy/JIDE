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

package com.willow.androidide.ultra.treesitter.api

import com.willow.androidide.ultra.treesitter.TSParser
import com.willow.androidide.ultra.utils.DefaultRecyclable
import com.willow.androidide.ultra.utils.RecyclableObjectPool

/**
 * @author Akash Yadav
 */
class TreeSitterParser @JvmOverloads internal constructor(
  pointer: Long = 0
) : TSParser(pointer), RecyclableObjectPool.Recyclable by DefaultRecyclable() {

  companion object {

    @JvmStatic
    fun obtain(
      pointer: Long
    ): TreeSitterParser {
      return obtainFromPool<TreeSitterParser>().apply {
        this.nativeObject = pointer
      }
    }
  }

  override fun close() {
    super.close()
    recycle()
  }

  override fun recycle() {
    this.nativeObject = 0
    returnToPool()
  }
}