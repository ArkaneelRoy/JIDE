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

package com.willow.androidide.ultra.templates.impl.noActivity

import com.willow.androidide.ultra.templates.base.modules.android.defaultAppModule
import com.willow.androidide.ultra.resources.R
import com.willow.androidide.ultra.templates.impl.base.createRecipe
import com.willow.androidide.ultra.templates.impl.base.emptyThemesAndColors
import com.willow.androidide.ultra.templates.impl.baseProjectImpl

fun noActivityProjectTemplate() = baseProjectImpl {
  templateName = R.string.template_no_activity
  thumb = R.drawable.template_no_activity
  defaultAppModule {
    recipe = createRecipe {
      res {
        emptyThemesAndColors(actionBar = true)
      }
    }
  }
}