package multi.robot.exploration;

public class Main
{
    public static void main(String[] args)
    {
        // TODO: Handle file input.
        int t = 1000;
        Position initLoc[] = {new Position(0,9), new Position(1,9), new Position(2,9)};
        Position obstacles[] = {new Position(0,0), new Position(1,0), new Position(2,2),
                                new Position(3,3), new Position(4,4), new Position(5,5),
                                new Position(6,6), new Position(7,7), new Position(8,8),
                                new Position(9,9)};

        RobotExploration algo = new RobotExploration(10, 10, 10, 3, initLoc, obstacles);

        // run algorithm for t (time) steps
        for (int i = 0; i < t; ++i)
        {
            if (!algo.runInteration())
            {
                System.out.println(i + " iterations");
                return;
            }
        }
    }
}
