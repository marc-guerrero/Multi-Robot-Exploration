package multi.robot.exploration;

enum State {OBSTACLE, UNEXPLORED, EXPLORED, FRONTIER, OCCUPIED;}

public class Cell
{
    private int x;
    private int y;
    private State state;

    public Cell(int x, int y)
    {
        this.x = x;
        this.y = y;
        this.state = State.UNEXPLORED;
    }

    public void setState(State state)
    {
        this.state = state;
    }

    public State getState()
    {
        return state;
    }

    public boolean setFrontier()
    {
        if (state == State.FRONTIER)
        {
            return true;
        }

        // sets this cell as a frontier cell if currently unexplored
        if (state == State.UNEXPLORED)
        {
            state = State.FRONTIER;
            return true;
        }

        return false;
    }
}
