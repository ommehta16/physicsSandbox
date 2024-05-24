import java.awt.Color;
import java.util.ArrayList;

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
    public boolean sound;

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
        sound = false;
        updatePoints();
    }

    public void playBoing() {
        if (sound) StdAudio.playInBackground("boing.wav");
    }

    public void move() {
        position[0] += velocity[0];
        position[1] += velocity[1];
        velocity[1] -= gravity;
    }

    public void updatePoints() {
        for (int i = 0; i < points.length; i++) {
            double theta = rotation + i * Math.PI * 2 / sides;
            points[i] = new double[] { position[0] + length * Math.cos(theta), position[1] + length * Math.sin(theta) };
        }
    }

    public void bounceWall() {
        double bot = points[0][1];
        double top = points[0][1];
        double lef = points[0][0];
        double rit = points[0][0];
        
        for (int i = 0; i < points.length; i++) {
            if (points[i][0] < lef) lef = points[i][0];
            if (points[i][0] > rit) rit = points[i][0];
            if (points[i][1] < bot) bot = points[i][1];
            if (points[i][1] > top) top = points[i][1];
        }
        if (lef < -0.9) {
            velocity[0] *= -elasticness;
            position[0] = (-0.9+0.01 + Math.abs(lef-position[0]) + position[0])/2;
            if (Math.abs(velocity[0]) > 0.007) playBoing();
        } else if (rit > 1) {
            velocity[0] *= -elasticness;
            position[0] = (1-0.01 - Math.abs(rit-position[0]) + position[0])/2;
            if (Math.abs(velocity[0]) > 0.007) playBoing();
        } if (bot < -1) {
            velocity[1] *= -elasticness;
            position[1] = (-1+0.01 + Math.abs(bot - position[1]) + position[1])/2;
            if (Math.abs(velocity[1]) > 0.007) playBoing();
        } else if (top > 1) {
            velocity[1] *= -elasticness;
            position[1] = (1-0.01 - Math.abs(top - position[1]) + position[1])/2;
            if (Math.abs(velocity[1]) > 0.007) playBoing();
        } 

    }

    public double[] closestPoint(double[] point, double error) {
        double lowestDist = Double.POSITIVE_INFINITY;
        double[] closePoint = points[0];
        for (int i = 0; i < points.length; i++) {
            double x1 = points[i][0];
            double y1 = points[i][1];
            double x2 = points[(i+1) % sides][0];
            double y2 = points[(i+1) % sides][1];
            while (Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2)) > error) {
                double dist1 = Math.sqrt(Math.pow(x1 - position[0], 2) + Math.pow(y1 - position[1], 2));
                double dist2 = Math.sqrt(Math.pow(x2 - position[0], 2) + Math.pow(y2 - position[1], 2));

                if (dist1 < dist2) {
                    x2 = (x1 + x2) / 2;
                    y2 = (y1 + y2) / 2;
                } else {
                    x1 = (x1 + x2) / 2;
                    y1 = (y1 + y2) / 2;
                }
            }
            double x = (x1 + x2) / 2;
            double y = (y1 + y2) / 2;
            double dist = Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
            if (dist < lowestDist) {
                lowestDist = dist;
                closePoint = new double[] {x,y};
            }
        }
        return closePoint;
    }
    public double[] intersectLine(double[] start, double[] end) {
        double xI, yI, xT1,xT2,yT1,yT2,mT,bT;
        xI = Double.POSITIVE_INFINITY;
        yI = Double.POSITIVE_INFINITY;
        xT1 = start[0];
        xT2 = end[0];
        yT1 = start[1];
        yT2 = end[1];

        mT = (yT2-yT1)/(xT2-xT1);
        boolean verticalT = (xT2-xT1 == 0);
        
        bT = Double.POSITIVE_INFINITY;
        if (!verticalT) bT = yT1-mT*xT1;

        ArrayList<double[]> intersections = new ArrayList<double[]>();

        for (int i = 0; i < points.length-1; i++) {
            double xL1, xL2, yL1, yL2, mL, bL;
            xL1 = points[i][0];
            xL2 = points[i+1][0];
            yL1 = points[i][1];
            yL2 = points[i+1][1];

            mL = (yL2-yL1)/(xL2-xL1);
            boolean verticalL = (xL2-xL1 == 0);
            
            bL = Double.POSITIVE_INFINITY;
            if (!verticalL) bL = yL1-mL*xL1;

            if ((verticalL && verticalT) || mL == mT) continue;
            else if (verticalL) {
                xI = xL1;
                yI = mT * xI + bT;
            }
            else if (verticalT) {
                xI = xT1;
                yI = mL * xI + bL;
            }
            else {
                xI = (bT-bL)/(mL-mT);
                yI = mT * xI + bT;
            }
            
            if (
                !(Math.min(xT1,xT2) < xI && xI < Math.max(xT1,xT2) &&
                Math.min(yT1,yT2) < yI && yI < Math.max(yT1,yT2) &&
                Math.min(xL1,xL2) < xI && xI < Math.max(xL1,xL2) &&
                Math.min(yL1,yL2) < yI && yI < Math.max(yL1,yL2))
            ) continue;

            intersections.add(new double[] {xI,yI});
        }
        if (intersections.size() == 0) return new double[] {Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY};

        double[] bestIntersect = intersections.get(0);
        for (int i = 0; i < intersections.size(); i++) {
            if (intersections.get(i)[1] < bestIntersect[1]) bestIntersect = intersections.get(i);
        }
        return bestIntersect;
        
    }
    public boolean containsPoint(double[] point) {
        double xT = point[0];
        double yT = point[1];
        boolean contained = false;
        ArrayList<double[]> collidedAt = new ArrayList<double[]>();
        for (int i = 0; i < points.length; i++) {
            double x1 = points[i][0];
            double x2 = points[(i + 1) % points.length][0];
            double y1 = points[i][1];
            double y2 = points[(i + 1) % points.length][1];

            double xI = (x2*yT - x2*y1 - x1*yT + x1*y2) / (y2 - y1);
            if (x2 == x1) xI = x1;

            if ((xI <= xT) && ((x2 >= xI && xI >= x1) || (x1 >= xI && xI >= x2))
                           && ((y2 >= yT && yT >= y1) || (y1 >= yT && yT >= y2))
                           && !collidedAt.contains(new double[] {xI,yT})) {
                contained = !contained;
                collidedAt.add(new double[] {xI,yT});
            };
        }
        return contained;
    }
    /*
    This^^ used to be a bunch of fun math...
    until i simplified it :(
    all(most of?) the fun math is below if you want to see it
    @ommehta16 in the future DON'T DELETE THIS!!

    double xT = point[0];
    double yT = point[1];
    boolean contained = false;
    for (int i = 0; i < points.length; i++) {
        double x1 = points[i][0];
        double x2 = points[(i + 1) % points.length][0];
        double y1 = points[i][1];
        double y2 = points[(i + 1) % points.length][1];
        double m = (y2 - y1) / (x2 - x1);
        double b = y1 - m * x1;
        double xI = (yT - b) / m;
        if (x2 == x1)
            xI = x1;

        if ((xI <= xT) && ((x2 > xI && xI > x1) || (x1 > xI && xI > x2))
                && ((y2 > yT && yT > y1) || (y1 > yT && yT > y2)))
            contained = !contained;
    }

    return contained;
    */

    public boolean collide(Polygon other) {
        double[][] otherPoints = other.getPoints();
        int otherSides = points.length;
        double[] dists = new double[otherSides + 1];
        dists[otherSides] = Double.POSITIVE_INFINITY;

        for (int i = 0; i < otherPoints.length; i++) {
            if (!containsPoint(otherPoints[i])) continue;
            double velMag = Math.sqrt(Math.pow(velocity[0], 2) + Math.pow(velocity[1], 2));
            double othMag = Math.sqrt(Math.pow(other.velocity[0], 2) + Math.pow(other.velocity[1], 2));

            double dist = Math
                    .sqrt(Math.pow(position[0] - other.position[0], 2) + Math.pow(position[1] - other.position[1], 2));
            double diffX = position[0] - other.position[0];
            double diffY = position[1] - other.position[1];

            velocity = new double[] { elasticness * velMag * diffX / dist, elasticness * velMag * diffY / dist };
            other.velocity = new double[] { -other.elasticness * othMag * diffX / dist,
                    -other.elasticness * othMag * diffY / dist };

            if (Math.max(velMag, othMag) > 0.007) playBoing();
            return true;
        }

        return false;
    }

    public boolean collide(Polyline other) {
        double[][] otherPoints = other.getPoints();

        for (int i = 0; i < otherPoints.length-1; i++) {
            double[] intersectsAt = intersectLine(otherPoints[i], otherPoints[i+1]);
            if (Double.isInfinite(intersectsAt[0])) continue;

            double[] startL = otherPoints[i];
            double[] endL = otherPoints[i+1];

            double cLMag = Math.sqrt(Math.pow(endL[1] - startL[1], 2) + Math.pow(endL[0] - startL[0], 2));

            // invert projection of velocity onto dL, then project back to normal --> hooray!

            double[] collideLine = {(endL[0]-startL[0])/cLMag,(endL[1]-startL[1])/cLMag};
            // now, project velocity onto collideline (let collideline = c, velocity = v, projection = p)
            // p = (v dot c) * c
            double dot = velocity[0]*collideLine[0] + velocity[1] * collideLine[1];


            double[] projVector = new double[] {dot*collideLine[0],dot*collideLine[1]};
            double[] orthoVector = new double[] {velocity[0] - projVector[0],velocity[1]-projVector[1]};

            velocity = new double[] {projVector[0]-elasticness*orthoVector[0],projVector[1]-elasticness*orthoVector[1]};
            if (dot > 0.007) playBoing();
            /*double velMag = Math.sqrt(Math.pow(velocity[0], 2) + Math.pow(velocity[1], 2));

            double otherX = intersectsAt[0];
            double otherY = intersectsAt[1];

            double dist = Math.sqrt(Math.pow(position[0] - otherX, 2) + Math.pow(position[1] - otherY, 2));
            
            double diffX = position[0] - otherX - length/2;
            double diffY = position[1] - otherY - length/2;

            velocity = new double[] { elasticness * velMag * diffX / dist, elasticness * velMag * diffY / dist };*/

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
