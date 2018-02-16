package multi.robot.exploration;

enum State {OBSTACLE, UNEXPLORED, EXPLORED, FRONTIER, OCCUPIED;}

public class Cell
{
    private int x, y;
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
        return this.state;
    }

    void setFrontier()
    {
        // sets this cell as a frontier cell if it is currently unexplored
        if (state == State.UNEXPLORED)
        {
            state = State.FRONTIER;
        }
    }
}
