package tech.pcreate.remember.database;


import java.util.List;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface WordDao {

    @Query("Select * from word_db order by name asc")
    DataSource.Factory<Integer, Word> getAllWordsPaged();

    @Query("Select * from word_db where id= :id")
    Word getWord(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertWord(Word word);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWords(List<Word> wordList);

    @Update
    void updateWord(Word word);

    @Delete
    void deleteWord(Word word);
}
