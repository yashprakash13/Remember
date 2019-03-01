package tech.pcreate.remember.database;


import java.io.Serializable;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "word_db")
public class Word implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    @Nullable
    public String meaning;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Word( String word, @Nullable String meaning) {
        this.name = word;
        this.meaning = meaning;
    }

    public Word() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Nullable
    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(@Nullable String meaning) {
        this.meaning = meaning;
    }
}
