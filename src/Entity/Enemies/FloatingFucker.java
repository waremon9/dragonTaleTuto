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
public class FloatingFucker extends Enemy{
    
    private BufferedImage[] sprites;
 
    public FloatingFucker(TileMap tm) {
        
        super(tm);
        
        moveSpeed = 0.5;
        maxSpeed = 0.5;
        
        fallSpeed = 0.2;
        maxFallSpeed = 0;
        
        canBeBacking = false;
        
        width = 28;
        height = 55;
        cwidth = 22;
        cheight = 34;
        
        health = maxHealth = 10;
        damage = 2;
        
        xp = 20;
        
        //load sprites
        try{
            
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/res/Sprites/Enemies/FloatingFucker.gif"));
            
            sprites = new BufferedImage[6];
            for(int i = 0; i<sprites.length; i++){
                sprites[i] = spritesheet.getSubimage(i*width, 0, width, height);
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(60);
        
        Random rd = new Random();
        facingRight = right = rd.nextBoolean();
        left = !right;
        down = rd.nextBoolean();
        up = !down;
        
    }
    
    private void getNextPosition(){
        
        //movement
        positionMoveLeftRight();
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
        collisionLeftRight();
        collisionUpDown(30);
        
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
