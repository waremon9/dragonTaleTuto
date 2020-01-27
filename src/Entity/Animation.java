/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.awt.image.BufferedImage;

/**
 *
 * @author verhi
 */
public class Animation {
    
    private BufferedImage[] frames;
    private int currentFrame;
    
    private long startedTime;
    private long delay;
    
    //for animation that do not loop
    private boolean  playedOnce;
    
    public void Animation(){
        playedOnce = false;
    }
    
    public void setFrames(BufferedImage[] frames){
        this.frames = frames;
        currentFrame = 0;
        startedTime = System.nanoTime();
        playedOnce = false;
    }
    
    public void setDelay(long d){delay = d;}
    public void setFrame(int i){currentFrame = i;}
    
    //logic wheter or not to move to next frame
    public void update(){
        if (delay == -1)return;
        
        long elapsed = ((System.nanoTime() -startedTime)/1000000);
        if (elapsed > delay){//go to next frame
            currentFrame++;
            startedTime = System.nanoTime();
        }
        if(currentFrame == frames.length){//too far so we loop
            currentFrame = 0;
            playedOnce = true;
        }
    }
    
    public int getFrame() {return currentFrame;}
    public BufferedImage getImage(){return frames[currentFrame];}
    public boolean hasPlayedOnce(){return playedOnce;}
    
}
