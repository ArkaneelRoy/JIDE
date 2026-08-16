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

package com.willow.androidide.ultra.fragments.output;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.willow.androidide.ultra.R;
import com.willow.androidide.ultra.databinding.FragmentNonEditableEditorBinding;
import com.willow.androidide.ultra.editor.ui.IDEEditor;
import com.willow.androidide.ultra.fragments.EmptyStateFragment;
import com.willow.androidide.ultra.syntax.colorschemes.SchemeAndroidIDE;
import com.willow.androidide.ultra.utils.TypefaceUtilsKt;
import io.github.rosemoe.sora.lang.EmptyLanguage;

public abstract class NonEditableEditorFragment extends
    EmptyStateFragment<FragmentNonEditableEditorBinding>
    implements ShareableOutputFragment {

  public NonEditableEditorFragment() {
    super(R.layout.fragment_non_editable_editor, FragmentNonEditableEditorBinding::bind);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    getEmptyStateViewModel().getEmptyMessage().setValue(createEmptyStateMessage());
    final var editor = getBinding().getRoot();
    editor.setEditable(false);
    editor.setDividerWidth(0);
    editor.setEditorLanguage(new EmptyLanguage());
    editor.setWordwrap(false);
    editor.setUndoEnabled(false);
    editor.setTypefaceLineNumber(TypefaceUtilsKt.jetbrainsMono());
    editor.setTypefaceText(TypefaceUtilsKt.jetbrainsMono());
    editor.setTextSize(12);
    editor.setColorScheme(SchemeAndroidIDE.newInstance(requireContext()));
  }

  private CharSequence createEmptyStateMessage() {
    return null;
  }

  @NonNull
  @Override
  public String getContent() {
    final var editor = getEditor();
    if (editor == null) {
      return "";
    }

    return editor.getText().toString();
  }

  @Nullable
  public IDEEditor getEditor() {
    final var binding = get_binding();
    if (binding == null) {
      return null;
    }
    return binding.editor;
  }

  @NonNull
  @Override
  public String getFilename() {
    return "build_output";
  }

  @Override
  public void clearOutput() {
    final var editor = getEditor();
    if (editor == null) {
      return;
    }

    // Editing CodeEditor's content is a synchronized operation
    editor.getText().delete(0, editor.getText().length());
    getEmptyStateViewModel().isEmpty().setValue(true);
  }
}
