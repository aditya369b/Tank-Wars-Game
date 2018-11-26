 package tankgame;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet {

        private int x;
        private int y;
        private int vx;
        private int vy;
        private int angle;

        private final int R = 2;



        private BufferedImage img;
        private boolean pressed;
        private tankgame.CollisionDetection cd;
        private int tank;



        Bullet(int x, int y, int vx, int vy, int angle, BufferedImage img, tankgame.CollisionDetection cd,int tank) {
            this.x = x+16;
            this.y = y+16;
            this.vx = vx;
            this.vy = vy;
            this.img = img;
            this.angle = angle;
            this.cd =cd;
            this.tank = tank;
        }
               public void togglePressed() {
            this.pressed = true;
        }



        public void update() {
            if (this.pressed) {
                this.moveForwards();
            }
        }


        private void moveForwards() {
            vx = (int) Math.round(2 * R * Math.cos(Math.toRadians(angle)));
            vy = (int) Math.round(2 * R * Math.sin(Math.toRadians(angle)));
            x += vx;
            y += vy;
            checkBorder();
        }




        private void checkBorder() {

            cd.updateBulletPosition(x,y);

            if(cd.checkBulletCollision(this.tank))
            { this.pressed = false;}


        }

        public boolean bulletPresent()
        {
            return this.pressed;
        }


    void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;

        if(this.pressed)
        g2d.drawImage(this.img, rotation, null);

    }



    }