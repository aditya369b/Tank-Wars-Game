package tankgame;


import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
//import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static javax.imageio.ImageIO.read;
import static tankgame.TRE.SCREEN_HEIGHT;
import static tankgame.TRE.SCREEN_WIDTH;


public class Title implements KeyListener{

    private boolean start = false;
    private int key;
    private BufferedImage titleimg;

    Title(int key)
    {
        this.key=key;

        try {
            titleimg = read(new File("Resources/Title.bmp"));

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }


    public void keyTyped(KeyEvent ke) {
        int keyTyped = ke.getKeyChar();
        if (keyTyped == key) {
            this.start =true;
        }
    }

    public void keyPressed(KeyEvent ke) {}
    public void keyReleased(KeyEvent ke) {}


    boolean isStart()
    {
        return this.start;
    }

    void drawImage(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

            g2d.drawImage(this.titleimg, 0,0,1280,960, null);

        String string = "PRESS 'Esc' TO START THE GAME!!! ";
        String Controls1 = "Player 1 CONTROLS: W,A,S,D,Space bar";
        String Controls2 = "Player 2 CONTROLS: Up,Down,Left,Right,Enter";

        Font displayFont = new Font("TimesNewRoman", Font.BOLD, 48);
        Font controlFont = new Font("TimesNewRoman", Font.BOLD, 16);
        g2d.setFont(displayFont);
        g2d.drawString(string, 200, 100);
        g2d.setFont(controlFont);
        g2d.setColor(Color.BLACK);
        g2d.drawString(Controls1,100,750);
        g2d.drawString(Controls2,800,750);

    }


}
