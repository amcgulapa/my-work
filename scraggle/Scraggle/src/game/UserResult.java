/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.util.LinkedHashMap;
import java.util.Map;
import model.WordResult;

/**
 *
 * @author G
 */
public class UserResult 
{
    int totalScore;
    private final Map<String, WordResult> wordToResultMap = new LinkedHashMap<>();
    
    // getter
    public int getTotalScore()
    {
        return totalScore;
    }
    
    //add word to member var wordToResultMap and updates score by adding word score to the currnet score
    public void add(String word, WordResult result)
    {
        this.wordToResultMap.put(word, result);
        totalScore += result.getScore();
    }
    
    //returns an individual word
    public WordResult get(String word)
    {
        return this.wordToResultMap.get(word);
    }
    
    //returns all words from the list
    public Map<String, WordResult> all()
    {
        return this.wordToResultMap;
    }
    
    //prevents from having a repeated word
    public boolean exists(String word)
    {
        return this.wordToResultMap.containsKey(word);
    }
    
    //returns the word size
    public int getWordCount()
    {
        return this.wordToResultMap.size();
    }
    
}
