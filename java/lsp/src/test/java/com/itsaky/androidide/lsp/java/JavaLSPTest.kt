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

package com.willow.androidide.ultra.lsp.java

import com.willow.androidide.ultra.lsp.api.ILanguageServerRegistry
import com.willow.androidide.ultra.lsp.testing.LSPTest
import com.willow.androidide.ultra.lsp.java.compiler.JavaCompilerService
import com.willow.androidide.ultra.projects.util.findAppModule
import org.junit.Before
import org.junit.Ignore

/** @author Akash Yadav */
@Ignore("Base singleton class")
object JavaLSPTest : LSPTest("java") {

  val server by lazy {
    ILanguageServerRegistry.getDefault().getServer(JavaLanguageServer.SERVER_ID)
        as JavaLanguageServer
  }

  @Before
  fun setup() {
    log.debug("Initializing project...")
    initProjectIfNeeded()
  }

  override fun registerServer() {
    ILanguageServerRegistry.getDefault().register(JavaLanguageServer())
  }

  override fun getServerId() = JavaLanguageServer.SERVER_ID

  fun getCompiler(): JavaCompilerService {
    return JavaCompilerProvider.get(findAppModule()!!)
  }

  override fun test() {}
}
