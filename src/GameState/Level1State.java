/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameState;

import Entity.Enemies.Slugger;
import Entity.Enemy;
import Entity.Explosion;
import Entity.HUD;
import Entity.Player;
import Main.GamePanel;
import TileMap.*;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import Audio.AudioPlayer;

/**
 *
 * @author verhi
 */
public class Level1State extends GameState {
    
    private TileMap tileMap;
    private Background bg;
    
    private Player player;
    
    private ArrayList<Enemy> enemies;
    public ArrayList<Explosion> explosions;
    
    private HUD hud;
    
    private AudioPlayer bgMusic;
    
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
        player.setPosition(100, 150);//starting position
        
        populateEnemies();
                
        explosions = new ArrayList<Explosion>();
        
        hud = new HUD(player);
        
        bgMusic = new AudioPlayer("/res/Music/level1-1.mp3");
        //bgMusic.playLoop();
        
    }
    
    private void populateEnemies(){
        
        enemies = new ArrayList<Enemy>();
        
        Slugger s;
        Point[] points = new Point[]{
            new Point(400, 108),
            new Point(860, 198),
            new Point(1525, 198),
            new Point(1680, 198),
            new Point(1800, 198)
        };
        for (int i = 0; i < points.length; i++) {
            s = new Slugger(tileMap);
            s.setPosition(points[i].x, points[i].y);
            enemies.add(s);
        }
        
        
    }
    
    public void update(){
    
        //update player
        player.update();
        tileMap.setPosition(GamePanel.WIDTH/2 - player.getx(), GamePanel.HEIGHT/2 - player.gety());
        if(player.isDead()) {
            gsm.setState(GameStateManager.DEADSTATE);
        }
        
        //background scrolling
        bg.setPosition(tileMap.getx(), tileMap.gety());
        
        //attack enemies
        player.checkAttack(enemies);
        
        //update all enemies
        for(int i= 0; i < enemies.size(); i++){
            Enemy e = enemies.get(i);
            e.update();
            if(e.isDead()){
                enemies.remove(i);
                i--;
                explosions.add(new Explosion(e.getx(), e.gety()));
            }
        }
        
        //update explosions
        for(int i = 0; i < explosions.size(); i++) {
            explosions.get(i).update();
            if(explosions.get(i).shouldRemove()) {
                    explosions.remove(i);
                    i--;
            }
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
        
        //draw explosion
        for(int i = 0; i < explosions.size(); i++){
            explosions.get(i).setMapPosition((int)tileMap.getx(), (int)tileMap.gety());
            explosions.get(i).draw(g);
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
