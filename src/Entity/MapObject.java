/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import Main.GamePanel;
import TileMap.Tile;
import TileMap.TileMap;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author verhi
 */

//all items in a level : ennemie, player, item, projectiles...
//abstract to be extended by these items to not have to recreate all of them everytime
//abstract superclass so var need to be protected so subclass can see these
public abstract class MapObject {
    
    //tiles stuff
    protected TileMap tileMap;
    protected int tileSize;
    protected double xmap;
    protected double ymap;
    
    //position and vector
    protected double x;
    protected double y;
    protected double dx;
    protected double dy;
    
    //dimensions
    protected int width;
    protected int height;
    
    //collision box (use to determine collision with tiles and ennemies (the real dimension))
    protected int cwidth;
    protected int cheight;
    
    //other collision stuff
    protected int curRow;//where the item is
    protected int curCol;
    protected double xdest;//the next position
    protected double ydest;
    protected double xtemp;//temporary position
    protected double ytemp;
    protected boolean topLeft;//4 point method for colision
    protected boolean topRight;//use the 4 corner to determine if item touch a block tile
    protected boolean bottomLeft;
    protected boolean bottomRight;
    
    //animation
    protected Animation animation;
    protected int currentAction;
    protected int previousAction;
    protected boolean facingRight;//facing right or left to flip or not the animation
    
    //movement (boolean that determine whet the item is currently doing)
    protected boolean left;
    protected boolean right;
    protected boolean up;
    protected boolean down;
    protected boolean jumping;
    protected boolean falling;
    
    //movement attributes (physics)
    protected double moveSpeed;//acceleration
    protected double maxSpeed;
    protected double stopSpeed; //decrease speed when touching nohing
    protected double fallSpeed;
    protected double maxFallSpeed;//terminal velocity
    protected double jumpStart;
    protected double stopJumpSpeed;//for mechanic when you press longer the jump button to go higher
    
    
    //constructor
    public MapObject(TileMap tm){
        tileMap = tm;
        tileSize = tm.getTileSize();
    }
    
    public boolean intersects(MapObject o){//true if item's rectangle collide with o's rectangle
        Rectangle r1 = getRectangle();
        Rectangle r2 = o.getRectangle();
        return r1.intersects(r2);
    }
    
    public Rectangle getRectangle(){
        return new Rectangle((int)x-cwidth,(int)y-cheight, cwidth, cheight);
    }
    
    public void calculateCorners(double x, double y){
        
        int leftTile = (int)(x-cwidth/2)/tileSize;
        int rightTile = (int)(x+cwidth/2 -1)/tileSize;//-1 to not step over the new column
        int topTile = (int)(y-cheight/2)/tileSize;
        int bottomTile = (int)(y+cheight/2 -1)/tileSize;//-1 to not step over the new row
        
        int tl = tileMap.getType(topTile, leftTile);
        int tr = tileMap.getType(topTile, rightTile);
        int bl = tileMap.getType(bottomTile, leftTile);
        int br = tileMap.getType(bottomTile, rightTile);
        topLeft = tl == Tile.BLOCKED;
        topRight = tr == Tile.BLOCKED;
        bottomLeft = bl == Tile.BLOCKED;
        bottomRight = br == Tile.BLOCKED;
        
    }
    
    public void checkTileMapCollision(){
        
        curCol = (int)x/tileSize;
        curRow = (int)y/tileSize;
        
        xdest = x+dx;
        ydest = y+dy;
        
        xtemp = x;
        ytemp = y;
        
        calculateCorners(x, ydest);
        if(dy<0){ //mean we go upward so check top 2 corners
            if(topLeft || topLeft){//we hit the ceiling
                dy = 0;
                ytemp = curRow * tileSize + cheight/2;
            }else{//no ceiling so we keep going
                ytemp += dy;
            }
        }
        if(dy>0){//mean we go downward so check bot 2 corners
            if(bottomLeft || bottomRight){
                dy = 0;
                falling = false;//because we hit the floor
                ytemp = (curRow + 1)*tileSize-cheight/2;//+1 to go just above the solide tile
            }else{
                ytemp += dy;//we keep falling
            }
        }
        
        calculateCorners(xdest, y);
        if(dx<0){ //mean we go left so check left 2 corners
            if(topLeft || bottomLeft){//we hit the wall
                dx = 0;
                xtemp = curCol * tileSize + cwidth/2;
            }else{//no wall so we keep going
                xtemp += dx;
            }
        }
        if(dx>0){//mean we go right so check bot 2 corners
            if(topRight || bottomRight){
                dx = 0;
                xtemp = (curCol + 1)*tileSize-cwidth/2;//+1 to not collide with the tile
            }else{
                xtemp += dx;//we keep going
            }
        }
        
        if(!falling){//check for falling off a cliff
            calculateCorners(x, ydest+1);
            if(!bottomLeft && !bottomRight){
                falling = true;
            }
        }
        
    }
    
    public int getx(){return (int)x;}
    public int gety(){return (int)y;}
    public int getWidth(){return width;}
    public int getHeight(){return height;}
    public int getCWidth(){return cwidth;}
    public int getCHeight(){return cheight;}
    
    public void setPosition(double x, double y){
        this.x = x;
        this.y = y;
    }
    
    public void setVector(double dx, double dy){
        this.dx = dx;
        this.dy = dy;
    }
    
    //every item has a global position and a local position
    public void setMapPosition(){
        xmap = tileMap.getX();
        ymap = tileMap.getY();
    }
    
    public void setLeft(boolean b){left = b;}
    public void setRight(boolean b){right = b;}
    public void setUp(boolean b){up = b;}
    public void setDown(boolean b){down = b;}
    public void setJumping(boolean b){jumping = b;}
    
    public boolean notOnScreen(){//tell if item on or not on screen to not draw offscreen items
        return x+xmap+width<0 ||
               x+xmap-width > GamePanel.WIDTH ||
               y+ymap+height < 0 ||
               y+ymap-height > GamePanel.HEIGHT; 
    }
    
    public void draw(Graphics2D g){
        if(facingRight){
            g.drawImage(animation.getImage(), (int)(x+xmap-width/2), (int)(y+ymap-height/2), null);
        }else{//flip the image
            g.drawImage(animation.getImage(), (int)(x+xmap-width/2+width), (int)(y+ymap-height/2),-width,height, null);
        }
    }
    
}
