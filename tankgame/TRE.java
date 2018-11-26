/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tankgame;


import javax.sound.sampled.AudioInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


import static javax.imageio.ImageIO.read;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class TRE extends JPanel  {


    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 960;
    private BufferedImage world;
    private Graphics2D buffer;
    private JFrame jf;
    private tankgame.Tank t1;
    private tankgame.Tank t2;
    private BufferedImage background;
    private BufferedImage breakableWall;
    private BufferedImage unbreakableWall;
    private BufferedImage powerup;
    private Image powerUp;
    private tankgame.CollisionDetection cd;
    private tankgame.Title title;
    private tankgame.Map map = new tankgame.Map();






    public static void main(String[] args) {
        Thread x;
        TRE trex = new TRE();



            trex.init();

            try {

                while (true) {
                    if (trex.title.isStart()) {
                        trex.t1.update();
                        trex.t2.update();
                        if(trex.t1.isGameOver() || trex.t2.isGameOver())
                            break;
                        trex.repaint();
                        Thread.sleep(1000 / 144);
                    } else
                        trex.repaint();
                }
            } catch (InterruptedException ignored) {

            }
           trex.closeGame();

    }


    private void init() {

        this.jf = new JFrame("Tank Rotation");
        this.world = new BufferedImage(TRE.SCREEN_WIDTH, TRE.SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        BufferedImage t1img = null;
        try {
             t1img = read(new File("Resources/tank1.png"));

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }


        try {
            background = read(new File("Resources/Background.bmp"));

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        try {
              unbreakableWall = read(new File("Resources/Wall2.gif"));

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            breakableWall = read(new File("Resources/Wall1.gif"));

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        try {
            powerup = read(new File("Resources/Shield1.gif"));
            this.powerUp = powerup.getScaledInstance(32,32,Image.SCALE_FAST);

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

         t1 = new tankgame.Tank(200, 200, 0, 0, 0, t1img,1);
        t2 = new tankgame.Tank(1020, 710, 0, 0, 180, t1img,2);


        tankgame.TankControl tc1 = new tankgame.TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
        tankgame.TankControl tc2 = new tankgame.TankControl(t2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);

        this.cd = new tankgame.CollisionDetection(this.t1, this.t2, this.map);
        cd.checkWall();

        t1.getCollisionDetection(cd);
        t2.getCollisionDetection(cd);


        this.title = new tankgame.Title(KeyEvent.VK_ESCAPE);

        this.jf.setLayout(new BorderLayout());
        this.jf.add(this);


        this.jf.addKeyListener(tc1);
        this.jf.addKeyListener(tc2);
        this.jf.addKeyListener(title);

        this.jf.setSize(TRE.SCREEN_WIDTH, TRE.SCREEN_HEIGHT +30);
        this.jf.setResizable(false);
        jf.setLocationRelativeTo(null);

        this.jf.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.jf.setVisible(true);


    }


    public void closeGame()
    {
        this.jf.dispatchEvent(new WindowEvent(this.jf,WindowEvent.WINDOW_CLOSING));
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        buffer = world.createGraphics();
        super.paintComponent(g2);


        if(!this.title.isStart()) {
            this.title.drawImage(buffer);
            g2.drawImage(world, 0, 0, null);
        }
        else {
            this.drawImage(buffer);
            this.t1.drawImage(buffer);
            this.t2.drawImage(buffer);

            if (this.t1.bullet()) {
                this.t1.drawImageBullet(buffer);
            }

            if (this.t2.bullet()) {
                this.t2.drawImageBullet(buffer);
            }

            // to check the position of tanks and display the map with respect to it for the split screen

            int tank1position = this.t1.currentPositionx();
            int tank2position = this.t2.currentPositionx();

                        if(tank1position<=SCREEN_WIDTH/4)
                            tank1position = 0;
                        else if(tank1position <3*SCREEN_WIDTH/4)
                            tank1position = tank1position - SCREEN_WIDTH/4;
                        else
                            tank1position = SCREEN_WIDTH/2;

                                                    if(tank2position<=SCREEN_WIDTH/4)
                                                        tank2position = 0;
                                                    else if(tank2position <3*SCREEN_WIDTH/4)
                                                        tank2position = tank2position - SCREEN_WIDTH/4;
                                                    else
                                                        tank2position = SCREEN_WIDTH/2;

            BufferedImage leftHalf = this.world.getSubimage(tank1position,0,SCREEN_WIDTH/2,SCREEN_HEIGHT);
            BufferedImage rightHalf = this.world.getSubimage(tank2position,0,SCREEN_WIDTH/2,SCREEN_HEIGHT);
            g2.drawImage(leftHalf, 0,0, null);
            g2.drawImage(rightHalf, SCREEN_WIDTH/2,0, null);
            g2.drawImage(this.world, 3*SCREEN_WIDTH/8,3*SCREEN_HEIGHT/4,SCREEN_WIDTH/4,SCREEN_HEIGHT/4, null);

            String string1 = "TANK A LIVES: ";
            String string2 = "TANK B LIVES: ";
            int health1 = this.t1.getTankHealth();
            int health2 = this.t2.getTankHealth();
            int life1 = this.t1.getTankLives();
            int life2 = this.t2.getTankLives();
            Font stringFont = new Font("TimesNewRoman", Font.BOLD, 36);
            g2.setFont(stringFont);
            String lifeStr = Integer.toString(life1);
            string1 = string1 + lifeStr;
            lifeStr = Integer.toString(life2);
            string2 = string2 + lifeStr;
            g2.setColor(Color.BLACK);
            g2.drawString(string1,100,90);
            g2.drawString(string2,900,90);
            g2.setColor(Color.GREEN);
            g2.fill3DRect(100, 100,health1,25,true);
            g2.fill3DRect(1000, 100,health2,25,true);
        }
    }


    void drawImage(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.background, 0,0,SCREEN_WIDTH,SCREEN_HEIGHT, null);
        int xValue=0,yValue=0,i=0,j=0;

        // to draw breakable,unbreakable walls and power up

        while(i<30) {
            int layer[][] = this.cd.returnMap();
            if(layer[i][j]==9) {
                g2d.drawImage(this.unbreakableWall, xValue, yValue, null);
            }
            else if(layer[i][j]==3)
                g2d.drawImage(this.breakableWall, xValue, yValue, null);
            else if(layer[i][j]==5)
                g2d.drawImage(this.powerUp, xValue, yValue, null);

            j++;
            if(j>39) {
                i++;
                j = 0;
                xValue = 0;
                yValue = yValue + breakableWall.getHeight();
            }
            else
                xValue= xValue + breakableWall.getWidth();

        }
    }


}