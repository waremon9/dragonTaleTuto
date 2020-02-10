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
import java.util.Random;
import javax.imageio.ImageIO;

/**
 *
 * @author verhi
 */
public class Slogger extends Enemy{
    
    private BufferedImage[] sprites;
 
    public Slogger(TileMap tm) {
        
        super(tm);
        
        moveSpeed = 0.02;
        maxSpeed = 0.4;
        
        fallSpeed = 0.2;
        maxFallSpeed = 10.0;
        
        canBeBacking = true;
        
        width = 30;
        height = 37;
        cwidth = 25;
        cheight = 18;
        
        health = maxHealth = 12;
        damage = 1;
        
        xp = 15;
        
        //load sprites
        try{
            
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/res/Sprites/Enemies/Slogger.gif"));
            
            sprites = new BufferedImage[3];
            for(int i = 0; i<sprites.length; i++){
                sprites[i] = spritesheet.getSubimage(i*width, 0, width, height);
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(200);
        
        Random rd = new Random();
        facingRight = right = rd.nextBoolean();
        left = !right;
        
    }
    
    private void getNextPosition(){
        
        //do not fall of ledge
        isAboutToFall();
        
        //movement
        positionMoveLeftRight();
        
        //knockback
        positionKnockback(2.5);
        
        //falling
        positionFalling();
        
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
        
        //colision wall
        collisionLeftRight();
        
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
