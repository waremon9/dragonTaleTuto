/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameState;

import Entity.Enemies.*;
import Entity.Enemy;
import Entity.Explosion;
import Entity.HUD;
import Entity.Player;
import Main.GamePanel;
import TileMap.*;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import Audio.AudioPlayer;
import Entity.Damage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;

/**
 *
 * @author verhi
 */
public class LevelState extends GameState {
    
    private TileMap tileMap;
    private String bgString;
    private Background bg;
    
    private Player player;
    private boolean playerStartedDying;
    private boolean playerFinishedDying;
    
    private ArrayList<Enemy> enemies;
    private ArrayList<Explosion> explosions;
    private ArrayList<Damage> damages;
    
    private HUD hud;
    
    private String bgMusicString;
    private AudioPlayer bgMusic;
    
    private static File savePlayer = new File(System.getenv("HOME"), "save/.Player_save.txt");
    
    public LevelState(GameStateManager gsm){
        this.gsm = gsm;
        init();
    }
    
    private String currentLevel;
    
    //implement abstract function
    public void init(){
        
        currentLevel = "/res/Maps/TEST.map";
        
        tileMap = new TileMap(30); //size of tile in param
        tileMap.loadFullMap(currentLevel);
        tileMap.setPosition(0, 0);
        
        
        //load the player
        try {
            java.util.List<String> infoPlayer=Files.readAllLines(savePlayer.toPath());
            for (String infos:infoPlayer){
                String[] infoPlayer2 = infos.split(":");
                String pseudo = infoPlayer2[0];
                int maxHealth = Integer.parseInt(infoPlayer2[1]);
                int maxFire = Integer.parseInt(infoPlayer2[2]);
                int xp = Integer.parseInt(infoPlayer2[3]);
                player = new Player(tileMap, pseudo, maxHealth, maxFire, xp);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        
        player.setPosition(100, 150);//starting position
        playerStartedDying = false;
        playerFinishedDying = false;
        
        populateEnemyAndBgMusic();
                
        explosions = new ArrayList<Explosion>();
        damages = new ArrayList<Damage>();
        
        hud = new HUD(player);
        
        bg = new Background(bgString, 0.1);
        
        bgMusic = new AudioPlayer(bgMusicString);
        //bgMusic.playLoop();
    }
    
    private void populateEnemyAndBgMusic(){ //get background and music here to not open the file even more later
        
        enemies = new ArrayList<Enemy>();
        
        try{
            
            InputStream in = getClass().getResourceAsStream(currentLevel);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            
            br.readLine();
            bgString = br.readLine();
            bgMusicString = br.readLine();
            br.readLine();
            int numRows = Integer.parseInt(br.readLine());
            for (int i = 0; i < numRows; i++) {//skip unwanted line
                br.readLine();
            }
            
            int qteDiffEnemy = Integer.parseInt(br.readLine());
            
            for (int i = 0; i < qteDiffEnemy; i++) {
                String  nameEnemy = br.readLine();
                int qteEnemy = Integer.parseInt(br.readLine());
                
                //delimiters
                String delims = "\\s+";
                String line = br.readLine();
                String[] tokens = line.split(delims);
                
                for(int j = 0; j < qteEnemy*2;j+=2){
                    int x = Integer.parseInt(tokens[j]);
                    int y = Integer.parseInt(tokens[j+1]);
                    
                    switch(nameEnemy) {
                        case "Slugger":
                            Slugger su = new Slugger(tileMap);
                            su.setPosition(x,y);
                            enemies.add(su);
                            break;
                        case "Slogger":
                            Slogger so = new Slogger(tileMap);
                            so.setPosition(x,y);
                            enemies.add(so);
                            break;
                        case "GiantSlugger":
                            GiantSlugger gs = new GiantSlugger(tileMap);
                            gs.setPosition(x,y);
                            enemies.add(gs);
                            break;
                        case "Arachnik":
                            Arachnik a = new Arachnik(tileMap);
                            a.setPosition(x,y);
                            enemies.add(a);
                            break;
                        case "Goomba":
                            Goomba g = new Goomba(tileMap);
                            g.setPosition(x,y);
                            enemies.add(g);
                            break;
                        case "FloatingFucker":
                            FloatingFucker f = new FloatingFucker(tileMap);
                            f.setPosition(x,y);
                            enemies.add(f);
                            break;
                        default:
                            break;
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Player getPlayer(){return player;}
    
    public void update(){
    
        //update player
        player.update();
        tileMap.setPosition(GamePanel.WIDTH/2 - player.getx(), GamePanel.HEIGHT/2 - player.gety());
        if(player.isDead() && !playerStartedDying) {
            gsm.saveProgress(player);
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
            if(e.isDead() || e.gety()>tileMap.height-14){
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
