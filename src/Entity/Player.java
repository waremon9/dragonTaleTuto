/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Audio.AudioPlayer;
import TileMap.TileMap;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import static java.lang.Math.round;

/**
 *
 * @author verhi
 */
public class Player extends MapObject{
    
    //player stuff
    private String pseudo;
    public int health;
    private int maxHealth;
    private int fire;
    private int maxFire;
    private boolean dead;
    private boolean  flinching;
    private long flincTimer;
    private int xp;
    
    //fireball
    private boolean firing;
    private int fireCost;
    private int fireBallDamage;
    private ArrayList<FireBall> fireBalls;
    
    //scratch attack
    private boolean scratching;
    private int scratchDamage;
    private int scratchRange;
    
    //gliding
    private boolean gliding;
    
    //animation
    private ArrayList<BufferedImage[]> sprites;
    private final int[] numFrames={//nb of frames for every actions
        2, 8, 1, 2, 4, 2, 5
    };
    
    //animation actions
    private static final int IDLE = 0;
    private static final int WALKING = 1;
    private static final int JUMPING = 2;
    private static final int FALLING = 3;
    private static final int GLIDING = 4;
    private static final int FIREBALL = 5;
    private static final int SCRATCHING = 6;
    
    //audio
    private HashMap<String, AudioPlayer> sfx;
 
