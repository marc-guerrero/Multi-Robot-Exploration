package multi.robot.exploration;

import java.util.Objects;
import java.util.Random;
import java.lang.Math;

public class Position
{
    private int x;
    private int y;

    public Position(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public Position(Position prevPos, int height, int width)
    {
        x = prevPos.x();
        y = prevPos.y();
        generateNewRandomPosition(width, height);
    }

    public int x()
    {
        return x;
    }

    public int y()
    {
        return y;
    }

    public void setPosition(Position pos)
    {
        x = pos.x();
        y = pos.y();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
        {
            return true;
        }

        if (!(obj instanceof Position))
        {
            return false;
        }

        Position pos = (Position)obj;

        if (x == pos.x() && y == pos.y())
        {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(x, y);
    }

    private void generateNewRandomPosition(int height, int width)
    {
        int xmin, xmax;
        int ymin, ymax;

        // ensures that the random move is selected from within grid bounds
        xmin = Math.max(x-1, 0);
        xmax = Math.min(x+1, height-1);

        ymin = Math.max(y-1, 0);
        ymax = Math.min(y+1, width-1);

        // selects random move within [xmin, xmax] and [ymin, ymax] (inclusive)
        Random rand = new Random();
        x = rand.nextInt((xmax - xmin) + 1) + xmin;
        y = rand.nextInt((ymax - ymin) + 1) + ymin;
    }
}
