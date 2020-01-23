/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameState;

import java.util.ArrayList;

/**
 *
 * @author verhi
 */
public class GameStateManager {
    
    private ArrayList<GameState> gameStates;
    private int currentState;
    
    public static final int MENUSTATE = 0;
    public static final int LEVEL1STATE = 1;
    
    public GameStateManager(){
        
        gameStates = new ArrayList<GameState>();
        
        currentState = MENUSTATE;
        gameStates.add(new MenuState(this));
        gameStates.add(new Level1State(this));
        
    }
    
    public void setState(int state){
        currentState = state;
        gameStates.get(currentState).init();
    }
    
    public void update(){
        gameStates.get(currentState).update();
    }
    
    public void draw(java.awt.Graphics2D g){
        gameStates.get(currentState).draw(g);
    }
    
    public void keyPressed(int k){
        gameStates.get(currentState).keyPressed(k);
    }
    
    public void keyReleased(int k){
        gameStates.get(currentState).keyReleased(k);
    }
    
}