    public Player(TileMap tm, String pseudo, int maxHealth, int maxFire, int xp){
        
        super(tm);
        
        this.pseudo = pseudo;
        
        width = 30;
        height = 30;
        cwidth = 20;
        cheight = 20;
        
        moveSpeed = 0.3;
        maxSpeed = 1.6;
        stopSpeed = 0.4;
        fallSpeed = 0.15;
        maxFallSpeed = 4.0;
        jumpStart = -4.8;
        stopJumpSpeed = 0.3;
        
        facingRight = true;
        
        health = this.maxHealth = maxHealth;
        fire = this.maxFire = maxFire;
        this.xp = xp;
        
        fireCost = 200;
        fireBallDamage = 3;
        fireBalls = new ArrayList<FireBall>();
        
        scratchDamage = 5;
        scratchRange = 40;
        
        //load sprites (all the sprites of the 7 differrent animation
        try{
            
            BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/res/Sprites/Player/Playersprites.gif"));
            
            sprites = new ArrayList<BufferedImage[]>();
            
            for(int i=0; i<7; i++){// 7 actions
                BufferedImage[] bi = new BufferedImage[numFrames[i]];
                for (int j=0; j<numFrames[i]; j++){
                    if(i!= SCRATCHING){//scratch animation isn't 30*30 but 60*30
                        bi[j] = spriteSheet.getSubimage(j*width, i*height, width, height);
                    }else{    
                        bi[j] = spriteSheet.getSubimage(j*width*2, i*height, width*2, height);
                    }
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
        animation.setDelay(400);
        
        sfx = new HashMap<String, AudioPlayer>();
        sfx.put("jump", new AudioPlayer("/res/SFX/jump.mp3"));
        sfx.put("scratch", new AudioPlayer("/res/SFX/scratch.mp3"));
    }
    
    public int getHealth(){return health;}
    public int getMaxHealth(){return maxHealth;}
    public int getFire(){return fire;}
    public int getMaxFire(){return maxFire;}
    public int getXp(){return xp;}
    public String getPseudo(){return pseudo;}
    public boolean isFlinching(){return flinching;}
    
    //position for test in lvl
    public String getPosition(){return "x : "+x+" y : "+y;}
    
    public void setFiring(){
        firing = true;
    }
    public void setScratching(){
        scratching = true;
    }
    public void setGliding(boolean b){
        gliding = b;
    }
    
    public ArrayList<Damage> checkAttack(ArrayList<Enemy> enemies, TileMap tm){
        //loop through all enemies
        ArrayList<Damage> damages = new ArrayList<Damage>();
        int enemyHit;
        for (int i = 0; i < enemies.size(); i++) {
            
            Enemy e = enemies.get(i);
            
            //scratch attack
            if(scratching){
                if(facingRight){
                    if(
                            e.getx()>x &&
                            e.getx()<x+scratchRange &&
                            e.gety()>y-height/2 &&
                            e.gety()<y+height/2
                    ) {
                        enemyHit = e.hit(scratchDamage, true);
                        if(enemyHit != -1){
                            gainXp(enemyHit);
                            damages.add(new Damage(tm, scratchDamage, e.getx(), e.gety()));
                        }
                    }
                }else{
                    if(
                            e.getx()<x &&
                            e.getx()>x-scratchRange &&
                            e.gety()>y-height/2 &&
                            e.gety()<y+height/2
                    ) {
                        enemyHit = e.hit(scratchDamage, false);
                        if(enemyHit != -1){
                            gainXp(enemyHit);
                            damages.add(new Damage(tm, scratchDamage, e.getx(), e.gety()));
                        }
                    }
                }
            }
            
            //fireballs
            for (int j = 0; j < fireBalls.size(); j++) {
                if(fireBalls.get(j).intersects(e)){
                    enemyHit = e.hit(fireBallDamage, fireBalls.get(j).facingRight);
                    if(enemyHit!=-1){
                        gainXp(enemyHit);
                        damages.add(new Damage(tm, fireBallDamage, e.getx(), e.gety()));
                    }
                    fireBalls.get(j).setHit();
                    break;
                }
            }
            
            //check enemy collision
            if(intersects(e)){
                boolean reallyHit = hit(e.getDamage());
                if(reallyHit){
                    damages.add(new Damage(tm, e.getDamage(), x, y));
                }
            }
        }
        return damages;
    }
    
    public void gainXp(int gain){
        xp += gain;
    }
    
    public boolean hit (int damage){
        if(flinching || dead) return false;
        health -= damage;
        if(health<0) health = 0;
        if(health==0){
            xp -= round(xp/3);
            dead = true;
        }
        flinching = true;
        flincTimer = System.nanoTime();
        return true;
    }
    
    public boolean isDead(){return dead;}
    
    private void getNextPosition(){
        
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
        }else{
            if(dx >0){
                dx -= stopSpeed;
                if(dx < 0){
                    dx = 0;
                }
            }else if(dx < 0){
                dx += stopSpeed;
                if(dx > 0){
                    dx = 0;
                }
            }
        }
        
        //cannot move while attacking, except in air
        if((currentAction == SCRATCHING || currentAction == FIREBALL)&& !(jumping || falling)){
            dx = 0;
        }
        
        //jumping
        if(jumping && !falling){
            sfx.get("jump").play();
            dy = jumpStart;
            safeSpotx = x;
            safeSpoty = y;
            falling = true;
        }
        
        //falling
        if(falling){
            if(dy>0 && gliding) dy+= fallSpeed*0.1; //gliding so less falling acceleration
            else dy += fallSpeed;
            
            if(dy>0) jumping = false;
            if(dy < 0 && !jumping) dy += stopJumpSpeed;//released jump button
            
            if(dy > maxFallSpeed) dy = maxFallSpeed;
            
        }
        
        
    }
    
    public void update(){
        
        //update position
        if(!dead){
            getNextPosition();
            checkTileMapCollision();
            if(ytemp>tileMap.height-14) {
                xtemp = safeSpotx;
                ytemp = safeSpoty;
                hit(round(maxHealth/5));
            }
            setPosition(xtemp, ytemp);
        }
            
        //check attack has stopped
        if(currentAction == SCRATCHING){
            if(animation.hasPlayedOnce()) scratching = false;
        }
        if(currentAction == FIREBALL){
            if(animation.hasPlayedOnce()) firing = false;
        }
        
        //fireball attack
        fire += 1;//regenerate fire
        if (fire > maxFire) fire = maxFire;
        if(firing && currentAction != FIREBALL){
            if (fire >= fireCost){
                fire -= fireCost;
                FireBall fb = new FireBall(tileMap, facingRight);
                fb.setPosition(x, y);//fireball appear at the same position as the player
                fireBalls.add(fb);
            }
        }
        
        //update fireballs
        for(int i = 0; i<fireBalls.size(); i++){
            fireBalls.get(i).update();
            if(fireBalls.get(i).shouldRemove()){
                fireBalls.remove(i);
                i--;
            }
        }
        
        //check done flinching
        if(flinching){
            long elapsed = (System.nanoTime() - flincTimer)/1000000;
            if (elapsed>1000){
                flinching = false;
            }
        }
        
        //set animation
        if(scratching){
            if(currentAction != SCRATCHING){
                sfx.get("scratch").play();
                currentAction = SCRATCHING;
                animation.setFrames(sprites.get(SCRATCHING));
                animation.setDelay(50);
                width = 60;
            }
        }else if(firing){
            if(currentAction != FIREBALL){
                currentAction = FIREBALL;
                animation.setFrames(sprites.get(FIREBALL));
                animation.setDelay(100);
                width = 30;
            }
        }else if(dy>0){
            if (gliding){
                if(currentAction != GLIDING){
                    currentAction = GLIDING;
                    animation.setFrames(sprites.get(GLIDING));
                    animation.setDelay(100);
                    width = 30;
                }
            }else if(currentAction != FALLING){
                currentAction = FALLING;
                animation.setFrames(sprites.get(FALLING));
                animation.setDelay(100);
                width = 30;
            }
        }else if(dy<0){
            if(currentAction != JUMPING){
                currentAction = JUMPING;
                animation.setFrames(sprites.get(JUMPING));
                animation.setDelay(-1);
                width = 30;
            }
        }else if(left || right){
            if(currentAction != WALKING){
                currentAction = WALKING;
                animation.setFrames(sprites.get(WALKING));
                animation.setDelay(40);
                width = 30;
            }
        }else {
            if(currentAction != IDLE){
                currentAction = IDLE;
                animation.setFrames(sprites.get(IDLE));
                animation.setDelay(400);
                width = 30;
            }
        }
        
        animation.update();
        
        //set direction
        if(currentAction != SCRATCHING && currentAction != FIREBALL){
            if(right)facingRight = true;
            if(left) facingRight = false;
        }
    }
    
    public void draw(Graphics2D g){
        
        setMapPosition();//first to bell call in any map object
        
        //draw fireballs
        for(int i = 0; i<fireBalls.size(); i++){
            fireBalls.get(i).draw(g);
        }
        
        //draw player
        if (dead){
            return;
        }else if(flinching){
            long elapsed = (System.nanoTime()-flincTimer)/1000000;
            if(elapsed / 100%2 == 0){ //blinking every 100 milliseconde
                return;
            }
        }
        
        //call function draw from MapObject
        super.draw(g);
        
    }
    
}
