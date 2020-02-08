/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity.Enemies;

import Entity.Animation;
import Entity.Damage;
import Entity.Enemy;
import Entity.Player;
import Entity.PoisonBall;
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
    
    private boolean idle;
    
    //poisonball
    private boolean firing;
    private int poisonBallDamage;
    private ArrayList<PoisonBall> poisonBalls;
    private int poisonDelay;
    
    //munch attack
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
    private static final int JUMPING = 1;
    private static final int POISONBALL = 3;
    private static final int MUNCHING = 2;
 
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
        
        canBeBacking = false;
        
        facingRight = true;
        
        health = this.maxHealth = 20;
        this.xp = 35;

        poisonBalls = new ArrayList<PoisonBall>();
        poisonBallDamage = 2;
        
        munchDamage = 3;
        munchRange = 20;
        
        //load sprites (all the sprites of the 4 differrent animation
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
        idle = false;
        munching = false;
        jumping = false;
        firing = true;
    }
    public void setMunching(){
        idle = false;
        jumping = false;
        firing = false;
        munching = true;
    }
    public void setJumping(){
        idle = false;
        munching = false;
        firing = false;
        jumping = true;
    }
    public void setIdle(){
        munching = false;
        jumping = false;
        firing = false;
        idle = true;
    }
    
    private void getNextPosition(){
        
        //jumping
        if(jumping && !falling){
            dy = jumpStart;
            if(facingRight) dx = -1.5;
            else dx = 1.5;
            falling = true;
        }
        else if (!falling) dx = 0;
        
        //falling
        positionFalling();
        
        
    }
    
    public void chooseAction(int xPlayer, int yPlayer){
        //idle when too far, fire when good distance, flee when too close
        if(xPlayer>x) facingRight = false;
        else facingRight = true;
        if(xPlayer>x+170 || xPlayer<x-170) setIdle();
        else if(xPlayer > x+70 || xPlayer < x-70) setFiring();
        else{
            setJumping();
            facingRight = !facingRight;
        }
    }
    
    public ArrayList<Damage> poisonHit(TileMap tm, Player player){
        ArrayList<Damage> damages = new ArrayList<Damage>();
        for (int j = 0; j < poisonBalls.size(); j++) {
            if(poisonBalls.get(j).intersects(player)){
                player.health-=poisonBallDamage;
                damages.add(new Damage(tm, poisonBallDamage, player.getx(), player.gety()));
                poisonBalls.get(j).setHit();
                break;
            }
        }
        return damages;
    }
    
    public void update(){
        
        //update position
        getNextPosition();
        super.update();
            
        
        //check flinching
        if(flinching){
            long elapsed = (System.nanoTime() - flinchTimer)/1000000;
            if (elapsed>1000){
                flinching = false;
            }
        }
        
        //update poisonBall
        for(int i = 0; i<poisonBalls.size(); i++){
            poisonBalls.get(i).update();
            if(poisonBalls.get(i).shouldRemove()){
                poisonBalls.remove(i);
                i--;
            }
        }
        
        //set animation
        if(munching){
            if(currentAction != MUNCHING){
                currentAction = MUNCHING;
                animation.setFrames(sprites.get(MUNCHING));
                animation.setDelay(150);
            }
        }else if(firing){
            if(currentAction != POISONBALL){
                currentAction = POISONBALL;
                animation.setFrames(sprites.get(POISONBALL));
                animation.setDelay(270);
                poisonDelay = 150;
            }
        }else if(jumping){
            //falling animation is idle
            if(currentAction != JUMPING){
                    currentAction = JUMPING;
                    animation.setFrames(sprites.get(JUMPING));
                    animation.setDelay(60);
            }
        }else if(idle){
            if(currentAction != IDLE){
                currentAction = IDLE;
                animation.setFrames(sprites.get(IDLE));
                animation.setDelay(200);
                width = 30;
            }
        }
        
        //poison attack
        poisonDelay--;
        if (poisonDelay<0) poisonDelay=0;
        if(firing && !falling && poisonDelay == 0){
            PoisonBall pb = new PoisonBall(tileMap, !facingRight);
            pb.setPosition(x, y);//poisonBall appear at the same position as the munchie
            poisonBalls.add(pb);
            poisonDelay = 200;
        }
        
        animation.update();
        
        //set direction
        if(currentAction != MUNCHING && currentAction != POISONBALL){
            if(right)facingRight = true;
            if(left) facingRight = false;
        }
        
        
    }
    
    public void draw(Graphics2D g){
        
        setMapPosition();//first to  call in any map object
        
        //draw poisonBall
        for(int i = 0; i<poisonBalls.size(); i++){
            poisonBalls.get(i).draw(g);
        }
        
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
