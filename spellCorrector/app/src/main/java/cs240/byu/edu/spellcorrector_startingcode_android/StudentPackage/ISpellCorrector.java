package cs240.byu.edu.spellcorrector_startingcode_android.StudentPackage;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by blaine on 3/4/16.
 */


public interface ISpellCorrector {

    @SuppressWarnings("serial")
    public static class NoSimilarWordFoundException extends Exception {
    }

    /**
     * Tells this <code>ISpellCorrector</code> to use the given file as its dictionary
     * for generating suggestions.
     * @param dictionaryFile File containing the words to be used
     * @throws IOException If the file cannot be read
     */
    public void useDictionary(InputStreamReader dictionaryFile) throws IOException;

    /**
     * Suggest a word from the dictionary that most closely matches
     * <code>inputWord</code>
     * @param inputWord
     * @return The suggestion
     * @throws NoSimilarWordFoundException If no similar word is in the dictionary
     */
    public String suggestSimilarWord(String inputWord) throws NoSimilarWordFoundException;

}
