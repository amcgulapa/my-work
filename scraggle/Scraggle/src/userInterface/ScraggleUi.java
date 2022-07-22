/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package userInterface;


import game.Game;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ArrayList;

/**
 *
 * @author G
 */
public class ScraggleUi {
    
    // menu
    private JFrame frame;
    private JMenuBar menuBar;
    private JMenu gameMenu;
    private JMenuItem newGame;
    private JMenuItem exit;
    
    // player status
    private JPanel currentWordBoard;
    private JButton submitButton;
    private JLabel currentWordLabel;
    private JLabel playerScoreLabel;
    private int score;
    
    //scraggle board
    private JPanel scraggleBoard;
    private JButton[][] diceButtons;
    
    //game status
    private JPanel gameStatusBoard;
    private JTextArea userInputWords;
    private JScrollPane scrollPane;
    private JButton diceShaker;
    private JLabel time;
    
    private Timer timer;
    private int minutes;
    private int seconds;
    
    private Game game;
    private ArrayList<String> foundWords = new ArrayList<String>();
    private ResetGameListener resetGameListener;
    private TileListener tileListener;
    private LetterListener letterListener;
    
    //used for the nested for-loop to fill out the 4x4 scraggle board
    private final int GRID = 4;
    
    public ScraggleUi(Game inGame)
    {
        game = inGame;
        
        initObjects();
        initComponents();
    }
    
