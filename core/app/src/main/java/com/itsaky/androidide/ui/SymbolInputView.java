/*
 * This file is part of AndroidIDE Ultra.
 *
 * AndroidIDE Ultra is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AndroidIDE Ultra is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE Ultra.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package com.willow.androidide.ultra.ui;

import android.content.Context;
import android.util.AttributeSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.willow.androidide.ultra.adapters.SymbolInputAdapter;
import com.willow.androidide.ultra.editor.ui.IDEEditor;
import com.willow.androidide.ultra.models.Symbol;
import com.willow.androidide.ultra.utils.Symbols;
import java.util.List;

public class SymbolInputView extends RecyclerView {

  public SymbolInputView(Context context) {
    this(context, null);
  }

  public SymbolInputView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public SymbolInputView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
  }

  public void refresh(IDEEditor editor, List<Symbol> symbols) {
    if (symbols == null || symbols.isEmpty()) {
      symbols = Symbols.INSTANCE.getPlainTextSymbols();
    }

    final var adapter = getAdapter();
    if (adapter instanceof SymbolInputAdapter) {
      ((SymbolInputAdapter) adapter).refresh(editor, symbols);
    } else {
      setAdapter(new SymbolInputAdapter(editor));
    }
  }
}
