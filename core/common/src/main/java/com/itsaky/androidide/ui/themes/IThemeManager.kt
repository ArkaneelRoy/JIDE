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

package com.willow.androidide.ultra.ui.themes

import android.app.Activity
import com.willow.androidide.ultra.utils.ServiceLoader

/**
 * Theme manager for AndroidIDE Ultra.
 *
 * @author Akash Yadav
 */
interface IThemeManager {

  companion object {
    private object Provider {
      val instance by lazy { ServiceLoader.load(IThemeManager::class.java).findFirstOrThrow() }
    }

    /**
     * Get the [IThemeManager] instance.
     */
    fun getInstance() : IThemeManager = Provider.instance
  }

  /**
   * Apply current theme to [activity].
   */
  fun applyTheme(activity: Activity)

  /**
   * Get the current IDE theme.
   */
  fun getCurrentTheme(): IDETheme
}