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

package com.willow.androidide.ultra.templates.impl.basicActivity

import com.willow.androidide.ultra.templates.ProjectTemplate
import com.willow.androidide.ultra.templates.base.AndroidModuleTemplateBuilder
import com.willow.androidide.ultra.templates.base.modules.android.defaultAppModule
import com.willow.androidide.ultra.templates.base.util.AndroidModuleResManager.ResourceType.LAYOUT
import com.willow.androidide.ultra.templates.base.util.SourceWriter
import com.willow.androidide.ultra.resources.R
import com.willow.androidide.ultra.templates.impl.base.createRecipe
import com.willow.androidide.ultra.templates.impl.base.emptyThemesAndColors
import com.willow.androidide.ultra.templates.impl.base.writeMainActivity
import com.willow.androidide.ultra.templates.impl.baseProjectImpl

fun basicActivityProject(): ProjectTemplate {
  return baseProjectImpl {
    templateName = R.string.template_basic
    thumb = R.drawable.template_basic_activity
    defaultAppModule {
      recipe = createRecipe {
        sources {
          writeBasicActivitySrc(this)
        }

        res {
          writeBasicActivityLayout()
          emptyThemesAndColors()
        }
      }
    }
  }
}

private fun AndroidModuleTemplateBuilder.writeBasicActivitySrc(
  writer: SourceWriter
) {
  writeMainActivity(writer = writer, ktSrc = ::basicActivitySrcKt,
    javaSrc = ::basicActivitySrcJava)
}

internal fun AndroidModuleTemplateBuilder.writeBasicActivityLayout() {
  res.apply {
    writeXmlResource("activity_main", LAYOUT, source = ::basicActivityLayout)
    writeXmlResource("content_main", LAYOUT, source = ::basicActivityContent)
  }
}