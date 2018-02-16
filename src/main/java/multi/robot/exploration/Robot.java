package multi.robot.exploration;

public class Robot
{
    private int x;
    private int y;

    Robot(int x, int y)
    {
        setPosition(x, y);
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
}
