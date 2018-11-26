package tankgame;



import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static javax.imageio.ImageIO.read;

public class Tank{


    private int x, xInitial;
    private int y,yInitial;
    private int vx, vxInitial;
    private int vy, vyInitial;
    private int angle, angleInitial;
    private tankgame.Bullet bullet;
    private tankgame.CollisionDetection cd;
    private int tankPlayer;
    private ArrayList<Integer> position = new ArrayList<>();
    private ArrayList<Integer> initialPosition = new ArrayList<>();
    private tankgame.Health health = new tankgame.Health();

    private final int R = 2;
    private final int ROTATIONSPEED = 4;


    private BufferedImage img;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean EnterPressed;
    private BufferedImage bulletimg = null;
    private boolean bulletPresent = false;



    Tank(int x, int y, int vx, int vy, int angle, BufferedImage img,int tankPlayer) {
        this.x = this.xInitial = x;
        this.y = this.yInitial = y;
        this.vx = this.vxInitial = vx;
        this.vy = this.vyInitial = vy;
        this.angle = this.angleInitial =  angle;
        this.img = img;
        this.tankPlayer = tankPlayer;
        this.position.add(0,this.x);
        this.position.add(1,this.y);
        this.position.add(2,this.vx);
        this.position.add(3,this.vy);
        this.initialPosition.add(0,this.x);
        this.initialPosition.add(1,this.y);
        this.initialPosition.add(2,this.vx);
        this.initialPosition.add(3,this.vy);


    }


    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    void toggleEnterPressed() {
        this.EnterPressed = true;
    }

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    void untoggleEnterPressed() { this.EnterPressed = false; }

    public void update() {
        if (this.UpPressed) {
            this.moveForwards();
        }
        if (this.DownPressed) {
            this.moveBackwards();
        }

        if (this.LeftPressed) {
            this.rotateLeft();
        }
        if (this.RightPressed) {
            this.rotateRight();
        }
        if (this.EnterPressed) {
            this.shoot();
        }


    }

    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }

    private void moveBackwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
         x -= vx;
         y -= vy;
        checkBorder(false);
    }

    private void moveForwards() {
        vx = (int) Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = (int) Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder(true);
    }

    private void createBullet(){
        this.bulletPresent = true;

        try {
            bulletimg = read(new File("Resources/Shell.gif"));

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }}

    private void shoot() {

        if(!this.bulletPresent)
        {
            this.createBullet();
         this.bullet = new tankgame.Bullet(x,y,vx,vy,angle,this.bulletimg,this.cd,this.tankPlayer);
         this.bullet.togglePressed();
        }

        else
        {
            this.bullet.update();
            this.bulletPresent = this.bullet.bulletPresent();
            if(!this.bulletPresent)
                this.untoggleEnterPressed();
        }

    }



    private void checkBorder(boolean forward) {

        this.position.add(0,this.x);
        this.position.add(1,this.y);
        this.position.add(2,this.vx);
        this.position.add(3,this.vy);

            cd.updateTankPosition(position,tankPlayer);



            if(this.cd.checkTank() || this.cd.checkWallBorder(this.tankPlayer))
            {
                if(forward)
                {x -= vx;
                y -= vy;}
                else
                {x += vx;
                    y += vy;}
            }



            if (this.cd.powerUpDetection(this.tankPlayer))
            {  this.health.increaseLife();}

    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }

    public int currentPositionx()
    {
        return this.x;
    }

    public ArrayList<Integer> currentPosition(){ return this.position;}

    public ArrayList<Integer> currentInitialPosition(){ return this.initialPosition;}

    public void getCollisionDetection(tankgame.CollisionDetection cdn)
    {
        this.cd = cdn;
    }



    void reduceHealth()
    {
        this.health.reduceHealth();
        // to reset the tank to its original position after life ends (only if the other tank is not present at its original position)
        if(this.health.isStart() && this.cd.checkTankInitial(this.tankPlayer))
        {
            this.x = this.xInitial;
            this.y = this.yInitial;
            this.vx = this.vxInitial;
            this.vy = this.vyInitial;
            this.angle = this.angleInitial;
            checkBorder(true);
        }
    }

    int getTankHealth()
    {
        return this.health.displayHealth();
    }

    int getTankLives()
    {
        return this.health.displayLives();
    }


    boolean isGameOver()
    {
        if(this.health.isEnd())
            return true;
        else
            return false;
    }

    void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
    }

    void drawImageBullet(Graphics g){this.bullet.drawImage(g);}

    boolean bullet()
    {
        return this.bulletPresent;
    }

}
