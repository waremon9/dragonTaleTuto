/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import TileMap.TileMap;

/**
 *
 * @author verhi
 */
public class Enemy extends MapObject{
    
    protected int health;
    protected int maxHealth;
    protected boolean dead;
    protected int damage;
    protected int xp;
    
    protected boolean flinching;
    protected long flinchTimer;
    
    protected boolean canBeBacking;
    protected boolean backing;
    protected boolean backingRight;
    protected boolean backingFirst;
    
    public Enemy(TileMap tm){
        super(tm);
    }
    
    public boolean isDead(){return dead;}
    public int getDamage(){return damage;}
    public int getXp(){return xp;}
    
    
    //if cannot hit, return null array, else return xp if dead and true it hit
    public int hit(int damage, boolean faceRight){
        if(dead || flinching){
            return -1;
        }
        if(canBeBacking){
            backing = backingFirst = true;
            backingRight = faceRight;
        }
        health -= damage;
        if(health <= 0) {
            dead = true;
            return xp;
        }
        flinching = true;
        flinchTimer = System.nanoTime();
        return 0;
    }
    
    public void update(){
        
        //update position
        checkTileMapCollision();
        setPosition(xtemp, ytemp);
        
        
    }
    
    public void positionFalling(){
        //falling
        if(falling){
            dy += fallSpeed;
        }
    }
    
    public void positionKnockback(double power){
        //knockback
        if(backing){
            if(backingFirst) {
                dy -= power;
                backingFirst = false;
            }
            if(backingRight) dx=power/4;
            else dx=-(power/4);
            if(dy==0 && !falling) backing = false;
        }
    }
    
    public void positionMoveLeftRight(){
        //movement
        if(!backing){
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
        }
    }
    
    public void positionUpDown(){
        if(up){
            dy -= moveSpeed;
            if(dy < -maxSpeed){
                dy   = -maxSpeed;
            }
        }else if(down){
            dy += moveSpeed;
            if(dy > maxSpeed){
                dy = maxSpeed;
            }
        }
    }
    
    public void collisionLeftRight(){
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
    }
    
    public void collisionUpDown(int tropBasHaut){
        //if hits a wall, go other direction
        if(down && (dy == 0 || y>=tileMap.height-tropBasHaut)){//dy set to 0 when wallhit in MapObject
            dy = 0;
            down = false;
            up = true;
        }else if(up && (dy == 0 || y<=tropBasHaut)){//dy set to 0 when wallhit in MapObject
            dy = 0;
            up = false;
            down = true;
        }
    }
    
}