    private void initComponents()
    {
        //timer
        theTimer();

        //method for creating menu bar
        theMenu();
        
        //method for current panel (for current word, submitting word, and score count)
        theCurrentPanel();
        
        //method to make the scraggle playing board
        theScraggleBoard();
        
        //method to make the right part of the game (words found, time left, dice shake)
        theWordsTimeDice();
        
        //sets frame to visible so everything can be seen in the gui
      
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private void initObjects()
    {
        resetGameListener = new ResetGameListener();
        tileListener = new TileListener();
        letterListener = new LetterListener();
    }
    
    private void theTimer()
    {
        timer = new Timer(1000, new TimerListener());
        minutes = 3;
        seconds = 0;
        timer.start();
    }
    
    private void theMenu()
    {
        //frame component
        frame = new JFrame("Le Scraggle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(700, 500));
        
        //menu bar component
        menuBar = new JMenuBar();
        
        gameMenu = new JMenu("Scraggle");
        gameMenu.setMnemonic('S');
        newGame = new JMenuItem("New Game");
        newGame.addActionListener(new ResetGameListener());

        exit = new JMenuItem("Exit");
        exit.addActionListener(new ExitListener());
        
        gameMenu.add(newGame);
        gameMenu.add(exit);
        menuBar.add(gameMenu);
        
        frame.setJMenuBar(menuBar);
    }
    
    private void theCurrentPanel()
    {
        //current word component
        currentWordBoard = new JPanel();
        currentWordBoard.setLayout(new FlowLayout());
        currentWordLabel = new JLabel();
        currentWordLabel.setBorder(BorderFactory.createTitledBorder("Current Word"));
        currentWordLabel.setPreferredSize(new Dimension(320, 50));
        
        //submit button component
        submitButton = new JButton("Submit Word");
        submitButton.setPreferredSize(new Dimension(225, 50));
        submitButton.addActionListener(new SubmitListener());
        
        //player score component
        playerScoreLabel = new JLabel();
        playerScoreLabel.setBorder(BorderFactory.createTitledBorder("Score"));
        playerScoreLabel.setPreferredSize(new Dimension(105, 50));
        score = 0;
        playerScoreLabel.setText(String.valueOf(score));
        
        currentWordBoard.setBorder(BorderFactory.createTitledBorder("Current Word"));
        currentWordBoard.add(currentWordLabel, BorderLayout.WEST);
        currentWordBoard.add(submitButton, BorderLayout.CENTER);
        currentWordBoard.add(playerScoreLabel, BorderLayout.EAST);
  
        frame.add(currentWordBoard, BorderLayout.SOUTH);
    }
    
    private void theScraggleBoard()
    {
        scraggleBoard = new JPanel();
        scraggleBoard.setLayout(new GridLayout(4,4));
        scraggleBoard.setPreferredSize(new Dimension(400,400));
        scraggleBoard.setMinimumSize(new Dimension(400,400));
        scraggleBoard.setBorder(BorderFactory.createTitledBorder("Scraggle Board"));
       
        //fills out the 4x4 grid for scraggle board
        diceButtons = new JButton[GRID][GRID];
        
        for(int row = 0; row < GRID; row++)
        {
            for(int col = 0; col <GRID; col++)
            {
                //path to img icons
                URL imgURL = getClass().getResource(game.getGrid()[row][col].getImgPath());
                ImageIcon icon = new ImageIcon(new ImageIcon(imgURL).getImage().getScaledInstance(90, 80, java.awt.Image.SCALE_SMOOTH));
                
                diceButtons[row][col] = new JButton(icon);
                diceButtons[row][col].putClientProperty("row", row);
                diceButtons[row][col].putClientProperty("col", col);
                diceButtons[row][col].putClientProperty("letter", game.getGrid()[row][col].getLetter());
                
                //attaches action listener to the letter buttons
                diceButtons[row][col].addActionListener(new TileListener());
                diceButtons[row][col].addActionListener(new LetterListener());
                
                scraggleBoard.add(diceButtons[row][col]);
            }
        }
            
        
        frame.add(scraggleBoard, BorderLayout.WEST);
    }
    
    private void theWordsTimeDice()
    {
        gameStatusBoard = new JPanel();
        gameStatusBoard.setLayout(new BoxLayout(gameStatusBoard, BoxLayout.Y_AXIS));
        gameStatusBoard.setPreferredSize(new Dimension(280,400));
        gameStatusBoard.setMinimumSize(new Dimension(280,400));
        gameStatusBoard.setBorder(BorderFactory.createTitledBorder("Enter Words Found"));
        
        //make textarea to put into the scrollpane
        userInputWords = new JTextArea();
        userInputWords.setEditable(false);
        scrollPane = new JScrollPane(userInputWords, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        //time component
        time = new JLabel("3:00");
        ////////////time.setAlignmentX(0f); 0f = align left, 0.5 = align center, 1.0 = align right; for BoxLayout only
        time.setHorizontalAlignment(SwingConstants.CENTER);
        time.setFont(new Font("Courier New", Font.BOLD, 50));
        time.setPreferredSize(new Dimension(280,100));
        time.setMinimumSize(new Dimension(280,100));
        time.setMaximumSize(new Dimension(280,100));
        time.setForeground(Color.gray);
        time.setBorder(BorderFactory.createTitledBorder("Time Remaining"));
        
        //dice shaker button component
        diceShaker = new JButton("Shake the Dice!");
        diceShaker.setPreferredSize(new Dimension(280, 100));
        diceShaker.setMaximumSize(new Dimension(250,100));
        diceShaker.addActionListener(new ResetGameListener());
        
        
        //add everything to gameStatusBoard panel
        gameStatusBoard.add(scrollPane);
        gameStatusBoard.add(time);
        gameStatusBoard.add(diceShaker);
   
        //then add to the main frame
        frame.add(gameStatusBoard, BorderLayout.EAST);
    }
    
    private void gameRestart()
    {
            score = 0;
            game.populateGrid();
            
            //remove panel scraggleBoard from the frame
            frame.remove(scraggleBoard);
            
            //remove all components of scraggleBoard
            scraggleBoard.removeAll();
            //then add everything back in again
            theScraggleBoard();
            scraggleBoard.revalidate();
            scraggleBoard.repaint();
            
            //add back the scraggleBaord to the panel again
            frame.add(scraggleBoard, BorderLayout.WEST);
            
            //empty out userinputwords textbox
            userInputWords.setText("");
            
            //empty out user score
            playerScoreLabel.setText("0");
            
            //empty out current word
            currentWordLabel.setText("");
            
            //reset time
            time.setText("3:00");
            
            //clear foundwords array
            foundWords.removeAll(foundWords);
            
            //reset timer
            timer.stop();
            minutes = 3;
            seconds = 0;
            timer.start();
    }
    
    //inner classes for event handlers
    private class ExitListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent ae) 
        {
            int response = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit Scraggle?", "Exit", JOptionPane.YES_NO_OPTION);
            
            if(response == JOptionPane.YES_OPTION)
            {
                //if yes, game will exit
                System.exit(0);
            }
        }
    }
    
    private class ResetGameListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent b) 
        {
            gameRestart();
        }
    }
    
    private class SubmitListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent c) 
        {
            int wordScore = game.getDictionary().search(currentWordLabel.getText().toLowerCase());
            
            if(foundWords.contains(currentWordLabel.getText().toLowerCase()))
            {
                JOptionPane.showMessageDialog(frame, "Word already found!");
            } else {
                if(wordScore>0) //if user submits a valid word
                {
                    //puts word in the "words found" text area
                    userInputWords.setText(userInputWords.getText() + "\n" + currentWordLabel.getText());
                    userInputWords.setCaretPosition(userInputWords.getDocument().getLength());
                    
                    foundWords.add(currentWordLabel.getText().toLowerCase());
                    score += wordScore;
                    playerScoreLabel.setText(String.valueOf(score));
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid word!");
                }
            }
            
            currentWordLabel.setText("");
            
            //enable all of the letter buttons again
            for(int row = 0; row < GRID; row++)
            {
                for(int col = 0; col <GRID; col++)
                {
                    diceButtons[row][col].setEnabled(true);
                }
            }
        }
    }
    
    private class LetterListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent d) 
        {
            if(d.getSource() instanceof JButton)
            {
                JButton button = (JButton)d.getSource();
                String letter = (String)button.getClientProperty("letter");
                currentWordLabel.setText(currentWordLabel.getText() + letter);
            }
        }
    }
    
    private class TileListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            if(e.getSource() instanceof JButton)
            {
                JButton button = (JButton)e.getSource();
                
                int row = (int)button.getClientProperty("row");
                int col = (int)button.getClientProperty("col");
                
                //disables all buttons
                for(int i=0; i<4; i++)
                {
                    for(int j=0; j<4; j++)
                    {
                        diceButtons[i][j].setEnabled(false);
                    }
                }
                
                //the following blocks of codes dictates which button will be eneabled based on their location on the grid
                if(row == 0 && col == 0)
                {
                    diceButtons[row+1][col].setEnabled(true); //down
                    diceButtons[row+1][col+1].setEnabled(true); //down-right
                    diceButtons[row][col+1].setEnabled(true); //right
                }
                
                if(row == 0 && (col == 1 || col == 2))
                {
                    diceButtons[row+1][col].setEnabled(true); //down
                    diceButtons[row+1][col+1].setEnabled(true); //down-right
                    diceButtons[row][col+1].setEnabled(true); //right
                    
                    diceButtons[row][col-1].setEnabled(true); //left
                    diceButtons[row+1][col-1].setEnabled(true); //down-left
                }
                
                if(row == 0 && col == 3)
                {
                    diceButtons[row+1][col].setEnabled(true); //down
                    diceButtons[row][col-1].setEnabled(true); //left
                    diceButtons[row+1][col-1].setEnabled(true); //down-left
                }
                
                if((row == 1 || row == 2) && col == 0)
                {
                    diceButtons[row-1][col].setEnabled(true); //up
                    diceButtons[row-1][col+1].setEnabled(true); //up-right
                    diceButtons[row][col+1].setEnabled(true); //right
                    diceButtons[row+1][col+1].setEnabled(true); //down-right
                    diceButtons[row+1][col].setEnabled(true); //down
                }
                
                if((row == 1 || row == 2) && (col == 1 || col == 2))
                {
                    diceButtons[row-1][col].setEnabled(true); //up
                    diceButtons[row-1][col+1].setEnabled(true); //up-right
                    diceButtons[row][col+1].setEnabled(true); //right
                    diceButtons[row+1][col+1].setEnabled(true); //down-right
                    diceButtons[row+1][col].setEnabled(true); //down
                    diceButtons[row+1][col-1].setEnabled(true); //down-left
                    diceButtons[row][col-1].setEnabled(true); //left
                    diceButtons[row-1][col-1].setEnabled(true); //up-left
                }
                
                if((row == 1 || row == 2) && col == 3)
                {
                    diceButtons[row-1][col].setEnabled(true); //up
                    diceButtons[row-1][col-1].setEnabled(true); //up-left
                    diceButtons[row][col-1].setEnabled(true); //left
                    diceButtons[row+1][col-1].setEnabled(true); //down-left\
                    diceButtons[row+1][col].setEnabled(true); //down
                }
                
                if(row == 3 && col == 0)
                {
                    diceButtons[row-1][col].setEnabled(true); //up
                    diceButtons[row-1][col+1].setEnabled(true); //up-right
                    diceButtons[row][col+1].setEnabled(true); //right
                }
                
                if(row == 3 && (col == 1 || col == 2))
                {
                    diceButtons[row][col-1].setEnabled(true); //left
                    diceButtons[row-1][col-1].setEnabled(true); //up-left
                    diceButtons[row-1][col].setEnabled(true); //up
                    diceButtons[row-1][col+1].setEnabled(true); //up-right
                    diceButtons[row][col+1].setEnabled(true); //right
                }
                
                if(row == 3 && col == 3)
                {
                    diceButtons[row-1][col].setEnabled(true); //up
                    diceButtons[row-1][col-1].setEnabled(true); //up-left
                    diceButtons[row][col-1].setEnabled(true); //left
                }

                
            }
        }
    }
    
    private class TimerListener implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) 
        {
            if(seconds == 0 && minutes == 0)
            {              
                timer.stop();
                
                
                JOptionPane.showMessageDialog(frame,"Time's up!");  
                int response = JOptionPane.showConfirmDialog(frame, "Would you like to play again?", "", JOptionPane.YES_NO_OPTION);
            
                if(response == JOptionPane.YES_OPTION)
                {
                    //if yes, game will restart
                    gameRestart();
                    
                } else{
                    //if no, game will exit
                    System.exit(0);
                }
            }
            else
            {
                if(seconds == 0)
                {
                    seconds = 59;
                    minutes--;
                }
                else
                {
                    seconds--;
                }
            }

            if(seconds < 10)
            {
                String strSeconds = "0" + String.valueOf(seconds);
                time.setText(String.valueOf(minutes) + ":" + strSeconds);
            }
            else
            {
                time.setText(String.valueOf(minutes) + ":" + String.valueOf(seconds));
            }
        }
    }
}
