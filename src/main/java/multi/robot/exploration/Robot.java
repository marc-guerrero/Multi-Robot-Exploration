package multi.robot.exploration;

public class Robot
{
    private Position position;

    public Robot(Position pos)
    {
        setPosition(pos);
    }

    public void setPosition(Position pos)
    {
        position = pos;
    }

    public Position getPosition()
    {
        return position;
    }
}
