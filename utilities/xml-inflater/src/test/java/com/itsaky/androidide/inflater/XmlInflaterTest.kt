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

package com.willow.androidide.ultra.inflater

import androidx.appcompat.app.AppCompatActivity
import com.google.common.truth.Truth.assertThat
import com.willow.androidide.ultra.inflater.utils.endParse
import com.willow.androidide.ultra.inflater.utils.startParse
import com.willow.androidide.ultra.lookup.Lookup
import com.willow.androidide.ultra.projects.IProjectManager
import com.willow.androidide.ultra.projects.android.AndroidModule
import com.willow.androidide.ultra.projects.builder.BuildService
import com.willow.androidide.ultra.projects.util.findAppModule
import com.willow.androidide.ultra.testing.tooling.ToolingApiTestLauncher
import com.willow.androidide.ultra.testing.tooling.models.ToolingApiTestLauncherParams
import kotlinx.coroutines.runBlocking
import org.junit.Ignore
import org.robolectric.Robolectric
import java.util.concurrent.atomic.AtomicBoolean

@Ignore("Test utility provider")
object XmlInflaterTest {

  private var init = AtomicBoolean(false)
  internal val activity by lazy { Robolectric.buildActivity(AppCompatActivity::class.java).get() }

  fun initIfNeeded() {
    if (init.get()) {
      return
    }

    val params = ToolingApiTestLauncherParams()
    ToolingApiTestLauncher.launchServer(params) {
      assertThat(result?.isSuccessful).isTrue()

      Lookup.getDefault().register(BuildService.KEY_PROJECT_PROXY, project)

      val projectManager = IProjectManager.getInstance()
      projectManager.openProject(params.projectDir.toFile())

      runBlocking { projectManager.setupProject(project) }

      init.set(true)
    }
  }
}

fun inflaterTest(block: (AndroidModule) -> Unit) {
  XmlInflaterTest.initIfNeeded()
  val app = findAppModule()!!
  startParse(app)
  block(app)
  endParse()
}

fun requiresActivity(block: AppCompatActivity.() -> Unit) {
  XmlInflaterTest.activity.block()
}
