/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameState;

import Entity.Player;
import java.io.File;
import java.nio.file.Files;

/**
 *
 * @author verhi
 */
public class GameStateManager {
    
    private GameState[] gameStates;
    protected LevelState savedState;
    private int savedStateNumber;
    private int currentState;
    protected String levelChoice;
    
    public static final int NUMGAMESTATES = 4;
    public static final int MENUSTATE = 0;
    public static final int DEADSTATE = 1;
    public static final int LEVELSTATE = 2;
    public static final int PAUSESTATE = 3;
    
    public GameStateManager(){
        
        gameStates = new GameState[NUMGAMESTATES];
        
        currentState = MENUSTATE;
        loadState(currentState);
        
    }
    
    private void loadState(int state){
        if(state == MENUSTATE){
           gameStates[state] = new MenuState(this);
        }
        if(state == LEVELSTATE){
           gameStates[state] = new LevelState(this);
        }
        if(state == DEADSTATE){
           gameStates[state] = new DeadState(this);
        }
        if(state == PAUSESTATE){
           gameStates[state] = new PauseState(this);
        }
    }
    
    private void unloadState(int state){
        gameStates[state] = null;
    }
    
    public void setLevelChoice(String level){
        levelChoice = "/res/Maps/"+level;
    }
    
    public void setState(int state){
        if(state == PAUSESTATE){
            savedState = (LevelState) gameStates[currentState];
            savedStateNumber = currentState;//if game is paused, save the level state to not reset it
        } 
        unloadState(currentState);
        currentState = state;
        loadState(currentState);
    }
    
    public void unpauseLevelState(){
        unloadState(currentState);
        currentState = savedStateNumber;
        gameStates[currentState] = savedState;
    }
    
    public void update(){
        try{
            gameStates[currentState].update();
        }catch(Exception e){}
    }
    
    public void draw(java.awt.Graphics2D g){
        try{
            gameStates[currentState].draw(g);
        }catch(Exception e){}
    }
    
    public void keyPressed(int k){
        gameStates[currentState].keyPressed(k);
    }
    
    public void keyReleased(int k){
        gameStates[currentState].keyReleased(k);
    }
    
    public void saveProgress(Player player){
        String save;
        try {
            File savePlayer = new File(System.getenv("HOME"), "save/.Player_save.txt");
            save = player.getPseudo()+":"+player.getMaxHealth()+":"+player.getMaxFire()+":"+player.getXp();
            Files.write(savePlayer.toPath(), save.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
