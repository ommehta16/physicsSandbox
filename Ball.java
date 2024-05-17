import java.awt.Color;

public class Ball {
    private double[] position;
    private double radius;
    private double elasticness;
    private double gravity;
    private double[] velocity;
    private Color color;
    final Color[] colors = { StdDraw.RED, StdDraw.ORANGE, StdDraw.YELLOW, StdDraw.GREEN, StdDraw.BLUE, StdDraw.MAGENTA,
            StdDraw.BLACK, StdDraw.CYAN, StdDraw.LIGHT_GRAY, StdDraw.PINK, StdDraw.PINK, StdDraw.YELLOW,
            StdDraw.DARK_GRAY };

    public Ball(double[] pos, double[] vel, double r, double elastic, Color col) {
        position = pos;
        velocity = vel;
        radius = r;
        elasticness = elastic;
        gravity = 0.001;
        color = col;
    }

    public Ball() {
        position = new double[] { 0, 0 };
        velocity = new double[] { 0, 0 };
        radius = 0.1;
        elasticness = 0.5;
        gravity = 0.001;
        color = colors[(int) (Math.random() * colors.length)];
    }

    public void move() {
        position[0] += velocity[0];
        position[1] += velocity[1];
        velocity[1] -= gravity;
    }

    public void bounceWall() {
        if (position[0] - radius <= -0.9) {
            velocity[0] *= -elasticness;
            position[0] = (-0.9 + radius + 3 * position[0]) / 4;
            //if (Math.abs(velocity[0]) > 0.005) StdAudio.playInBackground("boing.wav");
        }
        if (position[0] + radius >= 1) {
            velocity[0] *= -elasticness;
            position[0] = (1 - radius + 3 * position[0]) / 4;
            //if (Math.abs(velocity[0]) > 0.005) StdAudio.playInBackground("boing.wav");
        }
        if (position[1] - radius <= -1) {
            velocity[1] *= -elasticness;
            position[1] = (-1 + radius + 3 * position[1]) / 4;
            //if (Math.abs(velocity[1]) > 0.005) StdAudio.playInBackground("boing.wav");
        }
        if (position[1] + radius >= 1) {
            velocity[1] *= -elasticness;
            position[1] = (1 - radius + 3 * position[1]) / 4;
            //if (Math.abs(velocity[1]) > 0.005) StdAudio.playInBackground("boing.wav");
        }

    }

    public boolean collide(Ball otherBall) {
        double dist = Math.sqrt(
                Math.pow(otherBall.position[0] - position[0], 2) + Math.pow(otherBall.position[1] - position[1], 2));

        if (dist > radius + otherBall.radius) return false;

        double velMag = Math.sqrt(Math.pow(velocity[0], 2) + Math.pow(velocity[1], 2));
        double othMag = Math.sqrt(Math.pow(otherBall.velocity[0], 2) + Math.pow(otherBall.velocity[1], 2));

        double diffX = position[0] - otherBall.position[0];
        double diffY = position[1] - otherBall.position[1];

        velocity = new double[] { elasticness * velMag * diffX / dist, elasticness * velMag * diffY / dist };

        otherBall.velocity = new double[] { elasticness * -othMag * diffX / dist,
                                            elasticness * -othMag * diffY / dist };
        //if (velMag > 0.005 || othMag > 0.005) StdAudio.playInBackground("boing.wav");
        return true;
    }

    // DOESN'T WORK RIGHT NOW!
    public boolean collideLine(double x1, double y1, double x2, double y2, Polygon poly) {
        // binary search thru line for the point closest to the circle

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

        if (dist > radius)
            return false;

        double velMag = Math.sqrt(Math.pow(velocity[0], 2) + Math.pow(velocity[1], 2));
        double othMag = Math.sqrt(Math.pow(poly.getVel()[0], 2) + Math.pow(poly.getVel()[1], 2));

        double diffX = position[0] - x;
        double diffY = position[1] - y;

        //if (velMag > 0.007) StdAudio.playInBackground("boing.wav");

        velocity = new double[] { elasticness * velMag * diffX / dist, elasticness * velMag * diffY / dist };
        poly.setVel(new double[] {  poly.getElastic() * -othMag * diffX / dist,
                                    poly.getElastic() * -othMag * diffY / dist });

        return true;
    }

    public boolean collide (Polygon other) {
        if (other.containsPoint(position)) {
        
            double dist = Math.sqrt(Math.pow(other.getPos()[0] - position[0], 2) + Math.pow(other.getPos()[1] - position[1], 2));

            double velMag = Math.sqrt(Math.pow(velocity[0], 2) + Math.pow(velocity[1], 2));
            double othMag = Math.sqrt(Math.pow(other.getVel()[0], 2) + Math.pow(other.getVel()[1], 2));

            double diffX = position[0] - other.getPos()[0];
            double diffY = position[1] - other.getPos()[1];

            //if (velMag > 0.007) StdAudio.playInBackground("boing.wav");

            velocity = new double[] { elasticness * velMag * diffX / dist, elasticness * velMag * diffY / dist };
            other.setVel(new double[] {  other.getElastic() * -othMag * diffX / dist, other.getElastic() * -othMag * diffY / dist });
            return true;
        }

        double[][] points = other.getPoints();
        int sides = points.length;

        double[] dists = new double[sides+1];
        dists[sides] = Double.POSITIVE_INFINITY;
        int minInd = sides;
        int min2nd= sides;
        
        for (int i = 0; i < points.length; i++) dists[i] = Math.sqrt(Math.pow(position[0]-points[i][0],2)+Math.pow(position[1]-points[i][1],2));
        for (int i = 0; i < dists.length; i++) if (dists[i] < dists[minInd]) minInd=i;
        for (int i = 0; i < dists.length; i++) if (dists[i] < dists[min2nd] && i != minInd) min2nd=i;
        
        if ( collideLine(points[minInd][0],points[minInd][1],points[min2nd][0],points[min2nd][1],other) ) {
            return true;
        }

        return false;
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
