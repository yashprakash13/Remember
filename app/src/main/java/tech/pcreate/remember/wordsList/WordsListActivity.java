package tech.pcreate.remember.wordsList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import tech.pcreate.remember.R;
import tech.pcreate.remember.database.Word;
import tech.pcreate.remember.exportWords.ExportActivity;
import tech.pcreate.remember.newWord.NewWordDialogFragment;
import tech.pcreate.remember.settings.SettingsActivity;
import tech.pcreate.remember.utils.RecyclerViewDividerItem;

import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class WordsListActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener
                                                                    , WordsListAdapter.WordListClickListener{

    private SharedPreferences sharedPreferences;
    private WordsListViewModel wordsListViewModel;
    private RecyclerView recyclerView;
    private WordsListAdapter wordsListAdapter;
    private boolean doubleClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpViews();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            showWordDialog(null);
        });

        setUpSharedPrefs();
    }

    private void showWordDialog(Bundle bundle) {
        NewWordDialogFragment newWordDialogFragment = new NewWordDialogFragment();
        if(bundle != null) newWordDialogFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            transaction.remove(prev);
        }
        transaction.addToBackStack(null);
        newWordDialogFragment.show(transaction, "dialog");
    }

    private void setUpViews() {
        recyclerView = findViewById(R.id.wordsList);
        wordsListAdapter = new WordsListAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new RecyclerViewDividerItem(this, DividerItemDecoration.VERTICAL, 13));
        recyclerView.setAdapter(wordsListAdapter);

        wordsListViewModel = ViewModelProviders.of(this).get(WordsListViewModel.class);
        wordsListViewModel.getAllWords().observe(this, new Observer<PagedList<Word>>() {
            @Override
            public void onChanged(PagedList<Word> wordPagedList) {
                if(wordPagedList == null || wordPagedList.size() == 0){
                    wordsListViewModel.insertDefaultWords();
                }
                wordsListAdapter.submitList(wordPagedList);
            }
        });
    }


    private void setUpSharedPrefs() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        boolean isEnabled = sharedPreferences.getBoolean(getString(R.string.dark_mode_key), true);
        if(isEnabled) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }else if (id == R.id.export_btn){
            startActivity(new Intent(this, ExportActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.dark_mode_key))){
            if (sharedPreferences.getBoolean(key, false))Toast.makeText(this, R.string.dark_mode_ena_message, Toast.LENGTH_SHORT).show();
            else Toast.makeText(this, R.string.dark_mode_dis_message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    @Override
    public void onClick(int id) throws ExecutionException, InterruptedException {
        Runnable r = () -> doubleClick = false;
        if (doubleClick) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("wordEdit", wordsListViewModel.getWord(id));
            showWordDialog(bundle);
            doubleClick = false;

        }else {
            doubleClick=true;
            Toast.makeText(this, getString(R.string.click_again_to_edit) + wordsListViewModel.getWord(id).getName(), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(r, 2000);
        }
    }

    @Override
    public void onLongClick(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(getString(R.string.delete_this_word));
        builder.setMessage(getString(R.string.are_you_sure));
        builder.setPositiveButton(R.string.ok, (dialogInterface, i) -> {
            try {
                wordsListViewModel.deleteWord(id);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), ((dialogInterface, i) -> { }));

        builder.show();
    }

}
