import java.util.ArrayList;
import java.awt.Color;

public class Sandbox {
    public static void main(String[] args) {
        final Color[] colors = { StdDraw.RED, StdDraw.ORANGE, StdDraw.YELLOW, StdDraw.GREEN, StdDraw.BLUE,
                StdDraw.MAGENTA, StdDraw.BLACK, StdDraw.CYAN, StdDraw.LIGHT_GRAY, StdDraw.PINK, StdDraw.PINK,
                StdDraw.YELLOW, StdDraw.DARK_GRAY };
        final int shapes = 15;
        double radius = 0;
        int selectedShape = 0;
        int hoveredOption = -1;
        double rotation = 0;
        double[] center = { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY };

        Ball[] balls = new Ball[0];
        Polygon[] polys = new Polygon[0];

        StdDraw.setXscale(-1.0, 1.0);
        StdDraw.setYscale(-1.0, 1.0);
        StdDraw.enableDoubleBuffering();

        while (true) {
            StdDraw.clear(StdDraw.WHITE);

            collide(balls, polys);

            double x = StdDraw.mouseX();
            double y = StdDraw.mouseY();

            if (StdDraw.isMousePressed()) {
                if (hoveredOption != -1 && (hoveredOption <= shapes || hoveredOption == 19))
                    selectedShape = hoveredOption;
                else {
                    if (center[0] == Double.POSITIVE_INFINITY)
                        center = new double[] { x, y };
                    else {
                        StdDraw.line(x, y, center[0], center[1]);
                        radius = Math.max(Math.sqrt(Math.pow(x - center[0], 2) + Math.pow(y - center[1], 2)), 0.02);
                        if (x - center[0] == 0)
                            rotation = Math.PI / 2 * Math.signum(y - center[0]);
                        else if (x - center[0] <= 0)
                            rotation = Math.PI + Math.atan((y - center[1]) / (x - center[0]));
                        else
                            rotation = Math.atan((y - center[1]) / (x - center[0]));
                    }
                    if (selectedShape == 0)
                    StdDraw.circle(center[0], center[1], radius);
                    else {
                        double[] xS = new double[selectedShape + 2];
                        double[] yS = new double[selectedShape + 2];
                        for (int j = 0; j < selectedShape + 2; j++) {
                            double theta = j * Math.PI * 2 / (selectedShape + 2) + rotation;
                            xS[j] = center[0] + radius * Math.cos(theta);
                            yS[j] = center[1] + radius * Math.sin(theta);
                        }
                        StdDraw.polygon(xS, yS);
                    }
                }
            } else if (radius > 0) {
                if (selectedShape != 0) polys = addPoly(radius, selectedShape, rotation, center, polys);
                else balls = addBall(radius, center, balls);

                radius = 0;
                center = new double[] { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY };
                rotation = 0;
            } else if (selectedShape == 19) {
                radius = 0;
                selectedShape = 0;
                hoveredOption = -1;
                rotation = 0;
                center = new double[] { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY };

                balls = new Ball[0];
                polys = new Polygon[0];
            } else {
                if (-1 < x && x < -0.9)
                    hoveredOption = (9 - (int) (10 * (y + 1)) + 10);
                else
                    hoveredOption = -1;
            }
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.filledRectangle(-0.95, 0, 0.05, 1);

            StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
            StdDraw.filledSquare(-0.95, 0.95 - selectedShape * 0.1, 0.05);

            StdDraw.setPenColor(new Color(100, 100, 100, 75));
            if (hoveredOption != -1 && (hoveredOption <= shapes || hoveredOption == 19))
                StdDraw.filledSquare(-0.95, 0.95 - hoveredOption * 0.1, 0.05);

            StdDraw.setPenColor();
            StdDraw.rectangle(-0.95, 0, 0.05, 1);
            StdDraw.circle(-0.95, 0.95, 0.04);
            for (int i = 3; i < 3 + shapes; i++) {
                double[] xS = new double[i];
                double[] yS = new double[i];
                for (int j = 0; j < i; j++) {
                    double theta = j * Math.PI * 2 / i + Math.PI / 2;
                    xS[j] = -0.95 + 0.04 * Math.cos(theta);
                    yS[j] = 0.95 - 0.1 * (i - 2) + 0.04 * Math.sin(theta);
                }
                StdDraw.polygon(xS, yS);
            }
            StdDraw.picture(-0.95, -0.95, "exit.png", 0.1, 0.1);
            StdDraw.show();
            StdDraw.pause(20);
        }
    }

