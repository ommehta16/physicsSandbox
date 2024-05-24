import java.awt.Color;

public class Ball {
    private double[] position;
    private double radius;
    private double elasticness;
    private double gravity;
    private double[] velocity;
    private Color color;

    public Ball(double[] pos, double[] vel, double r, double elastic, Color col) {
        position = pos;
        velocity = vel;
        radius = r;
        elasticness = elastic;
        gravity = 0.001;
        color = col;
    }

    public void move() {
        position[0] += velocity[0];
        position[1] += velocity[1];
        velocity[1] -= gravity;

        velocity[0] *= 0.98;
        velocity[1] *= 0.98;
    }

    public void bounceWall() {
        if (position[0] - radius <= -0.9) {
            velocity[0] *= -elasticness;
            position[0] = (-0.9+0.01 + radius + 3 * position[0]) / 4;
        }
        if (position[0] + radius >= 1) {
            velocity[0] *= -elasticness;
            position[0] = (1-0.01 - radius + 3 * position[0]) / 4;
        }
        if (position[1] - radius <= -1) {
            velocity[1] *= -elasticness;
            position[1] = (-1+0.01 + radius + 3 * position[1]) / 4;
        }
        if (position[1] + radius >= 1) {
            velocity[1] *= -elasticness;
            position[1] = (1-0.01 - radius + 3 * position[1]) / 4;
        }
    }

    public boolean collide(Ball other) {
        double dist = Math.sqrt(Math.pow(other.position[0] - position[0], 2) + Math.pow(other.position[1] - position[1], 2));

        if (dist > radius + other.radius) return false;

        double velMag = Math.sqrt(Math.pow(velocity[0], 2) + Math.pow(velocity[1], 2));
        double othMag = Math.sqrt(Math.pow(other.velocity[0], 2) + Math.pow(other.velocity[1], 2));

        double diffX = position[0] - other.position[0];
        double diffY = position[1] - other.position[1];

        velocity =      new double[] { elasticness *  velMag * diffX / dist, elasticness *  velMag * diffY / dist };
        other.velocity= new double[] { elasticness * -othMag * diffX / dist, elasticness * -othMag * diffY / dist };
        return true;
    }

    public boolean collide (Polygon other) {
        
        // Special case for if we're inside the polygon
        if (other.containsPoint(position)) {
            double dist = Math.sqrt(Math.pow(other.getPos()[0] - position[0], 2) + Math.pow(other.getPos()[1] - position[1], 2));

            double velMag = Math.sqrt(Math.pow(velocity[0], 2) + Math.pow(velocity[1], 2));
            double othMag = Math.sqrt(Math.pow(other.getVel()[0], 2) + Math.pow(other.getVel()[1], 2));

            double diffX = position[0] - other.getPos()[0];
            double diffY = position[1] - other.getPos()[1];

            velocity = new double[] { elasticness * velMag * diffX / dist, elasticness * velMag * diffY / dist };
            other.setVel(new double[] {  other.getElastic() * -othMag * diffX / dist, other.getElastic() * -othMag * diffY / dist });
            return true;
        }

        double[][] points = other.getPoints();
        int sides = points.length;

        // Fun (optimization?): find the closest two points, then only look at the line between them :)
        double[] dists = new double[sides+1];
        dists[sides] = Double.POSITIVE_INFINITY;
        int minInd = sides;
        int min2nd= sides;

        for (int i = 0; i < points.length; i++) dists[i] = Math.sqrt(Math.pow(position[0]-points[i][0],2)+Math.pow(position[1]-points[i][1],2));        
        for (int i = 0; i < dists.length; i++) if (dists[i] < dists[minInd]) minInd=i;
        for (int i = 0; i < dists.length; i++) if (dists[i] < dists[min2nd] && i != minInd) min2nd=i;
        
        

        double x1 = points[minInd][0];
        double x2 = points[min2nd][0];
        double y1 = points[minInd][1];
        double y2 = points[min2nd][1];

        // Binary search to find the closest point on the line
        while (Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2)) > radius*radius / 20) {
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


        double dist = Math.sqrt(Math.pow(x - position[0], 2) + Math.pow(y - position[1], 2));
        if (dist > radius) return false;

        double velMag = Math.sqrt(Math.pow(velocity[0], 2) + Math.pow(velocity[1], 2));
        double othMag = Math.sqrt(Math.pow(other.getVel()[0], 2) + Math.pow(other.getVel()[1], 2));

        double diffX = position[0] - x;
        double diffY = position[1] - y;

        velocity = new double[] {   (elasticness * velMag * diffX / dist)/1,
                                    (elasticness * velMag * diffY / dist)/1 };
        other.setVel(new double[] { other.getElastic()  * -othMag * diffX / dist,
                                    other.getElastic()  * -othMag * diffY / dist });

        return true;
    }
    
    public boolean collide (Polyline other) {
        
        double[][] collInfo = other.closestPoint(position, radius/20);

        double[] closePoint = collInfo[0];
        double[] startL = collInfo[1];
        double[] endL = collInfo[2];
        
        double x = closePoint[0];
        double y = closePoint[1];

        double dist = Math.sqrt(Math.pow(x - position[0], 2) + Math.pow(y - position[1], 2));
        if (dist > radius) return false;

        double cLMag = Math.sqrt(Math.pow(endL[1] - startL[1], 2) + Math.pow(endL[0] - startL[0], 2));

        // invert projection of velocity onto dL, then project back to normal --> hooray!

        double[] collideLine = {(endL[0]-startL[0])/cLMag,(endL[1]-startL[1])/cLMag};
        // now, project velocity onto collideline (let collideline = c, velocity = v, projection = p)
        // p = (v dot c) * c
        double dot = velocity[0]*collideLine[0] + velocity[1] * collideLine[1];


        double[] projVector = new double[] {dot*collideLine[0],dot*collideLine[1]};
        double[] orthoVector = new double[] {velocity[0] - projVector[0],velocity[1]-projVector[1]};

        velocity = new double[] {projVector[0]-elasticness*orthoVector[0],projVector[1]-elasticness*orthoVector[1]};

        if (dist < 0.5 * radius) {
            double diffX = position[0] - x;
            double diffY = position[1] - y;
            double velMag = Math.sqrt(Math.pow(velocity[1], 2) + Math.pow(velocity[0], 2));

            velocity = new double[] { velMag * diffX / dist, velMag * diffY / dist };
            position = new double[] {position[0]+velocity[0],position[1]+velocity[1]};
        }

        return true;
    }

    public void draw() {
        StdDraw.setPenColor(color);
        StdDraw.filledCircle(position[0], position[1], radius);
        
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.circle(position[0], position[1], radius);
    }

    public double[] getPos() {
        return position;
    }

    public void setPos(double[] pos) {
        position = pos;
    }

    public double[] getVel() {
        return velocity;
    }

    public void setVel(double[] vel) {
        velocity = vel;
    }

    public double getRad() {
        return radius;
    }

    public void setRad(double rad) {
        radius = rad;
    }

    public double getElastic() {
        return elasticness;
    }

    public void setElastic(double elastic) {
        elasticness = elastic;
    }

}