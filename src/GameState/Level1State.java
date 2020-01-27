/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameState;

import Entity.Enemies.Slugger;
import Entity.Enemy;
import Entity.HUD;
import Entity.Player;
import Main.GamePanel;
import TileMap.*;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 *
 * @author verhi
 */
public class Level1State extends GameState {
    
    private TileMap tileMap;
    private Background bg;
    
    private Player player;
    
    private ArrayList<Enemy> enemies;
    
    private HUD hud;
    
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
        
        player = new Player(tileMap);
        player.setPosition(100, 100);//starting position
        
        enemies = new ArrayList<Enemy>();
        Slugger s;
        s = new Slugger(tileMap);
        s.setPosition(200, 100);
        enemies.add(s);
        
        hud = new HUD(player);
        
    }
    public void update(){
    
        //update player
        player.update();
        tileMap.setPosition(GamePanel.WIDTH/2 - player.getx(), GamePanel.HEIGHT/2 - player.gety());
        
        //background scrolling
        bg.setPosition(tileMap.getX(), tileMap.getY());
        
        //update all enemies
        for(int i= 0; i < enemies.size(); i++){
            enemies.get(i).update();
        }
        
    }
    
    public void draw(Graphics2D g){
        
        //draw bg (it clear the menu screen)
        bg.draw(g);
        
        //draw tileMap
        tileMap.draw(g);
        
        //draw player
        player.draw(g);
        
        //draw ennemis
        for(int i = 0; i < enemies.size(); i++){
            enemies.get(i).draw(g);
        }
        
        //draw hud
        hud.draw(g);
        
    }
    
    public void keyPressed(int k){
        if(k==KeyEvent.VK_LEFT) player.setLeft(true);
        if(k==KeyEvent.VK_RIGHT) player.setRight(true);
        if(k==KeyEvent.VK_UP) player.setUp(true);
        if(k==KeyEvent.VK_DOWN) player.setDown(true);
        if(k==KeyEvent.VK_SPACE) player.setJumping(true);
        if(k==KeyEvent.VK_D) player.setGliding(true);
        if(k==KeyEvent.VK_F) player.setScratching();
        if(k==KeyEvent.VK_G) player.setFiring();
    }
    
    public void keyReleased(int k){
        if(k==KeyEvent.VK_LEFT) player.setLeft(false);
        if(k==KeyEvent.VK_RIGHT) player.setRight(false);
        if(k==KeyEvent.VK_UP) player.setUp(false);
        if(k==KeyEvent.VK_DOWN) player.setDown(false);
        if(k==KeyEvent.VK_SPACE) player.setJumping(false);
        if(k==KeyEvent.VK_D) player.setGliding(false);
    }

}
