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

package com.willow.androidide.ultra.lsp.java.actions

import android.content.Context
import android.graphics.drawable.Drawable
import com.willow.androidide.ultra.actions.ActionData
import com.willow.androidide.ultra.actions.ActionItem
import com.willow.androidide.ultra.actions.EditorActionItem
import com.willow.androidide.ultra.actions.hasRequiredData
import com.willow.androidide.ultra.actions.markInvisible
import com.willow.androidide.ultra.actions.requireFile
import com.willow.androidide.ultra.lsp.api.ILanguageClient
import com.willow.androidide.ultra.lsp.api.ILanguageServerRegistry
import com.willow.androidide.ultra.lsp.java.JavaCompilerProvider
import com.willow.androidide.ultra.lsp.java.JavaLanguageServer
import com.willow.androidide.ultra.lsp.java.R
import com.willow.androidide.ultra.lsp.java.compiler.JavaCompilerService
import com.willow.androidide.ultra.lsp.java.rewrite.Rewrite
import com.willow.androidide.ultra.projects.IProjectManager
import com.willow.androidide.ultra.utils.DocumentUtils
import com.willow.androidide.ultra.utils.ILogger
import com.willow.androidide.ultra.utils.flashError
import java.io.File

/**
 * Base class for java code actions
 *
 * @author Akash Yadav
 */
abstract class BaseJavaCodeAction : EditorActionItem {

  override var visible: Boolean = true
  override var enabled: Boolean = true
  override var icon: Drawable? = null
  override var requiresUIThread: Boolean = false
  override var location: ActionItem.Location = ActionItem.Location.EDITOR_CODE_ACTIONS

  protected abstract val titleTextRes: Int

  override fun prepare(data: ActionData) {
    super.prepare(data)
    if (
      !data.hasRequiredData(Context::class.java, JavaLanguageServer::class.java, File::class.java)
    ) {
      markInvisible()
      return
    }

    if (titleTextRes != -1) {
      label = data[Context::class.java]!!.getString(titleTextRes)
    }

    val file = data.requireFile()
    visible = DocumentUtils.isJavaFile(file.toPath())
    enabled = visible
  }

  fun performCodeAction(data: ActionData, result: Rewrite) {
    val compiler = data.requireCompiler()

    val actions =
      try {
        result.asCodeActions(compiler, label)
      } catch (e: Exception) {
        flashError(e.cause?.message ?: e.message)
        ILogger.ROOT.error(e.cause?.message ?: e.message, e)
        return
      }

    if (actions == null) {
      onPerformCodeActionFailed(data)
      return
    }

    data.getLanguageClient()?.performCodeAction(actions)
  }

  protected open fun onPerformCodeActionFailed(data: ActionData) {
    flashError(R.string.msg_codeaction_failed)
  }

  protected fun ActionData.requireLanguageServer(): JavaLanguageServer {
    return ILanguageServerRegistry.getDefault().getServer(JavaLanguageServer.SERVER_ID)
        as JavaLanguageServer
  }

  protected fun ActionData.getLanguageClient(): ILanguageClient? {
    return requireLanguageServer().client
  }

  protected fun ActionData.requireCompiler(): JavaCompilerService {
    val module =
      IProjectManager.getInstance().getWorkspace()?.findModuleForFile(requireFile(), false)
    requireNotNull(module) {
      "Cannot get compiler instance. Unable to find module for file: ${requireFile().name}"
    }
    return JavaCompilerProvider.get(module)
  }
}
