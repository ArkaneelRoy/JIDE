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

package com.willow.androidide.ultra.lsp.util;

import com.willow.androidide.ultra.actions.ActionItem;
import com.willow.androidide.ultra.actions.ActionMenu;
import com.willow.androidide.ultra.actions.ActionsRegistry;
import com.willow.androidide.ultra.actions.locations.CodeActionsMenu;
import com.willow.androidide.ultra.lsp.actions.IActionsMenuProvider;
import com.willow.androidide.ultra.utils.ILogger;

/**
 * @author Akash Yadav
 */
public class LSPEditorActions {

  public static void ensureActionsMenuRegistered(IActionsMenuProvider provider) {
    final var registry = ActionsRegistry.getInstance();
    final var action =
        registry.findAction(ActionItem.Location.EDITOR_TEXT_ACTIONS, CodeActionsMenu.ID);

    if (action == null) {
      ILogger.ROOT.error("[LSPEditorActions] Cannot find registered editor actions menu");
      return;
    }

    final var editorActions = (ActionMenu) action;
    for (final var item : provider.getActions()) {
      if (editorActions.findAction(item.getId()) != null) {
        continue;
      }
      editorActions.addAction(item);
    }
  }
}
