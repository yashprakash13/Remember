package tech.pcreate.remember.database;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import tech.pcreate.remember.database.defaultWordGenerator.DefaultWords;

public class WordRepository {

    private WordDao wordDao;

    public WordRepository(Application application) {
        WordDatabase wordDatabase = WordDatabase.getDatabase(application);
        wordDao = wordDatabase.wordDao();
    }

    public LiveData<PagedList<Word>> getAllWords(PagedList.Config config){
        DataSource.Factory<Integer, Word> factory = wordDao.getAllWordsPaged();
        return new LivePagedListBuilder<>(factory, config).build();
    }
    public void insertDefaultWords(){
        insertWords(DefaultWords.getDefaultWordsList());
    }

    public void insertWords(List<Word> defaultWordsList) {
        new InsertWordsAsync(wordDao).execute(defaultWordsList);
    }

    public void insertWord(Word word){
        new InsertWordAsync(wordDao).execute(word);
    }

    public void updateWord(Word word){
        AsyncTask.execute(()-> wordDao.updateWord(word));
    }

    public void deleteWord(Word word){
        AsyncTask.execute(()-> wordDao.deleteWord(word));
    }

    public Word getWord(int id) throws ExecutionException, InterruptedException{
        return new GetWordAsync(wordDao).execute(id).get();
    }

    public List<Word> getAllWords() {
        return wordDao.getAllWords();
    }

    private static class GetWordAsync extends AsyncTask<Integer, Void, Word>{
        private WordDao wordDao;

        public GetWordAsync(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Word doInBackground(Integer... integers) {
            return wordDao.getWord(integers[0]);
        }
    }

    private static class InsertWordsAsync extends AsyncTask<List<Word>, Void, Void>{

        private WordDao wordDao;
        public InsertWordsAsync(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(List<Word>... lists) {
            wordDao.insertWords(lists[0]);
            return null;
        }
    }

    private static class InsertWordAsync extends AsyncTask<Word, Void, Void>{
        private WordDao wordDao;

        public InsertWordAsync(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.insertWord(words[0]);
            return null;
        }
    }

}
