package tankgame;

import java.awt.*;
import java.util.ArrayList;

public class CollisionDetection {

    private tankgame.Tank t1;
    private tankgame.Tank t2;

    private ArrayList<Integer> position1Initial;    // coordinates of tank 1 are stored
    private ArrayList<Integer> position2Initial;    // coordinates of tank 2 are stored
    private ArrayList<Integer> position1;    // coordinates of tank 1 are stored
    private ArrayList<Integer> position2;    // coordinates of tank 2 are stored
    private int[] bulletPosition = new int[2];
    private ArrayList<Integer> wallPosition = new ArrayList<>();
    private ArrayList<Integer> powerUpPosition = new ArrayList<>();
    private int[][] map;
    private tankgame.Map map1;

    public CollisionDetection(tankgame.Tank t1, tankgame.Tank t2, tankgame.Map map1) {
        this.t1 = t1;
        this.t2 = t2;

        this.position1Initial = t1.currentInitialPosition();
        this.position2Initial = t2.currentInitialPosition();
        this.position1 = t1.currentPosition();
        this.position2 = t2.currentPosition();

        this.map1 = map1;
        this.map = map1.getMap();

    }

   void updateTankPosition(ArrayList<Integer> position, int tankPlayer)
   {
       if(tankPlayer==1) this.position1 = position;
       else if(tankPlayer==2) this.position2 = position;
   }

        // to check if two tanks are colliding
    boolean checkTank()
    {
                if(((position1.get(0)>=position2.get(0)-50 && position1.get(0)<=position2.get(0)+50) && (position1.get(1)>=position2.get(1)-50 && position1.get(1)<=position2.get(1)+50)))
            return true;
        else
            return false;
    }

            // to check if other tank is present at present tank's initial position after life ends
    boolean checkTankInitial(int tankPlayer)
    {
       if(tankPlayer==2) {

            Rectangle tank1Rect = new Rectangle(position1.get(0),position1.get(1),50,50);
            Rectangle tank2Rect = new Rectangle(position2Initial.get(0),position2Initial.get(1),50,50);
            if(tank1Rect.intersects(tank2Rect))
                return false;
            else
                return true;
        }
        else
            {
                Rectangle tank1Rect = new Rectangle(position1Initial.get(0),position1Initial.get(1),50,50);
                Rectangle tank2Rect = new Rectangle(position2.get(0),position2.get(1),50,50);

                if(tank1Rect.intersects(tank2Rect))
            return false;
        else
            return true;
    }}


        // updating positions of walls and power ups
    void checkWall()
    {
        int i=0,j=0;

        while(i<30) {

            if (map[i][j] == 9 || map[i][j] == 3) {

                if (map[i][j] == 9)
                    this.wallPosition.add(0);
                else
                    this.wallPosition.add(1);

                this.wallPosition.add(j*32);
                this.wallPosition.add(i*32);
            }

            else if(map[i][j] == 5)
            {
                this.powerUpPosition.add(j*32);
                this.powerUpPosition.add(i*32);
               }

            j++;
            if (j > 39) {
                i++;
                j = 0;
            }
        }

    }

                // to check collision between tank and walls
    boolean checkWallBorder(int tank)
    {
        int i=1;
        boolean flag = false;
        Rectangle tank1Rect,tank2Rect,wallRect;
        tank1Rect = new Rectangle(position1.get(0),position1.get(1),50,50);
        tank2Rect = new Rectangle(position2.get(0),position2.get(1),50,50);

        while(i<wallPosition.size())
        {

            if(wallPosition.get(i-1)==2)
            {i=i+3; continue;}

            wallRect = new Rectangle(wallPosition.get(i),wallPosition.get(i+1),32,32);

            if(tank == 1) {
                 if(tank1Rect.intersects(wallRect))
                 {  flag = true;
                    break;
                }}

            if(tank == 2) {

                  if(tank2Rect.intersects(wallRect))
                  {
                    flag = true;
                    break;
                }}
                i = i + 3;

        }

        if(flag)
            return true;
        else
            return false;


    }

    void updateBulletPosition(int x, int y)
    {
        this.bulletPosition[0] = x;
        this.bulletPosition[1] = y;
    }

    boolean powerUpDetection(int tankPlayer)
    {
        int i=0;
        boolean flag=false;
        Rectangle tankRect,powerUpRect;

        if(tankPlayer==1) {
            tankRect = new Rectangle(position1.get(0),position1.get(1),50,50);
            while (i<this.powerUpPosition.size()){

                powerUpRect = new Rectangle(powerUpPosition.get(i),powerUpPosition.get(i+1),32,32);
                if(tankRect.intersects(powerUpRect))
                {  this.map1.setMap(powerUpPosition.get(i + 1) / 32, powerUpPosition.get(i) / 32);
            this.powerUpPosition.set(i,-100);
                flag = true; break;}
                i=i+2;
            }
        }
        else if(tankPlayer==2)
        {   tankRect = new Rectangle(position2.get(0),position2.get(1),50,50);

            while(i<this.powerUpPosition.size()){

                powerUpRect = new Rectangle(powerUpPosition.get(i),powerUpPosition.get(i+1),32,32);
                if(tankRect.intersects(powerUpRect))
            { this.map1.setMap(powerUpPosition.get(i + 1) / 32, powerUpPosition.get(i) / 32);
                this.powerUpPosition.set(i,-100);
            flag = true; break;}
                i=i+2;
            }
        }

        return flag;
    }
            // to check collision for bullets with tanks and unbreakable walls
    boolean checkBulletCollision(int tank)
    {

        int i=0;
        int breakable = 0;
        boolean flag = false;
        Rectangle tankRect, bulletRect, wallRect;
        bulletRect = new Rectangle(bulletPosition[0],bulletPosition[1],24,24);

        if(tank==2) {
            tankRect = new Rectangle(position1.get(0),position1.get(1),50,50);

        if(tankRect.intersects(bulletRect))
            { this.t1.reduceHealth(); return true;}
        }
        else if(tank==1)
        {
            tankRect = new Rectangle(position2.get(0),position2.get(1),50,50);
           if(tankRect.intersects(bulletRect))
            { this.t2.reduceHealth(); return true;}
        }


        while(i<wallPosition.size()) {

            if(wallPosition.get(i)==2)
            {i=i+3; continue;}

            wallRect = new Rectangle(wallPosition.get(i+1),wallPosition.get(i+2),32,32);

              if(bulletRect.intersects(wallRect))
                {
                    if(wallPosition.get(i) == 1)
                        breakable =1;
                    flag =true; break;
                }
                i = i + 3;


        }
            if(breakable==1) {
                this.map1.setMap(wallPosition.get(i + 2) / 32, wallPosition.get(i + 1) / 32);
                wallPosition.set(i, 2);
            }

            if(flag)
                return true;
            else
                return false;

        }

        int[][] returnMap()
        {
            return this.map1.getMap();
        }

}
