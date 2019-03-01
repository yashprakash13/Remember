package tech.pcreate.remember.newWord;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import tech.pcreate.remember.R;
import tech.pcreate.remember.database.Word;
import tech.pcreate.remember.wordsList.WordsListActivity;
import tech.pcreate.remember.wordsList.WordsListViewModel;

public class NewWordDialogFragment extends DialogFragment {

    private WordsListViewModel wordsListViewModel;
    private Word editingWord;
    private TextInputLayout mName;
    private TextInputLayout mMeaning;
    private boolean mEditMode = false;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(1250, 950);
        window.setGravity(Gravity.CENTER);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean isDarkThemeEnabled = sharedPreferences.getBoolean(getString(R.string.dark_mode_key), true);
        if (!isDarkThemeEnabled) {
            window.setBackgroundDrawable(getActivity().getDrawable(R.drawable.rounded_rectangle_white));
        }else {
            window.setBackgroundDrawable(getActivity().getDrawable(R.drawable.rounded_rectangle_transparent));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.new_word_dialog_layout, container, false);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        wordsListViewModel = new WordsListViewModel(getActivity().getApplication());

        if (getArguments() != null)
        editingWord = (Word) getArguments().getSerializable("wordEdit");

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mName = view.findViewById(R.id.name);
        mMeaning = view.findViewById(R.id.meaning);

        if (editingWord != null) setParams();

        Button doneBtn = view.findViewById(R.id.done_btn);
        doneBtn.setOnClickListener(view1 -> {
            if (!TextUtils.isEmpty(mName.getEditText().getText())){
                Word word = new Word(mName.getEditText().getText().toString(), mMeaning.getEditText().getText().toString());
                insertOrUpdateWord(word);
            }else mName.setError(getString(R.string.word_empty_error_message));
        });
    }

    private void insertOrUpdateWord(Word word) {
        if(!mEditMode) {
            wordsListViewModel.insertWord(word);
            Toast.makeText(getActivity(), R.string.word_added_message, Toast.LENGTH_SHORT).show();
            dismiss();
        }
        else {
            int id = editingWord.getId();
            word.setId(id);
            wordsListViewModel.updateWord(word);
            startActivity(new Intent(getActivity(), WordsListActivity.class));
            dismiss();
        }
    }

    private void setParams() {
        mName.getEditText().setText(editingWord.getName());
        mMeaning.getEditText().setText(editingWord.getMeaning());
        mEditMode =  true;
    }
}
