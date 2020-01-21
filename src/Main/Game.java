/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import javax.swing.JFrame;

/**
 *
 * @author verhi
 */
public class Game {
    public static void main(String[] args){
        
        //Create the window for the game
        JFrame window = new JFrame("Dragon Tale");
        window.setContentPane(new GamePanel());//contentPane is the game panel
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.pack();
        window.setVisible(true);
    }
}
