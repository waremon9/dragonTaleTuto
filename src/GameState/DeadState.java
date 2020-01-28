/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameState;

import java.awt.*;
import java.awt.event.KeyEvent;
/**
 *
 * @author verhi
 */
public class DeadState extends GameState{

    private int currentChoice = 0;
    private String[] options = {
            "Restart",
            "Help",
            "Menu"
    };

    private Color titleColor;
    private Font titleFont;

    private Font font;
    
    public DeadState(GameStateManager gsm){
        
        this.gsm = gsm;
        
        titleColor = new Color(50, 50, 50);
        titleFont = new Font(
                        "Century Gothic",
                        Font.PLAIN,
                        28);

        font = new Font("Arial", Font.PLAIN, 12);

    }
    
    public void init(){}
    public void update(){}
    public void draw(Graphics2D g){

        // draw title
        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString("Game Over", 80, 70);
        
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
                gsm.setState(GameStateManager.LEVEL1STATE);
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