    private static Polygon[] addPoly(double radius, int selectedShape, double rotation, double[] center, Polygon[] polys) {
        final Color[] colors = { StdDraw.RED, StdDraw.ORANGE, StdDraw.YELLOW, StdDraw.GREEN, StdDraw.BLUE,
                StdDraw.MAGENTA, StdDraw.BLACK, StdDraw.CYAN, StdDraw.LIGHT_GRAY, StdDraw.PINK, StdDraw.PINK,
                StdDraw.YELLOW, StdDraw.DARK_GRAY };

        Polygon[] newPolys = new Polygon[polys.length + 1];
        for (int i = 0; i < polys.length; i++)
            newPolys[i] = polys[i];
        newPolys[newPolys.length - 1] = new Polygon(center, new double[] { 0, 0 }, radius, 0.7,
                colors[(int) (Math.random() * colors.length)], selectedShape + 2, rotation);
        polys = newPolys;
        return polys;
    }

    private static Ball[] addBall(double radius, double[] center, Ball[] balls) {
        final Color[] colors = { StdDraw.RED, StdDraw.ORANGE, StdDraw.YELLOW, StdDraw.GREEN, StdDraw.BLUE,
                StdDraw.MAGENTA, StdDraw.BLACK, StdDraw.CYAN, StdDraw.LIGHT_GRAY, StdDraw.PINK, StdDraw.PINK,
                StdDraw.YELLOW, StdDraw.DARK_GRAY };

        Ball[] newBalls = new Ball[balls.length + 1];
        for (int i = 0; i < balls.length; i++)
            newBalls[i] = balls[i];
        newBalls[newBalls.length - 1] = new Ball(center, new double[] { 0, 0 }, radius, 0.88,
                colors[(int) (Math.random() * colors.length)]);
        balls = newBalls;
        return balls;
    }

    public static void collide(Ball[] balls, Polygon[] polys) {
        ArrayList<int[]> ballCollisions = new ArrayList<int[]>();
        ArrayList<int[]> polyCollisions = new ArrayList<int[]>();
        ArrayList<int[]> bpCollisions = new ArrayList<int[]>();

        for (int i = 0; i < balls.length; i++) {
            balls[i].bounceWall();

            for (int j = i + 1; j < balls.length; j++)
                if (!ballCollisions.contains(new int[] { i, j }) && !ballCollisions.contains(new int[] { j, i })) {
                    if (balls[i].collide(balls[j]))
                        ballCollisions.add(new int[] { i, j });
                }
            for (int j = 0; j < polys.length; j++)
                if (!bpCollisions.contains(new int[] { i, j }))
                    if (balls[i].collide(polys[j]))
                        bpCollisions.add(new int[] { i, j });
            balls[i].move();
        }

        for (int i = 0; i < polys.length; i++) {
            polys[i].bounceWall();
            for (int j = 0; j < polys.length; j++)
                if (j != i)
                    if (!polyCollisions.contains(new int[] { i, j }) && !polyCollisions.contains(new int[] { j, i })) {
                        if (polys[i].collide(polys[j]))
                            polyCollisions.add(new int[] { i, j });
                    }
            polys[i].move();
            polys[i].updatePoints();
            polys[i].draw();
        }
        for (int i = 0; i < balls.length; i++)
            balls[i].draw();
    }

    public static Ball[] addBall(Ball[] balls, double[] center, double radius) {
        return balls;
    }
}
