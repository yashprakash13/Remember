package tech.pcreate.remember.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Word.class}, version = 1, exportSchema = false)
public abstract class WordDatabase extends RoomDatabase {

        public abstract WordDao wordDao();

        private static WordDatabase INSTANCE;

        public static WordDatabase getDatabase(final Context context){
            if (INSTANCE == null){

                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WordDatabase.class, "word_db")
                            .build();
            }
            return INSTANCE;

        }

}
