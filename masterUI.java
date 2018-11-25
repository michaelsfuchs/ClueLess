/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package masterui;

/**
 *
 * @author Alisha R. Hunt
 */
public class masterUI {
    
    public static void masterUI() {
    
}
    
    public static void main(String args[]){
    // This class primarily manages switching between screens
    System.out.println("You're running from masterUI");
    
    GameStartScreen welcomeScreen = new GameStartScreen();
    GameBoard mainScreen = new GameBoard();
    // define other screens here
    
    welcomeScreen.setVisible(true);
    
    // wait for a few seconds, then switch screens
    System.out.println("reached the mark!");
    
    //welcomeScreen.setVisible(false);
    mainScreen.setVisible(true);
    
    
    }
}
