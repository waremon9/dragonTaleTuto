/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameState;

import TileMap.*;
import java.awt.Graphics2D;

/**
 *
 * @author verhi
 */
public class Level1State extends GameState {
    
    private TileMap tileMap;
    private Background bg;
    
    public Level1State(GameStateManager gsm){
        this.gsm = gsm;
        init();
    }
    
    //implement abstract function
    public void init(){
        tileMap = new TileMap(30); //size of tile in param
        tileMap.loadTile("/res/Tilesets/grasstileset.gif");
        tileMap.loadMap("/res/Maps/level1-1.map");
        tileMap.setPosition(0, 0);
        
        bg = new Background("/res/Backgrounds/grassbg1.gif", 0.1);
        
    }
    public void update(){}
    
    public void draw(Graphics2D g){
        
        //draw bg (it clear the menu screen)
        bg.draw(g);
        
        //draw tileMap
        tileMap.draw(g);
        
    }
    
    public void keyPressed(int k){}
    
    public void keyReleased(int k){}
   
    
}
