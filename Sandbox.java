import java.util.ArrayList;
import java.awt.Color;

public class Sandbox {
    public static void main(String[] args) {
        final Color[] colors = { StdDraw.RED, StdDraw.ORANGE, StdDraw.YELLOW, StdDraw.GREEN, StdDraw.BLUE,
                StdDraw.MAGENTA, StdDraw.BLACK, StdDraw.CYAN, StdDraw.LIGHT_GRAY, StdDraw.PINK, StdDraw.PINK,
                StdDraw.YELLOW, StdDraw.DARK_GRAY };
        final int shapes = 15;
        double radius = 0;
        int zapTimer = 0;
        double[][] zapLine = {{Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY},{Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY}};
        int menuOption = 0;
        int hoveredOption = -1;
        double rotation = 0;
        double[] center = { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY };
        double[] zapCenter = {Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY};

        Ball[] balls = new Ball[0];
        Polygon[] polys = new Polygon[0];

        StdDraw.setXscale(-1.0, 1.0);
        StdDraw.setYscale(-1.0, 1.0);
        StdDraw.enableDoubleBuffering();

        while (true) {
            StdDraw.clear(StdDraw.WHITE);

            update(balls, polys);

            double x = StdDraw.mouseX();
            double y = StdDraw.mouseY();
            
            zapTimer--;

            if (StdDraw.isMousePressed()) {

                // UI/select item
                if (hoveredOption != -1 && (hoveredOption <= shapes || hoveredOption == 19 || hoveredOption == 18)) menuOption = hoveredOption;
                
                else {
                    
                    // create center
                    if (center[0] == Double.POSITIVE_INFINITY) center = new double[] { x, y };

                    // update stuff if shape alr being built
                    else if (menuOption <= shapes) {
                        StdDraw.line(x, y, center[0], center[1]);
                        radius = Math.max(Math.sqrt(Math.pow(x - center[0], 2) + Math.pow(y - center[1], 2)), 0.02);

                        if (x - center[0] == 0) rotation = Math.PI / 2 * Math.signum(y - center[0]);
                        else if (x - center[0] < 0) rotation = Math.PI + Math.atan((y - center[1]) / (x - center[0]));
                        else rotation = Math.atan((y - center[1]) / (x - center[0]));
                    }

                    if (menuOption == 0) StdDraw.circle(center[0], center[1], radius);
                    else if (menuOption <= shapes) previewPolygon(radius, menuOption, rotation, center);

                    // zap if its like that
                    else if (menuOption == 18) {
                        if (zapTimer < 0) {
                            int add = 0;
                            int[] toZap = zap(balls, polys);
                            if (toZap[0] == 0 && polys.length >= 1) {
                                for (int i = 0; i < polys[toZap[1]].getPoints().length/2; i++) StdAudio.playInBackground("bang.wav");
                                zapCenter = polys[toZap[1]].getPos();
                                Polygon[] newPolys = new Polygon[polys.length - 1];
                                for (int i = 0; i < polys.length; i++) {
                                    if (i != toZap[1])
                                        newPolys[i + add] = polys[i];
                                    else
                                        add = -1;
                                }
                                polys = newPolys;
                            } else if (balls.length >= 1) {
                                StdAudio.playInBackground("bang.wav");
                                zapCenter = balls[toZap[1]].getPos();
                                Ball[] newBalls = new Ball[balls.length - 1];
                                for (int i = 0; i < balls.length; i++) {
                                    if (i != toZap[1])
                                        newBalls[i + add] = balls[i];
                                    else add = -1;
                                }
                                balls = newBalls;
                            }
                            else {
                                zapCenter = new double[] {Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY};
                                zapLine = new double[][] {{Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY},zapCenter};
                            }
                            zapTimer = 10;
                            zapLine = new double[][] {new double[] {x,y}, zapCenter};
                            
                            StdDraw.setPenColor(StdDraw.RED);
                            if (zapLine[0][0] < 1 && zapLine[1][0] < 1 && zapTimer > 0) {
                                StdDraw.line(zapLine[0][0], zapLine[0][1], zapLine[1][0], zapLine[1][1]);
                            }
                        }
                        else {
                            zapCenter = new double[] {Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY};
                            zapLine = new double[][] {{Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY},zapCenter};
                        }
                    }
                    // end zap
                }
            }
            // letting go
            else if (radius > 0) { // create shape
                if (menuOption == 0)            balls = addBall(radius,                       center, balls);
                else if (menuOption <= shapes)  polys = addPoly(radius, menuOption, rotation, center, polys);

                radius = 0;
                center = new double[] { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY };
                rotation = 0;
            }
            else if (menuOption == 19) { // delete all
                radius = 0;
                menuOption = 0;
                hoveredOption = -1;
                rotation = 0;
                center = new double[] { Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY };

                balls = new Ball[0];
                polys = new Polygon[0];
            }
            else { // reset option
                if (-1 < x && x < -0.9) hoveredOption = (9 - (int) (10 * (y + 1)) + 10);
                else hoveredOption = -1;
            }

            drawUI(shapes, menuOption, hoveredOption);
            StdDraw.show();
            StdDraw.pause(20);
        }
    }

