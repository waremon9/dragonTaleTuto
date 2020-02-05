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
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author verhi
 */
public class Munchie extends Enemy{
    
    //fireball
    private boolean firing;
    private int fireCost;
    private int fireBallDamage;
    //private ArrayList<FireBall> fireBalls;
    
    //scratch attack
    private boolean munching;
    private int munchDamage;
    private int munchRange;
    
    //jumping
    private boolean jumping;
    
    //animation
    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames={//nb of frames for every actions
        4, 9, 5, 12
    };
    
    //animation actions
    private static final int IDLE = 0;
    private static final int FALLING = 0;
    private static final int POISONBALL = 2;
    private static final int MUNCHING = 3;
 
    public Munchie(TileMap tm){
        
        super(tm);
        
        width = 64;
        height = 64;
        cwidth = 10;
        cheight = 40;
        
        fallSpeed = 0.15;
        maxFallSpeed = 4.0;
        jumpStart = -2.8;
        stopJumpSpeed = 0.3;
        
        facingRight = true;
        
        health = this.maxHealth = 20;
        this.xp = 35;
        
        fireCost = 200;
        fireBallDamage = 3;
        //fireBalls = new ArrayList<FireBall>();
        
        munchDamage = 3;
        munchRange = 20;
        
        //load sprites (all the sprites of the 7 differrent animation
        try{
            
            BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/res/Sprites/Enemies/munchie.gif"));
            
            sprites = new ArrayList<BufferedImage[]>();
            
            for(int i=0; i<4; i++){// 4 actions
                BufferedImage[] bi = new BufferedImage[numFrames[i]];
                for (int j=0; j<numFrames[i]; j++){
                    bi[j] = spriteSheet.getSubimage(j*width, i*height, width, height);
                }
                sprites.add(bi);
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        //set the starting animation (avoid null pointer exception)
        animation = new Animation();
        currentAction = IDLE;
        animation.setFrames(sprites.get(IDLE));
        animation.setDelay(150);
    }
    
    public int getHealth(){return health;}
    public int getMaxHealth(){return maxHealth;}
    public int getXp(){return xp;}
    
    //position for test in lvl
    public String getPosition(){return "x : "+x+" y : "+y;}
    
    public void setFiring(){
        firing = true;
    }
    public void setScratching(){
        munching = true;
    }
    public void setJumping(boolean b){
        jumping = b;
    }
    
    private void getNextPosition(){
        
        //jumping
        if(jumping && !falling){
            dy = jumpStart;
            safeSpotx = x;
            safeSpoty = y;
            falling = true;
        }
        
        //falling
        positionFalling();
        
        
    }
    
    public void update(){
        
        //update position
        getNextPosition();super.update();
            
        
        //check flinching
        if(flinching){
            long elapsed = (System.nanoTime() - flinchTimer)/1000000;
            if (elapsed>1000){
                flinching = false;
            }
        }
        
        //set animation
        if(munching){
            if(currentAction != MUNCHING){
                currentAction = MUNCHING;
                animation.setFrames(sprites.get(MUNCHING));
                animation.setDelay(50);
            }
        }else if(firing){
            if(currentAction != POISONBALL){
                currentAction = POISONBALL;
                animation.setFrames(sprites.get(POISONBALL));
                animation.setDelay(100);
            }
        }else if(dy > 0){
            if(currentAction != FALLING){
                    currentAction = FALLING;
                    animation.setFrames(sprites.get(FALLING));
                    animation.setDelay(100);
            }
        }else {
            if(currentAction != IDLE){
                currentAction = IDLE;
                animation.setFrames(sprites.get(IDLE));
                animation.setDelay(200);
                width = 30;
            }
        }
        
        animation.update();
        
        //set direction
        if(currentAction != MUNCHING && currentAction != POISONBALL){
            if(right)facingRight = true;
            if(left) facingRight = false;
        }
    }
    
    public void draw(Graphics2D g){
        
        setMapPosition();//first to bell call in any map object
        
        //dflinching
        if(flinching){
            long elapsed = (System.nanoTime()-flinchTimer)/1000000;
            if(elapsed / 100%2 == 0){ //blinking every 100 milliseconde
                return;
            }
        }
        
        super.draw(g);
        
    }
    
}
