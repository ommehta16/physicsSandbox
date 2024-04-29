public class Sandbox {
    public static void main(String[] args) {
        System.out.println("exampletext");
        Ball ball1 = new Ball();
        Ball ball2 = new Ball(new double[] {0,0.5}, new double[] {0,0}, 0.1, 0.9);
        StdDraw.setXscale(-1.0, 1.0);
        StdDraw.setYscale(-1.0, 1.0);
        StdDraw.enableDoubleBuffering();
        

        while (true) {
            StdDraw.clear(StdDraw.WHITE);
            ball1.move();
            ball2.move();
            ball1.bounceWall();
            ball2.bounceWall();
            ball1.collide(ball2);
            StdDraw.filledCircle(ball1.getPos()[0], ball1.getPos()[1], ball1.getRad());
            StdDraw.filledCircle(ball2.getPos()[0], ball2.getPos()[1], ball2.getRad());
            StdDraw.show();
            StdDraw.pause(20);
        }
    }
}
