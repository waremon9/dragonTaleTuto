/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity.Enemies;


import Entity.Animation;
import Entity.Enemy;
import TileMap.TileMap;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
/**
 *
 * @author verhi
 */
public class Arachnik extends Enemy{
    
    private BufferedImage[] sprites;
 
    public Arachnik(TileMap tm) {
        
        super(tm);
        
        moveSpeed = 0.003;
        maxSpeed = 0.5;
        
        width = 30;
        height = 30;
        cwidth = 28;
        cheight = 23;
        
        canBeBacking = false;
        
        health = maxHealth = 14;
        damage = 2;
        
        xp = 23;
        
        //load sprites
        try{
            
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/res/Sprites/Enemies/arachnik.gif"));
            
            sprites = new BufferedImage[1];
            for(int i = 0; i<sprites.length; i++){
                sprites[i] = spritesheet.getSubimage(i*width, 0, width, height);
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(500);
        
        down = true;
        up = false;
        
    }
    
    private void getNextPosition(){
        
        //movement
        positionUpDown();
    }
    
    public void update(){
        
        //update position
        getNextPosition();
        super.update();
        
        //check flinching
        if(flinching){
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if(elapsed > 400) {
                flinching = false;
            }
        }
        
        //wall colision
        collisionUpDown(15);
        
        //update animation
        animation.update();
        
    }
    
    public void draw(Graphics2D g){
        
        setMapPosition();
        
        //flinching
        if(flinching){
            long elapsed = (System.nanoTime()-flinchTimer)/1000000;
            if(elapsed / 100%2 == 0){ //blinking every 100 milliseconde
                return;
            }
        }
        
        super.draw(g);
        
    }
    
}
