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
package com.willow.androidide.ultra.lsp.java.actions.diagnostics

import com.willow.androidide.ultra.actions.ActionData
import com.willow.androidide.ultra.actions.hasRequiredData
import com.willow.androidide.ultra.actions.markInvisible
import com.willow.androidide.ultra.actions.requireFile
import com.willow.androidide.ultra.actions.requirePath
import com.willow.androidide.ultra.lsp.java.JavaCompilerProvider
import com.willow.androidide.ultra.lsp.java.actions.BaseJavaCodeAction
import com.willow.androidide.ultra.lsp.java.models.DiagnosticCode
import com.willow.androidide.ultra.lsp.java.rewrite.ConvertVariableToStatement
import com.willow.androidide.ultra.lsp.java.utils.CodeActionUtils.findPosition
import com.willow.androidide.ultra.projects.IProjectManager
import com.willow.androidide.ultra.resources.R
import org.slf4j.LoggerFactory

/** @author Akash Yadav */
class VariableToStatementAction : BaseJavaCodeAction() {

  override val id: String = "ide.editor.lsp.java.diagnostics.variableToStatement"
  override var label: String = ""
  private val diagnosticCode = DiagnosticCode.UNUSED_LOCAL.id

  override val titleTextRes: Int = R.string.action_convert_to_statement

  companion object {

    private val log = LoggerFactory.getLogger(VariableToStatementAction::class.java)
  }

  override fun prepare(data: ActionData) {
    super.prepare(data)

    if (!visible) {
      return
    }

    if (!data.hasRequiredData(com.willow.androidide.ultra.lsp.models.DiagnosticItem::class.java)) {
      markInvisible()
      return
    }

    val diagnostic = data.get(com.willow.androidide.ultra.lsp.models.DiagnosticItem::class.java)!!
    if (diagnosticCode != diagnostic.code) {
      markInvisible()
      return
    }

    visible = true
    enabled = true
  }

  override suspend fun execAction(data: ActionData): Any {
    val diagnostic = data[com.willow.androidide.ultra.lsp.models.DiagnosticItem::class.java]!!
    val compiler =
      JavaCompilerProvider.get(
        IProjectManager.getInstance().getWorkspace()?.findModuleForFile(data.requireFile(), false)
          ?: return Any()
      )
    val path = data.requirePath()

    return compiler.compile(path).get {
      ConvertVariableToStatement(path, findPosition(it, diagnostic.range.start))
    }
  }

  override fun postExec(data: ActionData, result: Any) {
    if (result !is ConvertVariableToStatement) {
      log.warn("Unable to convert variable to statement")
      return
    }

    performCodeAction(data, result)
  }
}
