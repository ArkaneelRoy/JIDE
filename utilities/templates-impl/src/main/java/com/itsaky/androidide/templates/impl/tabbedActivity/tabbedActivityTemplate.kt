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

package com.willow.androidide.ultra.templates.impl.tabbedActivity

import com.willow.androidide.ultra.templates.Language
import com.willow.androidide.ultra.templates.base.AndroidModuleTemplateBuilder
import com.willow.androidide.ultra.templates.base.models.Dependency
import com.willow.androidide.ultra.templates.base.modules.android.defaultAppModule
import com.willow.androidide.ultra.templates.impl.R
import com.willow.androidide.ultra.templates.impl.base.createRecipe
import com.willow.androidide.ultra.templates.impl.base.emptyThemesAndColors
import com.willow.androidide.ultra.templates.impl.base.writeMainActivity
import com.willow.androidide.ultra.templates.impl.baseProjectImpl
import com.willow.androidide.ultra.templates.impl.templateAsset

fun tabbedActivityProject() = baseProjectImpl {
  templateName = R.string.template_tabs
  thumb = R.drawable.template_blank_activity_tabs
  defaultAppModule {
    recipe = createRecipe {
      sources {
        writeMainActivity(this, ktSrc = ::tabbedActivitySrcKt,
          javaSrc = ::tabbedActivitySrcJava)
      }

      res {
        copyAssetsRecursively(templateAsset("tabbed", "res"), mainResDir())

        putStringRes("tab_text_1", "Tab 1")
        putStringRes("tab_text_2", "Tab 2")
        putStringRes("tab_text_3", "Tab 3")

        emptyThemesAndColors()
      }

      if (data.language == Language.Kotlin) {
        tabbedActivityProjectKt()
      } else {
        tabbedActivityProjectJava()
      }
    }
  }
}

fun AndroidModuleTemplateBuilder.tabbedActivityProjectKt() {
  executor.apply {
    addDependency(Dependency.AndroidX.LifeCycle_LiveData_Ktx)
    addDependency(Dependency.AndroidX.LifeCycle_ViewModel_Ktx)

    sources {
      writeKtSrc("${data.packageName}.ui.main", "SectionsPagerAdapter",
        source = ::tabbedPagerAdapterSrcKt)
      writeKtSrc("${data.packageName}.ui.main", "PageViewModel",
        source = ::tabbedPageViewModelSrcKt)
      writeKtSrc("${data.packageName}.ui.main", "PlaceholderFragment",
        source = ::tabbedPlaceholderFragmentSrcKt)
    }
  }
}

fun AndroidModuleTemplateBuilder.tabbedActivityProjectJava() {
  executor.apply {
    addDependency(Dependency.AndroidX.LifeCycle_LiveData)
    addDependency(Dependency.AndroidX.LifeCycle_ViewModel)

    sources {
      writeJavaSrc("${data.packageName}.ui.main", "SectionsPagerAdapter",
        source = ::tabbedPagerAdapterSrcJava)
      writeJavaSrc("${data.packageName}.ui.main", "PageViewModel",
        source = ::tabbedPageViewModelSrcJava)
      writeJavaSrc("${data.packageName}.ui.main", "PlaceholderFragment",
        source = ::tabbedPlaceholderFragmentSrcJava)
    }
  }
}
