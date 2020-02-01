/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import TileMap.TileMap;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 *
 * @author verhi
 */
public class Damage extends MapObject{

    private int hit;
    private boolean remove;
    private BufferedImage[] allSprites;
    private BufferedImage[] rightSprite;
    private ArrayList<Animation> allAnimation;
    private int damage;
    
    public Damage(TileMap tm, int value, double x, double y){
        
        super(tm);
        damage = value;
        
        facingRight = true;
        
        this.x=x;
        this.y=y;
        
        moveSpeed = 0.2;
        dy = -3;
        
        Random rd = new Random(); // creating Random object
        dx = (rd.nextDouble()-0.5)*2; //random between -1 and 1
        
        width = 20;//size in spritesheet
        height = 20;
        cwidth = 10;//real hitbox
        cheight = 10;
        
        remove = false;
        hit = 0;
        
        //load sprites
        allAnimation = new ArrayList<Animation>();
        try{
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/res/Sprites/damage.gif"));

            allSprites = new BufferedImage[10];
            for (int i = 0; i < allSprites.length; i++){
                allSprites[i] = spritesheet.getSubimage(i*width, 0, width, height);
            }
            while(damage>=1){
                rightSprite = new BufferedImage[1];
                rightSprite[0] = allSprites[damage%10];

                animation = new Animation();
                animation.setFrames(rightSprite);
                animation.setDelay(500);
                allAnimation.add(animation);
                
                damage/=10;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void addHit(){//when the fireball hit something
        hit++;
        if(hit == 3) remove = true;
    }
    
    public boolean shouldRemove(){ return remove;}
    
    private void getNextPosition(){
        
        //falling
        if(falling){
            dy += moveSpeed;
        }
        
        if(y>tileMap.height-15){
            addHit();
            dy = -3/hit;
        }
        
    }
    
    public int getDamage(){return damage;}
    
    public void update(){
        
        //update position
        getNextPosition();
        checkTileMapCollision();
        setPosition(xtemp, ytemp);
        
        if(dy == 0){
            addHit();
            dy = -3/hit;
        }
        
    }
    
    public void draw(Graphics2D g){
        
        setMapPosition();
        
        if(!notOnScreen()){
            for(int i = 0; i<allAnimation.size();i++){
                g.drawImage(allAnimation.get(i).getImage(), (int)(x+xmap-width/2-i*15), (int)(y+ymap-height/2), null);
            }
        }
        
    }
    
}
