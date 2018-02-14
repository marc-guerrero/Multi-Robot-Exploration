import java.util.Random;
import java.lang.Math;

public class Position
{
    private int x;
    private int y;

    Position(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    Position(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        generateRandomPosition(width, height);
    }

    public int x()
    {
        return this.x;
    }

    public int y()
    {
        return this.y;
    }

    public void setPosition(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    private void generateRandomPosition(int width, int height)
    {
        int xmin, xmax;
        int ymin, ymax;

        // ensures that the random move is selected from within grid bounds
        xmin = Math.max(x-1, 0);
        xmax = Math.min(x+1, width-1);

        ymin = Math.max(y-1, 0);
        ymax = Math.min(y+1, height-1);

        // selects random move within [xmin, xmax] and [ymin, ymax] (inclusive)
        Random rand = new Random();
        x = rand.nextInt((xmax - xmin) + 1) + xmin;
        y = rand.nextInt((ymax - ymin) + 1) + ymin;
    }
}
