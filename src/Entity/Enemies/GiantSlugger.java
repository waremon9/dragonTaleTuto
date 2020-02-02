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
public class GiantSlugger extends Enemy{
        
    private BufferedImage[] sprites;
 
    public GiantSlugger(TileMap tm) {
        
        super(tm);
        
        moveSpeed = 0.02;
        maxSpeed = 0.3;
        
        fallSpeed = 0.2;
        maxFallSpeed = 10.0;
        
        width = 60;
        height = 60;
        cwidth = 35;
        cheight = 27;
        
        health = maxHealth = 50;
        damage = 4;
        
        xp = 69;
        
        //load sprites
        try{
            
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/res/Sprites/Enemies/giantSlugger.gif"));
            
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
        
        isAboutToFall();
        
        //movement
        if(left){
            dx -= moveSpeed;
            if(dx < -maxSpeed){
                dx = -maxSpeed;
            }
        }else if(right){
            dx += moveSpeed;
            if(dx > maxSpeed){
                dx = maxSpeed;
            }
        }
        
        //falling
        if(falling){
            dy += fallSpeed;
        }
    }
    
    public void update(){
        
        //update position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);
        
        //check flinching
        if(flinching){
            long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
            if(elapsed > 400) {
                flinching = false;
            }
        }
        
        //if hits a wall, go other direction
        if(right && dx == 0){//dx set to 0 when wallhit in MapObject
            right = false;
            left = true;
            facingRight = false;
        }else if(left && dx == 0){//dx set to 0 when wallhit in MapObject
            left = false;
            right = true;
            facingRight = true;
        }
        
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