    private static void previewPolygon(double radius, int selectedShape, double rotation, double[] center) {
        double[] xS = new double[selectedShape + 2];
        double[] yS = new double[selectedShape + 2];
        for (int j = 0; j < selectedShape + 2; j++) {
            double theta = j * Math.PI * 2 / (selectedShape + 2) + rotation;
            xS[j] = center[0] + radius * Math.cos(theta);
            yS[j] = center[1] + radius * Math.sin(theta);
        }
        StdDraw.polygon(xS, yS);
    }

    private static void drawUI(final int shapes, int selectedShape, int hoveredOption) {
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.filledRectangle(-0.95, 0, 0.05, 1);

        StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
        StdDraw.filledSquare(-0.95, 0.95 - selectedShape * 0.1, 0.05);

        StdDraw.setPenColor(new Color(100, 100, 100, 75));
        if (hoveredOption != -1 && (hoveredOption <= shapes || hoveredOption == 19 || hoveredOption==18)) StdDraw.filledSquare(-0.95, 0.95 - hoveredOption * 0.1, 0.05);

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
        StdDraw.picture(-0.95, -0.95, "reload.png", 0.1, 0.1);
        StdDraw.picture(-0.95, -0.85, "exit.png", 0.1, 0.1);
    }

    private static Polygon[] addPoly(double radius, int selectedShape, double rotation, double[] center,
            Polygon[] polys) {
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

    public static void update(Ball[] balls, Polygon[] polys) {
        ArrayList<int[]> ballCollisions = new ArrayList<int[]>();
        ArrayList<int[]> polyCollisions = new ArrayList<int[]>();
        ArrayList<int[]> bpCollisions = new ArrayList<int[]>();

        for (int i = 0; i < balls.length; i++) {
            balls[i].bounceWall();
            for (int j = i+1; j < balls.length; j++) if (!ballCollisions.contains(new int[] { i, j })) if (balls[i].collide(balls[j])) ballCollisions.add(new int[] { i, j });
            for (int j = 0  ; j < polys.length; j++) if (!  bpCollisions.contains(new int[] { i, j })) if (balls[i].collide(polys[j])) bpCollisions  .add(new int[] { i, j });
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
        for (int i = 0; i < balls.length; i++) balls[i].draw();
    }

    public static int[] zap(Ball[] balls, Polygon[] polys) {
        double x = StdDraw.mouseX();
        double y = StdDraw.mouseY();
        double dist = Double.POSITIVE_INFINITY;
        int closeBall = 0;
        double closeBallDist = Double.POSITIVE_INFINITY;
        int closePoly = 0;
        double closePolyDist = Double.POSITIVE_INFINITY;
        for (int i = 0; i < balls.length; i++) {
            dist = Math.sqrt(Math.pow(balls[i].getPos()[0] - x, 2) + Math.pow(balls[i].getPos()[1] - y, 2));
            if (dist < closeBallDist) {
                closeBallDist = dist;
                closeBall = i;
            }
        }
        for (int i = 0; i < polys.length; i++) {
            double[] closePt = polys[i].closestPoint(new double[] { x, y }, 0.05);
            dist = Math.sqrt(Math.pow(closePt[0] - x, 2) + Math.pow(closePt[1] - y, 2));
            if (dist < closePolyDist) {
                closePolyDist = dist;
                closePoly = i;
            }
        }
        if (closePolyDist < closeBallDist) return new int[] { 0, closePoly };
        else return new int[] { 1, closeBall };
    }

}
