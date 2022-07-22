/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import dictionary.Alphabet;
import dictionary.Dictionary;
import model.GridPoint;
import model.GridUnit;


/**
 *
 * @author G
 */
public class Game 
{
    private final GridUnit[][] grid;
    private final Dictionary dictionary;
    
    //store 4x4 array of GridUnit objects
    public Game(Dictionary dictionary)
    {
        this.dictionary = dictionary;
        
        this.grid = new GridUnit[4][4];
        this.populateGrid();
    }
    
    // getter
    public GridUnit[][] getGrid()
    {
        return grid;
    }
    
    //returns grid position based on x and y values
    public GridUnit getGridUnit(GridPoint point)
    {
        return grid[point.x][point.y];
    }
    
    //populates grid with randomized letters
    public void populateGrid()
    {
        for(int row = 0; row < 4; row++)
        {
            for(int col = 0; col < 4; col++)
            {
                grid[row][col] = new GridUnit(Alphabet.newRandom(),new GridPoint(row,col));
            }
        }
    }
    
    public void displayGrid()
    {
        //prints out the grid along with the letters
        System.out.println("*****************");
        for(int row = 0; row < 4; row++)
        {
            
            for(int col = 0; col < 4; col++)
            {
                System.out.print("| " + grid[row][col].getLetter()+ " ");
                
            }
            System.out.print("|");
            System.out.println("");
            System.out.println("*****************");
        }
    }

    /**
     * @return the dictionary
     */
    public Dictionary getDictionary() {
        return dictionary;
    }
    
}
