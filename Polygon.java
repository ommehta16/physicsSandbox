import java.awt.Color;

public class Polygon {
    private double[] position;
    private double length;
    private double rotation;
    private double elasticness;
    private double gravity;
    private double[] velocity;
    private int sides;
    private double[][] points;
    public Color color;

    final Color[] colors = { StdDraw.RED, StdDraw.ORANGE, StdDraw.YELLOW, StdDraw.GREEN, StdDraw.BLUE, StdDraw.MAGENTA,
            StdDraw.BLACK, StdDraw.CYAN, StdDraw.LIGHT_GRAY, StdDraw.PINK, StdDraw.PINK, StdDraw.YELLOW,
            StdDraw.DARK_GRAY };

    public Polygon(double[] pos, double[] vel, double len, double elastic, Color col, int sid, double rot) {
        position = pos;
        velocity = vel;
        length = len;
        elasticness = elastic;
        color = col;
        sides = Math.abs(sid);
        points = new double[sides][2];
        rotation = rot;
        gravity = 0.001;
    }

    public void move() {
        position[0] += velocity[0];
        position[1] += velocity[1];
        velocity[1] -= gravity;
    }

    public void updatePoints() {
        for (int i = 0; i < points.length; i++) {
            double theta = rotation + i * Math.PI * 2 / sides;
            points[i] = new double[] {position[0]+length*Math.cos(theta),position[1]+length*Math.sin(theta)};
        }
    }

    public void bounceWall() {
        for (double[] point : points) {
            if (point[0] <= -0.9) {
                velocity[0] *= -elasticness;
                position[0] = (-0.9 + Math.abs(point[0]-position[0]) + position[0]) / 2;
            }
            else if (point[0] >= 1) {
                velocity[0] *= -elasticness;
                position[0] = (1 - Math.abs(point[0]-position[0]) +  position[0]) / 2;
            }
            else if (point[1] <= -1) {
                velocity[1] *= -elasticness;
                position[1] = (-1 + Math.abs(point[1]-position[1]) + position[1]) / 2;
            }
            else if (point[1] >= 1) {
                velocity[1] *= -elasticness;
                position[1] = (1 - Math.abs(point[1]-position[1]) + position[1]) / 2;
            }
            else continue;
            //StdAudio.playInBackground("boing.wav");
            break;
        }
        
    }
    public boolean containsPoint(double[] point) {
        double xT = point[0];
        double yT = point[1];
        boolean contained = false;
        for (int i = 0; i < points.length; i++) {
            double x1 = points[i][0];
            double x2 = points[(i+1) % points.length][0];
            double y1 = points[i][1];
            double y2 = points[(i+1) % points.length][1];
            double m = (y2-y1)/(x2-x1);
            double b = y1-m*x1;
            double xI = (yT-b)/m;
            if (x2 == x1) xI = x1;
            
            if ((xI <= xT) && ((x2 > xI && xI > x1) || (x1 > xI && xI > x2)) && ((y2 > yT && yT > y1) || (y1 > yT && yT > y2))) contained = !contained;
            /*
            points[i] --> points[i+1]
            x1 = points[i][0]
            x2 = points[i+1][0]
            y1 = points[i][1]
            y2 = points[i+1][1]
            yT = y of test point
            xT = x of test point
            
            if (x2 == x1) intersection = (x1,yT)
            m = (y2-y1)/(x2-x1)
            y1-m * x1 = b

            (yT-b)/m = xI

            intersection = (xI,yT)

            if x1 < xI < x2 and y1 < yT < y2 : we're bing chiling

            y=mx+b
            */
        }
        
        return contained;
    }

    // TODO: make them collide better (more like polygons than circles)
    public boolean collide(Polygon other) {
        double[][] otherPoints = other.getPoints();
        int otherSides = points.length;
        double[] dists = new double[otherSides+1];
        dists[otherSides] = Double.POSITIVE_INFINITY;
        
        for (int i = 0; i < otherPoints.length; i++) {
            if (!containsPoint(otherPoints[i])) continue;
            double velMag = Math.sqrt(Math.pow(velocity[0], 2) + Math.pow(velocity[1], 2));
            double othMag = Math.sqrt(Math.pow(other.velocity[0], 2) + Math.pow(other.velocity[1], 2));

            double dist = Math.sqrt(Math.pow(position[0]-other.position[0],2) + Math.pow(position[1]-other.position[1],2));
            double diffX = position[0] - other.position[0];
            double diffY = position[1] - other.position[1];

            //if (velMag > 0.007) StdAudio.playInBackground("boing.wav");

            velocity = new double[] { elasticness * velMag * diffX / dist, elasticness * velMag * diffY / dist };
            other.velocity = new double[] { -other.elasticness * othMag * diffX / dist, -other.elasticness * othMag * diffY / dist };
            return true;
        }
        
        return false;
    }

    public void draw() {
        StdDraw.setPenColor(color);
        double[] xS = new double[sides];
        double[] yS = new double[sides];
        for (int i = 0; i < sides; i++) {
            xS[i] = points[i][0];
            yS[i] = points[i][1];
        }
        StdDraw.filledPolygon(xS, yS);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.polygon(xS, yS);
    }

    public double[] getPos() {
        return position;
    }

    public void setPos(double[] pos) {
        position = pos;
    }

    public void setPoints(double[][] pts) {
        points = pts;
    }

    public double[][] getPoints() {
        return points;
    }

    public double[] getVel() {
        return velocity;
    }

    public void setVel(double[] vel) {
        velocity = vel;
    }

    public double getLen() {
        return length;
    }

    public void setLen(double len) {
        length = len;
    }

    public double getElastic() {
        return elasticness;
    }

    public void setElastic(double elastic) {
        elasticness = elastic;
    }

}
