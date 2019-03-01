package tech.pcreate.remember.database.defaultWordGenerator;

import java.util.ArrayList;
import java.util.List;

import tech.pcreate.remember.database.Word;

public class DefaultWords {

    public static List<Word> getDefaultWordsList(){
        List<Word> wordList = new ArrayList<>();

        wordList.add(new Word("Equivocal", " Open to many interpretations"));
        wordList.add(new Word("Tractable", " Obedient"));
        wordList.add(new Word("Engender", " To produce, cause, bring out"));
        wordList.add(new Word("Dogma", "Rigidly fixed in opinion"));
        wordList.add(new Word("Garrulous", "Very talkative"));
        wordList.add(new Word("Homogeneous", "Composed of identical parts"));
        wordList.add(new Word("Laconic", "Using a few words"));
        wordList.add(new Word("Quiescence", "Inactivity, stillness"));
        wordList.add(new Word("Venerate", " To respect"));
        wordList.add(new Word("Misanthrope", "Person who hates human beings"));

        return wordList;
    }
}
