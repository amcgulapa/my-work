/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.net.URL;
import java.io.File;

/**
 *
 * @author G
 */
public class Dictionary {
    private static final String WORDS_FILE = "words.txt";
    private final Trie trie;
    
    public Dictionary ()
    {
        Scanner inputFile = null;
        String word;
        
        //putting code inside a try for possibilities of error
        try
        {
            this.trie = new Trie();
            URL url = getClass().getResource(WORDS_FILE);
            File file = new File (url.toURI());
            inputFile = new Scanner(file);
            
            //if there is something wrong with the given inputFile, returns error
            if(inputFile == null)
            {
                throw new IOException("Invalid URL specified");
            }
            
            //while inputFile still has a word NOT loaded to the trie, it will keep looping to the next word until there are no more words to load
            while(inputFile.hasNext())
            {
                word = inputFile.next();
                word = word.trim().toLowerCase();
                trie.insert(word);
            }
            
            System.out.println("The data file has been read and words loaded into trie");
        }
        //handles the exceptions of the code within try codeblock
        catch(IOException | URISyntaxException e)
        {
            System.out.println("Error reading the file or loading the word(s) into trie");
            throw new RuntimeException(e);
        }
    }
    
    //returns the results from using the methods "search" and "prefix" from class Trie
    public int search (String word)
    {
        return this.trie.search(word);
    }
    public boolean prefix(String word)
    {
        return this.trie.prefix(word);
    }
    
}
