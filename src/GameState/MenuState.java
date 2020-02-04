/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameState;

import java.awt.*;
import TileMap.Background;
import java.awt.event.KeyEvent;
/**
 *
 * @author verhi
 */
public class MenuState extends GameState {
    
    private Background bg;
    
    private int currentChoice = 0;
    private String[] options;
    private String[] optionsMenu = {
            "Start",
            "Help",
            "Quit"
    };
    
    private String[] optionsLevel = {
            "TestMap",
            "Level 1",
            "Level 2",
            "return"
    };
    
    private boolean menuSelect = true;

    private Color titleColor;
    private Font titleFont;

    private Font font;
        
    public MenuState(GameStateManager gsm){
        
        this.gsm = gsm;
        
        options = optionsMenu;
        try {

            bg = new Background("/res/Backgrounds/menubg.gif", 1);
            bg.setVector(-0.1, 0);

            titleColor = new Color(128, 0, 0);
            titleFont = new Font(
                            "Century Gothic",
                            Font.PLAIN,
                            28);

            font = new Font("Arial", Font.PLAIN, 12);

        }
        catch(Exception e) {
                e.printStackTrace();
        }
        
    }
    
    public void init(){}
    public void update(){
	bg.update();
    }
    public void draw(Graphics2D g){

        // draw bg
        bg.draw(g);

        // draw title
        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString("Dragon Tale", 80, 70);
        
        //draw menu options
        g.setFont(font);
        for(int i = 0; i<options.length;i++){
            if(i==currentChoice){
                g.setColor(Color.BLACK);
            }else{
                g.setColor(Color.RED);
            }
            g.drawString(options[i], 145,140+i*15);
        }
    }
    
    private void select(){
        switch (currentChoice) {
            case 0:
                if(menuSelect){
                    options = optionsLevel;
                    menuSelect = false;
                }else{
                    gsm.setLevelChoice("TEST.map");
                    gsm.setState(GameStateManager.LEVELSTATE);
                }
                break;
            case 1:
                if(!menuSelect){
                    gsm.setLevelChoice("level1-1.map");
                    gsm.setState(GameStateManager.LEVELSTATE);
                }
                break;
            case 2:
                if(menuSelect) System.exit(0);
                else{
                    gsm.setLevelChoice("level1-2.map");
                    gsm.setState(GameStateManager.LEVELSTATE);
                }
                break;
            case 3:
                if(!menuSelect){
                    options = optionsMenu;
                    menuSelect = true;
                    currentChoice = 0;
                }
                break;
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
