package tankgame;

public class Health {

    private int health;
    private int lives;
    private boolean start;
    private boolean end;

    Health()
    {
         this.health = 100;
        this.lives = 3;
    }

    int displayHealth()
    {
        return this.health;
    }

    int displayLives()
    {
        return this.lives;
    }

    void reduceHealth()
    {
        this.health = this.health -10;
        if(this.health == 0)
        {
            if(this.lives>0)
            {lives =lives -1;
            this.health=100;
            this.start =true;}
            else
                this.end = true;
        }
    }

    void increaseLife()
    {
        this.lives += 1;
    }

    boolean isEnd()
    {
        return this.end;
    }

    boolean isStart()
    {
        if(this.start)
        {
            this.start = false;
            return true;
        }
        else
            return false;
    }

}
