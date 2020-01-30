/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameState;

import Entity.HUD;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 *
 * @author verhi
 */
public class PauseState extends GameState{
    
    private int currentChoice = 0;
    private String[] options = {
            "Continue",
            "Help",
            "Menu"
    };

    private Color titleColor;
    private Font titleFont;

    private Font font;
    
    private BufferedImage backHud;
    
    public PauseState(GameStateManager gsm){
        
        this.gsm = gsm;
        
        titleColor = new Color(50, 50, 50);
        titleFont = new Font(
                        "Century Gothic",
                        Font.PLAIN,
                        28);

        font = new Font("Arial", Font.PLAIN, 16);
        
        try{
            
            backHud = ImageIO.read(getClass().getResourceAsStream("/res/HUD/pause.gif"));
            
        }catch(Exception e){
            e.printStackTrace();
        }

    }
    
    public void init(){}
    public void update(){}
    public void draw(Graphics2D g){

        //draw back hud
        g.drawImage(backHud, 30, 20, null);
        
        // draw title
        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString("Game Paused", 70, 70);
        
        //draw menu options
        g.setFont(font);
        for(int i = 0; i<options.length;i++){
            if(i==currentChoice){
                g.setColor(Color.BLACK);
            }else{
                g.setColor(Color.RED);
            }
            g.drawString(options[i], 125,110+i*25);
        }
    }
    
    private void select(){
        switch (currentChoice) {
            case 0:
                gsm.unpauseLevelState();
                break;
            case 1:
                //help
                break;
            case 2:
                gsm.setState(GameStateManager.MENUSTATE);
            default:
                break;
        }
    }
    
    public void keyPressed(int k) {
        if(k == KeyEvent.VK_ENTER){
            select();
        }
        if(k == KeyEvent.VK_UP) {
            currentChoice--;
            if(currentChoice == -1) {
                    currentChoice = options.length - 1;
            }
        }
        if(k == KeyEvent.VK_DOWN) {
            currentChoice++;
            if(currentChoice == options.length) {
                    currentChoice = 0;
            }
        }
    }
    public void keyReleased(int k){}
}
