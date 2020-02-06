/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import TileMap.TileMap;

/**
 *
 * @author verhi
 */
public class PoisonBall extends MapObject{
    
    private boolean hit;
    private boolean remove;
    private BufferedImage[] sprites; 
    private BufferedImage[] hitSprites; 
    
    public PoisonBall(TileMap tm, boolean right){
        
        super(tm);
        
        facingRight = right;
        
        moveSpeed = 3.0;
        if(right) dx = moveSpeed;
        else dx = -moveSpeed;
        
        width = 32;//size in spritesheet
        height = 32;
        cwidth = 14;//real hitbox
        cheight = 14;
        
        //load sprites
        try{
            
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/res/Sprites/Enemies/poisonBall.gif"));
            
            sprites = new BufferedImage[3];
            for (int i = 0; i < sprites.length; i++){
                sprites[i] = spritesheet.getSubimage(i*width, 0, width, height);
            }
            
            hitSprites = new BufferedImage[3];
            for (int i = 3; i < hitSprites.length+3; i++){
                hitSprites[i-3] = spritesheet.getSubimage(i*width, 0, width, height);
            }
            
            animation = new Animation();
            animation.setFrames(sprites);
            animation.setDelay(70);
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    public void setHit(){//when the fireball hit something
        if(hit) return;
        hit = true;
        animation.setFrames(hitSprites);
        animation.setDelay(70);
        dx = 0;
    }
    
    public boolean shouldRemove(){ return remove;}
    
    public void update(){
        
        checkTileMapCollision();
        setPosition(xtemp, ytemp);
        
        if(dx == 0 && !hit){
            setHit();
        }
        
        animation.update();
        if(hit && animation.hasPlayedOnce()){
            remove = true;
        }
        
    }
    
    public void draw(Graphics2D g){
        
        setMapPosition();
        
        super.draw(g);
        
    }
}
