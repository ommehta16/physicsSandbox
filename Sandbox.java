import java.util.ArrayList;
import java.awt.Color;

public class Sandbox {
    public static void main(String[] args) {
        final Color[] colors = {StdDraw.RED, StdDraw.ORANGE, StdDraw.YELLOW, StdDraw.GREEN, StdDraw.BLUE, StdDraw.MAGENTA, StdDraw.BLACK, StdDraw.CYAN, StdDraw.LIGHT_GRAY, StdDraw.PINK, StdDraw.PINK, StdDraw.YELLOW, StdDraw.DARK_GRAY};
        double radius = 0;
        boolean makeSquare = false;

        System.out.println("exampletext");
        
        Ball[] balls = new Ball[0];
        Square[] squares = new Square[0];

        StdDraw.setXscale(-1.0, 1.0);
        StdDraw.setYscale(-1.0, 1.0);
        StdDraw.enableDoubleBuffering();
        
        while (true) {
            StdDraw.clear(StdDraw.WHITE);

            ArrayList<int[]> ballCollisions = new ArrayList<int[]>();
            ArrayList<int[]> squareCollisions = new ArrayList<int[]>();
            ArrayList<int[]> bsCollisions = new ArrayList<int[]>();
            
            for (int i = 0; i < balls.length; i++) {
                balls[i].bounceWall();
                
                for (int j = i+1; j < balls.length; j++) if (!ballCollisions.contains(new int[] {i,j}) && !ballCollisions.contains(new int[] {j,i})) {
                    if (balls[i].collide(balls[j])) ballCollisions.add(new int[] {i,j});
                }
                for(int j = 0; j < squares.length; j++) if (!bsCollisions.contains(new int[] {i,j}))
                if (balls[i].collide(squares[j])) bsCollisions.add(new int[] {i,j});
                balls[i].move(); 
            }
            
            for (int i = 0; i < squares.length; i++) {
                squares[i].bounceWall();
                
                for (int j = i+1; j < squares.length; j++) if (!squareCollisions.contains(new int[] {i,j}) && !squareCollisions.contains(new int[] {j,i})) {
                    if (squares[i].collide(squares[j])) squareCollisions.add(new int[] {i,j});
                }
                
                squares[i].move(); 
                squares[i].draw();
            }
            for (int i = 0; i < balls.length; i++) balls[i].draw();
            
            double x = StdDraw.mouseX();
            double y = StdDraw.mouseY();
            
            if (StdDraw.isMousePressed()) {
                if (-1 < x && x < -0.9 && 1 > y && 0.9 < y) makeSquare = true;
                radius += 0.005;
                if (!makeSquare) StdDraw.circle(StdDraw.mouseX(),StdDraw.mouseY(),radius);
                else StdDraw.square(StdDraw.mouseX(),StdDraw.mouseY(),radius);
            }
            else if (radius > 0) {
                if (makeSquare) {
                    Square[] newSquares = new Square[squares.length + 1];
                    for (int i = 0; i < squares.length; i++) newSquares[i] = squares[i];
                    newSquares[newSquares.length-1] = new Square(new double[] {x,y}, new double[] {0,0}, radius, 0.7, colors[(int)(Math.random() * colors.length)]);
                    squares = newSquares;
                }
                else {
                    Ball[] newBalls = new Ball[balls.length + 1];
                    for (int i = 0; i < balls.length; i++) newBalls[i] = balls[i];
                    newBalls[newBalls.length-1] = new Ball(new double[] {x,y}, new double[] {0,0}, radius, 0.7, colors[(int)(Math.random() * colors.length)]);
                    balls = newBalls;
                }
                radius = 0;
                makeSquare = false;
            }
            else radius = 0;

            StdDraw.square(-0.95, 0.95, 0.05);
            StdDraw.show();
            StdDraw.pause(20);
        }
    }
}
