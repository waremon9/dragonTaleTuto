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
import Entity.Damage;
import Entity.Enemies.Arachnik;
import Entity.Enemies.Slogger;

/**
 *
 * @author verhi
 */
public class Level1State extends GameState {
    
    private TileMap tileMap;
    private Background bg;
    
    private Player player;
    private boolean playerStartedDying;
    private boolean playerFinishedDying;
    
    private ArrayList<Enemy> enemies;
    private ArrayList<Explosion> explosions;
    private ArrayList<Damage> damages;
    
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
        playerStartedDying = false;
        playerFinishedDying = false;
        
        populateEnemies();
                
        explosions = new ArrayList<Explosion>();
        damages = new ArrayList<Damage>();
        
        hud = new HUD(player);
        
        bgMusic = new AudioPlayer("/res/Music/level1-1.mp3");
        //bgMusic.playLoop();
    }
    
    private void populateEnemies(){
        
        enemies = new ArrayList<Enemy>();
        
        Slugger su;
        Point[] pointsSu = new Point[]{
            //new Point(400, 108),
            new Point(860, 198),
            new Point(1525, 198),
            new Point(1680, 198),
            new Point(1800, 198)
        };
        for (int i = 0; i < pointsSu.length; i++) {
            su = new Slugger(tileMap);
            su.setPosition(pointsSu[i].x, pointsSu[i].y);
            enemies.add(su);
        }
        
        Slogger so;
        Point[] pointsSo = new Point[]{
            new Point(400, 108),
        };
        for (int i = 0; i < pointsSo.length; i++) {
            so = new Slogger(tileMap);
            so.setPosition(pointsSo[i].x, pointsSo[i].y);
            enemies.add(so);
        }
        
        Arachnik a;
        Point[] pointsA = new Point[]{
            new Point(1013, 198),
            new Point(1200, 150),
            new Point(1381, 80),
            new Point(1575, 130),
            new Point(1828, 41),
            new Point(2000, 106),
            new Point(2045, 76),
            new Point(2425, 163),
            new Point(2830, 160)
        };
        for (int i = 0; i < pointsA.length; i++) {
            a = new Arachnik(tileMap);
            a.setPosition(pointsA[i].x, pointsA[i].y);
            enemies.add(a);
        }
        
        
    }
    
    public void update(){
    
        //update player
        player.update();
        tileMap.setPosition(GamePanel.WIDTH/2 - player.getx(), GamePanel.HEIGHT/2 - player.gety());
        if(player.isDead() && !playerStartedDying) {
            playerStartedDying = true;
            explosions.add(new Explosion(player.getx(), player.gety()));
        }
        if(playerFinishedDying) gsm.setState(GameStateManager.DEADSTATE);
        
        //background scrolling
        bg.setPosition(tileMap.getx(), tileMap.gety());
        
        //attack enemies and add the new Damages when hit
        ArrayList<Damage> newDamages = player.checkAttack(enemies, tileMap);
        for (Damage dmg : newDamages) { 
            damages.add(dmg);
        }
        
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
        
        //update damages
        for(int i = 0; i < damages.size(); i++) {
            damages.get(i).update();
            if(damages.get(i).shouldRemove()) {
                    damages.remove(i);
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
        if(explosions.isEmpty() && player.isDead() && playerStartedDying) playerFinishedDying = true;
        
        //draw damages
        for(int i = 0; i < damages.size(); i++){
            damages.get(i).draw(g);
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
        
        if(k==KeyEvent.VK_O) gsm.setState(GameStateManager.PAUSESTATE);
        
        if(k==KeyEvent.VK_P) System.out.println(player.getPosition());;
        
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
