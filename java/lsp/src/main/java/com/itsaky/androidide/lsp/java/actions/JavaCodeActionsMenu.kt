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

import com.willow.androidide.ultra.actions.ActionItem
import com.willow.androidide.ultra.lsp.actions.IActionsMenuProvider
import com.willow.androidide.ultra.lsp.java.actions.common.CommentAction
import com.willow.androidide.ultra.lsp.java.actions.common.FindReferencesAction
import com.willow.androidide.ultra.lsp.java.actions.common.GoToDefinitionAction
import com.willow.androidide.ultra.lsp.java.actions.common.OrganizeImportsAction
import com.willow.androidide.ultra.lsp.java.actions.common.RemoveUnusedImportsAction
import com.willow.androidide.ultra.lsp.java.actions.common.UncommentAction
import com.willow.androidide.ultra.lsp.java.actions.diagnostics.AddImportAction
import com.willow.androidide.ultra.lsp.java.actions.diagnostics.AddThrowsAction
import com.willow.androidide.ultra.lsp.java.actions.diagnostics.AutoFixImportsAction
import com.willow.androidide.ultra.lsp.java.actions.diagnostics.CreateMissingMethodAction
import com.willow.androidide.ultra.lsp.java.actions.diagnostics.FieldToBlockAction
import com.willow.androidide.ultra.lsp.java.actions.diagnostics.ImplementAbstractMethodsAction
import com.willow.androidide.ultra.lsp.java.actions.diagnostics.RemoveClassAction
import com.willow.androidide.ultra.lsp.java.actions.diagnostics.RemoveMethodAction
import com.willow.androidide.ultra.lsp.java.actions.diagnostics.RemoveUnusedThrowsAction
import com.willow.androidide.ultra.lsp.java.actions.diagnostics.SuppressUncheckedWarningAction
import com.willow.androidide.ultra.lsp.java.actions.diagnostics.VariableToStatementAction
import com.willow.androidide.ultra.lsp.java.actions.generators.GenerateConstructorAction
import com.willow.androidide.ultra.lsp.java.actions.generators.GenerateMissingConstructorAction
import com.willow.androidide.ultra.lsp.java.actions.generators.GenerateSettersAndGettersAction
import com.willow.androidide.ultra.lsp.java.actions.generators.GenerateToStringMethodAction
import com.willow.androidide.ultra.lsp.java.actions.generators.OverrideSuperclassMethodsAction

/**
 * Java code actions.
 * @author Akash Yadav
 */
object JavaCodeActionsMenu : IActionsMenuProvider {

  override val actions: List<ActionItem> =
    listOf(
      CommentAction(),
      UncommentAction(),
      GoToDefinitionAction(),
      FindReferencesAction(),
      AddImportAction(),
      AutoFixImportsAction(),
      ImplementAbstractMethodsAction(),
      VariableToStatementAction(),
      FieldToBlockAction(),
      RemoveClassAction(),
      RemoveMethodAction(),
      RemoveUnusedThrowsAction(),
      CreateMissingMethodAction(),
      SuppressUncheckedWarningAction(),
      AddThrowsAction(),
      GenerateSettersAndGettersAction(),
      OverrideSuperclassMethodsAction(),
      GenerateMissingConstructorAction(),
      GenerateConstructorAction(),
      GenerateToStringMethodAction(),
      RemoveUnusedImportsAction(),
      OrganizeImportsAction()
    )
}
