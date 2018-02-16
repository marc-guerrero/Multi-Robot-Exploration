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

    void setState(State state)
    {
        this.state = state;
    }

    State getState()
    {
        return state;
    }

    boolean setFrontier()
    {
        // sets this cell as a frontier cell if it is currently unexplored
        if (state == State.FRONTIER)
        {
            return true;
        }

        if (state == State.UNEXPLORED)
        {
            state = State.FRONTIER;
            return true;
        }

        return false;
    }
}
