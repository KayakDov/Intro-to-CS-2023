package introtocsHW1;

import java.util.LinkedList;

/**
 * A solution to the ball problem in the assignment.
 * @author Dov Neimand
 */
public class PartialSolution {

    /**
     * Reflects x around the axis.
     *
     * @param axis The axis x is reflected around.
     * @param x The value to be reflected.
     * @return The reflection value.
     */
    private static double reflection(double axis, double x) {
        return 2 * axis - x;
    }

    /**
     * Moves the value by the velocity within the bounds.
     *
     * @param upperBound The upper bound that the value can achieve.
     * @param lowerBound The lower bound the value can attain.
     * @param loc The value before movement.
     * @param vel The velocity of the value.
     * @return The new value within the bounds.
     */
    private static double move(double upperBound, double lowerBound, double loc,
            double vel) {
        loc += vel;
        if (loc < lowerBound) return reflection(lowerBound, loc);
        if (loc > upperBound) return reflection(upperBound, loc);
        return loc;
    }

    /**
     * This method determines if the velocity needs to be changed.
     *
     * @param upperBound If the value is outside the upper bound, then the
     * velocity needs to be changed.
     * @param lowerBound If the value is outside the lower bound then the
     * velocity needs to be changed.
     * @param loc The location.
     * @return -1 if the velocity needs to be changed and 1 if it doesn't.
     */
    private static int changeDir(double upperBound, double lowerBound,
            double loc) {
        
        return loc < lowerBound - 1e-5 || loc > upperBound  + 1e-5? -1: 1;
    }

    /**
     * A ball moves an increment in a box.
     *
     * @param xLoc The x-locations of the ball.
     * @param yLoc The y-location of the ball.
     * @param xVel The x velocity of the ball.
     * @param yVel The y-velocity of the ball.
     * @param rightWall The x value of the right wall.
     * @param cieling The y value of the top wall.
     * @param diameter The diameter of the ball.
     * @return The balls new location and velocity values [xLoc, yLoc, xVel,
     * yVel]
     */
    public static Ball ballInBox(
            double xLoc, double yLoc, double xVel,
            double yVel, final int rightWall,
            final int cieling, final double diameter) {

        double r = diameter/2;

        xLoc = move(rightWall - r, r, xLoc, xVel);
        yLoc = move(cieling - r, r, yLoc, yVel);
        
        xVel *= changeDir(rightWall - r, r, xLoc + xVel);
        yVel *= changeDir(cieling - r, r, yLoc + yVel);
        

        return new Ball(diameter, xLoc, yLoc, xVel, yVel);
    }

    /**
     * This should let you watch the ball bounce around the box using the code
     * you wrote. It's a great tool to test your code, and see how simple code
     * you write, can play a greater roll in a group project.
     */
    public static void watchBall() {

        final int diameter = 30, cieling = 400, rightWall = 600;

        Vec2d initLoc = new Vec2d(40, 50), initVel = new Vec2d(2, 1.5);
        
        new Graphic("Ball in Box", 
                new MovingPicture("ball.jpg", 
                        0, diameter,
                diameter, initLoc) {
             private Vec2d vel = initVel;
                    
            @Override
            public void update() {
                Ball ball = ballInBox(
                        loc.x, 
                        loc.y, 
                        vel.x, 
                        vel.y, 
                        rightWall, 
                        cieling, 
                        diameter);
                loc = ball.loc;
                vel = ball.vel;
            }
            
        });
        
    }

    /**
     * The sum of all the elements in the list.
     * @param list The list of elements whose sum is desired.
     * @return The sum of all the elements in the list.
     */
    private static int sum(LinkedList<Integer> list){
        return list.stream().mapToInt(i -> i).sum();
    }
    
    public static int numCells(int numGenerations, int initialNumberOfCells) {
        
        final int LIFE_SPAN = 2;
        
        LinkedList<Integer> recent = new LinkedList<>();
        
        recent.add(initialNumberOfCells);
        
        while(recent.size() < LIFE_SPAN && recent.size() < numGenerations)
            recent.add(sum(recent));
        
        for(int gen = LIFE_SPAN; gen < numGenerations; gen++){
            recent.add(sum(recent));
            recent.removeFirst();
        }
        
        return sum(recent);
    }
    
    
    
    
    public static void main(String[] args) {
        watchBall();
        for(int i = 1; i < 15; i++)
            System.out.println(i + ":" + numCells(i, 1));
    }
}
