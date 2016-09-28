package cs240.byu.edu.spellcorrector_startingcode_android.StudentPackage;


import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by TyUdy on 9/13/16.
 */
public class Trie implements ITrie {

    // This is essentially the root node of the tree.
    private Node root;
    private int nodeCount;
    private int wordCount;

    public Trie(){
        root = new Node();
        nodeCount = 1;
        wordCount = 0;
    }

    /**
     * Adds the specified word to the trie (if necessary) and increments the word's frequency count
     *
     * @param word The word being added to the trie
     */
    public void add(String word){
        word = word.toLowerCase();
        root.add(new StringBuilder(word));
    }

    /**
     * Searches the trie for the specified word
     *
     * @param word The word being searched for
     *
     * @return A reference to the trie node that represents the word,
     * 			or null if the word is not in the trie
     */
    public Node find(String word){

        return root.find(new StringBuilder(word));
    }

    /**
     * Returns the number of unique words in the trie
     *
     * @return The number of unique words in the trie
     */
    public int getWordCount(){
        return wordCount;
    }

    /**
     * Returns the number of nodes in the trie
     *
     * @return The number of nodes in the trie
     */
    public int getNodeCount(){
        return nodeCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trie trie = (Trie) o;

        if (nodeCount != trie.nodeCount) return false;
        if (wordCount != trie.wordCount) return false;
        return root != null ? root.equals(trie.root) : trie.root == null;

    }

    @Override
    public int hashCode() {
        return (31*nodeCount*wordCount);
    }

    /**
     * The toString specification is as follows:
     * For each word, in alphabetical order:
     * <word>\n
     */
    @Override
    public String toString(){
        StringBuilder list = new StringBuilder();
        root.toString(list, new StringBuilder());
        return list.toString();

    }

//--------------------------------------------------------------------------------------------------------------------------------
    public class Node implements ITrie.INode {

        private static final int NUMBER_OF_LETTERS_IN_THE_ALPHABET = 26;
        private Node[] nodes;
        private int frequencyCount;

        public Node(){
            nodes = new Node[NUMBER_OF_LETTERS_IN_THE_ALPHABET];
            frequencyCount = 0;
        }

        /**
         * Returns the frequency count for the word represented by the node
         *
         * @return The frequency count for the word represented by the node
         */
        public int getValue(){
            return frequencyCount;
        }

        /**
         * @param word - word to be added
         * add the given word to the node tree recursively.
         * If the word already exists just increment the frequencyCount.
         */
        public void add(StringBuilder word){
            //stop at end of word
            if(word.length() == 0){
                frequencyCount++;
                if (frequencyCount == 1){
                    wordCount++;
                }
                return;
            }
            char firstLetter = word.charAt(0);
            int childIndex = firstLetter - 'a';
            //check if doesnt exists
            if(this.nodes[childIndex] == null){
                this.nodes[childIndex] = new Node();
                nodeCount++;

            } else {
                //if exists do nothing
            }
            //delete first character
            word.deleteCharAt(0);
            //keep adding
            nodes[childIndex].add(word);
        }

        /**
         * @param word - current word that is being found (loses first letter each recursive iteration)
         * @return the botton node (last letter of the word) or null if the word isnt found
         */
        public Node find(StringBuilder word){
            //end of word
            if(word.length() == 0 && frequencyCount > 0){
                return this;
            } else if (word.length() == 0 && frequencyCount == 0){
                return null;
            }
            char c = word.charAt(0);
            int childIndex = c - 'a';

            //check if child exists, return null if it doesnt
            if(nodes[childIndex] == null){
                return null;
            }

            //delete first letter
            word.deleteCharAt(0);
            //recurse
            return nodes[childIndex].find(word);
        }
        /**
         * "It explains itself"
         */
        public boolean equals(Node other){
            if(other == null) return false;
            if(frequencyCount != other.getValue()) return false;

            for(int i = 0; i < nodes.length; i++){
                if(nodes[i] == null && other.nodes[i] != null){
                    return false;
                } else if(nodes[i] != null){
                    //check if recursion returns false
                    if(nodes[i].equals(other.nodes[i]) == false) return false;
                }
            }
            return true;
        }
        /**
         * The toString specification is as follows:
         * For each word, in alphabetical order:
         * <word>\n
         */
        public void toString(StringBuilder list, StringBuilder currentWord){
            if(frequencyCount > 0){
                StringBuilder s = new StringBuilder(currentWord);
                s.append("\n");
                list.append(s.toString());
            }
            // Since all indexs are filled with letters in alphabetical order (if they exist),
            // then this loop will automatically create the list in alphabetical order.
            for(int i = 0; i < nodes.length; i++){
                //if child exists
                if(nodes[i] != null){
                    StringBuilder temp = new StringBuilder(currentWord);
                    char c = (char)('a' + i);
                    temp.append(c);
                    nodes[i].toString(list,temp);
                }
                //else go to next child
            }
        }

    }



}
