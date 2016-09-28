package cs240.byu.edu.spellcorrector_startingcode_android.StudentPackage;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.TreeSet;
import java.util.Set;


/**
 * Created by TyUdy on 9/13/16.
 */
public class MySpellCorrector implements ISpellCorrector {


    private Trie dicitonaryTrie;

    public MySpellCorrector(){
        dicitonaryTrie = new Trie();
    }
    /**
     * Tells this <code>ISpellCorrector</code> to use the given file as its dictionary
     * for generating suggestions.
     * @param dictionaryFile File containing the words to be used
     * @throws IOException If the file cannot be read
     */
    public void useDictionary(InputStreamReader dictionaryFile) throws IOException{
        BufferedReader dictionaryReader = new BufferedReader(dictionaryFile);

        String w = dictionaryReader.readLine();
        w = w.toLowerCase();

        while(w != null) {
            dicitonaryTrie.add(w);
            w = dictionaryReader.readLine();
        }
    }

    /**
     * Suggest a word from the dictionary that most closely matches
     * <code>inputWord</code>
     * @param inputWord
     * @return The suggestion
     * @throws NoSimilarWordFoundException If no similar word is in the dictionary
     */
    public String suggestSimilarWord(String inputWord) throws NoSimilarWordFoundException{
        Set<String> editedSet = new TreeSet<>();
        inputWord = inputWord.toLowerCase();
        if(dicitonaryTrie.find(inputWord) != null){
            return inputWord;
        } else {
            //if there is a similar word
            deletionStrings(inputWord, editedSet);
            transportationStrings(inputWord, editedSet);
            alterationStrings(inputWord, editedSet);
            insertionStrings(inputWord, editedSet);
            String suggestedWord = suggestWordInSet(editedSet);

            if(suggestedWord == null){
                //get set with distance of 2
                Set<String> newEditedSet = getDeletionDistanceOf2Set(editedSet);

                //run suggestWordInSet on that set
                suggestedWord = suggestWordInSet(newEditedSet);
                //if that is null
                if(suggestedWord == null){
                    throw new NoSimilarWordFoundException();
                }
            }
            return suggestedWord;

            //look through array to find word to suggest


            //if no similar word throw NoSimilarWordFoundException
        }
    }

    public String suggestWordInSet(Set<String> set){
        String returnString = null;
        int highestFrequency = 0;

        for(String s: set){
            Trie.Node n = dicitonaryTrie.find(s);
            if (n != null){ //this mean the word with distance 1 is in the dicitonary
                if(n.getValue() > highestFrequency) {
                    highestFrequency = n.getValue();
                    returnString = s;
                }
            }

        }
        return returnString;
    }

    public Set<String> getDeletionDistanceOf2Set(Set<String> mySet){
        Set<String> someSet = new TreeSet<>(mySet);
        for(String s: mySet){
            deletionStrings(s, someSet);
            transportationStrings(s, someSet);
            alterationStrings(s, someSet);
            insertionStrings(s, someSet);
        }
        return someSet;
    }

    /**
     *
     * @param s - string of which we are searching for for substrings that have a deletion distance of one
     * @return an array of strings that have a deletion distance of 1 from s
     */
    public void deletionStrings(String s, Set<String> mySet){
        //String[] newStrings = new String[s.length()]; //strings that are a deletion distance of 1 from s;
        for (int i = 0; i < s.length(); i++){
            StringBuilder sb = new StringBuilder(s);
            sb.deleteCharAt(i);
            mySet.add(sb.toString());
            //newStrings[i] = new String(sb);
        }
        //return newStrings;
    }

    /**
     *
     * @param s - string of which we are searching for for substrings that have a transportation distance of one
     * @return an array of strings that have a transportation distance of 1 from s
     */
    public void transportationStrings(String s, Set<String> mySet){
        //String[] newStirngs = new String[s.length()-1];
        for(int i = 0; i < s.length() - 1; i++){
            StringBuilder sb = new StringBuilder(s);
            char a = sb.charAt(i);
            char b = sb.charAt(i+1);
            sb.setCharAt(i, b);
            sb.setCharAt(i+1, a);
            //newStirngs[i] = new String(sb);
            mySet.add(sb.toString());
        }
       // return newStirngs;
    }

    public void alterationStrings(String s, Set<String> mySet){
        //String[] newStrings = new String[s.length() * 25];
        for(int i = 0; i < s.length(); i++){
            char oldChar = s.charAt(i);
            for(int j = 0; j < 26; j++) {
                StringBuilder sb = new StringBuilder(s);
                char newChar = (char)('a' + j);

                if(newChar == oldChar){
                    continue;
                }
                sb.setCharAt(i, newChar);
                //newStrings[((i*25) + j)] = new String(sb);
                mySet.add(sb.toString());
                // If newChar doesnt equal original Char
                // Create the new string and add it to the array
            }
        }
    }

    public void insertionStrings(String s, Set<String> mySet){
        //String[] newStrings = new String[26*(s.length()+1)];
        for(int i = 0; i < s.length()+1; i++){
            for(char j = 'a'; j <= 'z'; j++){
                StringBuilder sb = new StringBuilder(s);
                sb.insert(i, j);
                mySet.add(sb.toString());
            }
        }
    }

}
