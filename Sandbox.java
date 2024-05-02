import java.util.ArrayList;
import java.awt.Color;

public class Sandbox {
    public static void main(String[] args) {
        final Color[] colors = {StdDraw.RED, StdDraw.ORANGE, StdDraw.YELLOW, StdDraw.GREEN, StdDraw.BLUE, StdDraw.MAGENTA, StdDraw.BLACK, StdDraw.CYAN, StdDraw.LIGHT_GRAY, StdDraw.PINK, StdDraw.PINK, StdDraw.YELLOW, StdDraw.DARK_GRAY};

        System.out.println("exampletext");
        Ball[] balls = new Ball[20];
        Square[] squares = new Square[20];
        for (int i = 0; i < balls.length; i++) {
            balls[i] = new Ball(new double[] { 1.8 * Math.random() - 0.9,1.8 * Math.random() - 0.9}, new double[] {(Math.random()-0.5)/10,(Math.random()-0.5)/10}, 0.08, 0.9, colors[(int)(Math.random() * colors.length)]);
        }
        for (int i = 0; i < squares.length; i++) {
            squares[i] = new Square(new double[] { 1.8 * Math.random() - 0.9,1.8 * Math.random() - 0.9}, new double[] {(Math.random()-0.5)/10,(Math.random()-0.5)/10}, 0.08, 0.9, colors[(int)(Math.random() * colors.length)]);
        }

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
                balls[i].draw();
            }
            for (int i = 0; i < squares.length-1; i++) {
                squares[i].bounceWall();
                
                for (int j = i+1; j < squares.length; j++) if (!squareCollisions.contains(new int[] {i,j}) && !squareCollisions.contains(new int[] {j,i})) {
                    if (squares[i].collide(squares[j])) squareCollisions.add(new int[] {i,j});
                }
                
                squares[i].move(); 
                squares[i].draw();
            }
            StdDraw.show();
            StdDraw.pause(20);
        }
    }
}
