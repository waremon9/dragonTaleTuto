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
public class Slugger extends Enemy{
    
    private BufferedImage[] sprites;
 
    public Slugger(TileMap tm) {
        
        super(tm);
        
        moveSpeed = 0.3;
        maxSpeed = 0.3;
        
        fallSpeed = 0.2;
        maxFallSpeed = 10.0;
        
        canBeBacking = true;
        
        width = 30;
        height = 37;
        cwidth = 25;
        cheight = 18;
        
        health = maxHealth = 10;
        damage = 1;
        
        xp = 10;
        
        //load sprites
        try{
            
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/res/Sprites/Enemies/Slugger.gif"));
            
            sprites = new BufferedImage[3];
            for(int i = 0; i<sprites.length; i++){
                sprites[i] = spritesheet.getSubimage(i*width, 0, width, height);
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(300);
        
        right = true;
        facingRight = true;
        
    }
    
    private void getNextPosition(){
        
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
        
        //wall colision        
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
