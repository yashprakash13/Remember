package tech.pcreate.remember.wordsList;


import android.app.Application;

import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import tech.pcreate.remember.database.Word;
import tech.pcreate.remember.database.WordRepository;
import tech.pcreate.remember.utils.AppConstants;

public class WordsListViewModel extends AndroidViewModel {

    private WordRepository wordRepository;
    private LiveData<PagedList<Word>> pagedListWordLiveData;

    private final static PagedList.Config config
            = new PagedList.Config.Builder()
            .setPageSize(AppConstants.PAGE_SIZE)
            .setInitialLoadSizeHint(AppConstants.PAGE_INITIAL_LOAD_SIZE)
            .setPrefetchDistance(AppConstants.PAGE_PREFETCH_DISTANCE)
            .setEnablePlaceholders(true)
            .build();

    public WordsListViewModel(@NonNull Application application) {
        super(application);
        wordRepository = new WordRepository(application);
    }

    public LiveData<PagedList<Word>> getAllWords(){
        if(pagedListWordLiveData == null){
            pagedListWordLiveData = wordRepository.getAllWords(config);
        }
        return pagedListWordLiveData;
    }

    public void insertDefaultWords(){
        wordRepository.insertDefaultWords();
    }

    public void insertWords(List<Word> wordList){
        wordRepository.insertWords(wordList);
    }

    public void updateWord(Word word){
        wordRepository.updateWord(word);
    }

    public Word getWord(int id) throws ExecutionException, InterruptedException {
        return wordRepository.getWord(id);
    }

    public void insertWord(Word word){
        wordRepository.insertWord(word);
    }

    public void deleteWord(int id) throws ExecutionException, InterruptedException {
        wordRepository.deleteWord(getWord(id));
    }

}
