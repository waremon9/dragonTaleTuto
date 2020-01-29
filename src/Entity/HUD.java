/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 *
 * @author verhi
 */
public class HUD {
    
    private Player player;
    
    private BufferedImage healtFire;
    private BufferedImage XP;
    private Font font;
    
    public HUD(Player p){
        player = p;
        
        try{
            
            healtFire = ImageIO.read(getClass().getResourceAsStream("/res/HUD/hud.gif"));
            XP = ImageIO.read(getClass().getResourceAsStream("/res/HUD/xp.gif"));
            font = new Font("Arial", Font.PLAIN, 14);
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void draw(Graphics2D g){
        g.drawImage(healtFire, 0, 5, null);
        g.drawImage(XP, 100, 5, null);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(player.getHealth() + " / " + player.getMaxHealth(), 25, 19);
        g.drawString(player.getFire()/100 + " / " + player.getMaxFire()/100, 20, 41);
        g.drawString(player.getLvl()+"", 125, 20);
        g.drawString(player.getXp() + " / " + player.getXpNext(), 170, 20);
    }
    
}
