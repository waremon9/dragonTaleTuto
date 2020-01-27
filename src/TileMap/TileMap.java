/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TileMap;

import Main.GamePanel;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;


/**
 *
 * @author verhi
 */
public class TileMap {
    
    //position
    private double x;
    private double y;
    
    //bounds
    private int xmin;
    private int ymin;
    private int xmax;
    private int ymax;
    
    //gradually moove the camera
    private double tween;
    
    //Map
    private  int[][] map;
    private int tileSize;
    private int numRows;
    private int numCols;
    private int width;
    private int height;
    
    //tileset
    private BufferedImage tileset;
    private int numTilesAcross;
    private Tile[][] tiles;
    
    //drawing
    private int rowOffset; //where to start drawing cause we wont draw thousand of tile 60*per second
    private int colOffset;
    private int numRowsToDraw;//how many after start
    private int numColsToDraw;
    
    public TileMap(int tileSize){
        this.tileSize = tileSize;
        numRowsToDraw = GamePanel.HEIGHT / tileSize +2;
        numColsToDraw = GamePanel.WIDTH / tileSize +2;
        tween = 0.07;
    }
    
    public void loadTile(String s){
        //load tileset file into memory
        try{
            tileset = ImageIO.read(getClass().getResource(s));
            numTilesAcross = tileset.getWidth() / tileSize;
            tiles = new Tile[2][numTilesAcross];
            
            BufferedImage subimage;
            for(int col = 0; col < numTilesAcross; col++){
                subimage = tileset.getSubimage(col*tileSize, 0, tileSize, tileSize);
                tiles[0][col] = new Tile(subimage, Tile.NORMAL);//first row in the tileset have no collision or should never come in contact with an entitie
                subimage = tileset.getSubimage(col*tileSize, tileSize, tileSize, tileSize);
                tiles[1][col] = new Tile(subimage, Tile.BLOCKED);//second row in the tileset have collision
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void loadMap(String s){
        //load map file into memory
        //in the file, first line is int nb of columns, second line is number of rows
        //all the other line are the map
        try{
            
            InputStream in = getClass().getResourceAsStream(s);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            
            numCols = Integer.parseInt(br.readLine());
            numRows = Integer.parseInt(br.readLine());
            map = new int[numRows][numCols];
            width = numCols * tileSize;
            height = numRows * tileSize;
            
            xmin = GamePanel.WIDTH - width;
            xmax = 0;
            ymin = GamePanel.HEIGHT - height;
            xmax = 0;
            
            //delimiters
            String delims = "\\s+";
            for(int row = 0; row < numRows; row++){
                String line = br.readLine();
                String[] tokens = line.split(delims);
                for(int col = 0; col < numCols;col++){
                    map[row][col]= Integer.parseInt(tokens[col]);
                }
                
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
    
    }

    public double getx() {return  x;}
    public double gety() {return  y;}
    public int getTileSize() {return tileSize;}
    public int getWidth() {return width;}
    public int getHeight() {return height;}
    
    public int getType(int row, int col){
        int rc = map[row][col];//get the number of the id of the tile (start at 0)

        int r = rc / numTilesAcross;
        int c = rc % numTilesAcross;
        return tiles[r][c].getType();
    }

    public void setTween(double tween) {this.tween = tween;}
    
    public void setPosition(double x, double y){
        //camera follow the player exactly
        /*this.x = x;
        this.y = y;*/
        
        //tween so camera follow smoothly
        this.x += (x-this.x)*tween;
        this.y += (y-this.y)*tween;
        
        fixBounds();
        
        //don't draw the entire tilemap
        colOffset = (int)-this.x / tileSize;
        rowOffset = (int)-this.y / tileSize;
    }
    
    private void fixBounds(){
        //make sure bounds aren't being past
        if(x<xmin) x = xmin;
        if(y<ymin) y = ymin;
        if(x>xmax) x = xmax;
        if(y>ymax) y = ymax;
    }
    
    public void draw(Graphics2D g){
        
        for(int row = rowOffset; row < rowOffset + numRowsToDraw; row++){
            
            if (row >= numRows) break;//nothing to draw so quit
            
            for (int col = colOffset; col < colOffset + numColsToDraw; col++){
                
                if (col >= numCols) break;//nothing to draw so quit
                if(map[row][col]==0) continue;//don't bother drawing void
                
                int rc = map[row][col];
                int r = rc / numTilesAcross;
                int c = rc % numTilesAcross;
                
                g.drawImage(tiles[r][c].getImage(), (int)x+col*tileSize, (int)y+row*tileSize, null);
                
            }
        }
    }
    
}
