import java.awt.Color;
import java.util.ArrayList;

public class Ball {
    private double[] position;
    private double radius;
    private double elasticness;
    private double gravity;
    private double[] velocity;
    private Color color;
    final Color[] colors = {StdDraw.RED, StdDraw.ORANGE, StdDraw.YELLOW, StdDraw.GREEN, StdDraw.BLUE, StdDraw.MAGENTA, StdDraw.BLACK, StdDraw.CYAN, StdDraw.LIGHT_GRAY, StdDraw.PINK, StdDraw.PINK, StdDraw.YELLOW, StdDraw.DARK_GRAY};
    
    public Ball(double[] pos, double[] vel, double r, double elastic, Color col) {
        position = pos;
        velocity = vel;
        radius = r;
        elasticness = elastic;
        gravity = 0.001;
        color = col;
    }

    public Ball() {
        position = new double[] {0,0};
        velocity = new double[] {0,0};
        radius = 0.1;
        elasticness = 0.5;
        gravity = 0.001;
        color = colors[(int)(Math.random() * colors.length)];
    }
    public void move() {
        position[0] += velocity[0];
        position[1] += velocity[1];
        velocity[1] -= gravity;
    }
    
    public void bounceWall() {
        if (position[0] - radius <= -1) {velocity[0] *= -elasticness; position[0] = (-1+radius + 3*position[0])/4;}
        if (position[0] + radius >= 1)  {velocity[0] *= -elasticness; position[0] = (1-radius + 3*position[0])/4;}
        if (position[1] - radius <= -1) {velocity[1] *= -elasticness; position[1] = (-1+radius+3*position[1])/4;}
        if (position[1] + radius >= 1)  {velocity[1] *= -elasticness; position[1] = (1-radius+3*position[1])/4;}

    }

    public boolean collide(Ball otherBall) {
        double dist = Math.sqrt(Math.pow(otherBall.position[0] - position[0],2) + Math.pow(otherBall.position[1] - position[1],2));
        
        if (dist > radius + otherBall.radius) return false;
        
        double velMag = Math.sqrt(Math.pow(velocity[0],2) + Math.pow(velocity[1],2));
        double othMag = Math.sqrt(Math.pow(otherBall.velocity[0],2) + Math.pow(otherBall.velocity[1],2));
        
        double diffX = position[0]-otherBall.position[0];
        double diffY = position[1]-otherBall.position[1];
        
        velocity = new double[] {elasticness * velMag * diffX/dist, elasticness * velMag * diffY/dist};
        position = new double[] {position[0]+velocity[0], position[1]+velocity[1]};
        
        otherBall.velocity = new double[] {elasticness * -othMag * diffX/dist, elasticness * -othMag * diffY/dist};
        otherBall.position = new double[] {otherBall.position[0]+otherBall.velocity[0], otherBall.position[1]+otherBall.velocity[1]};
        return true;
    }
    // AAAAAAAH
    public boolean collideLine(double x1, double y1, double x2, double y2) {
        // binary search thru line for the point closest to the circle -- 5 iterations
        
        while (Math.max(Math.abs(x1-x2),Math.abs(y1-y2)) > radius/20) {
            double dist1 = Math.sqrt(Math.pow(x1-position[0],2)+Math.pow(y1-position[1], 2));
            double dist2 = Math.sqrt(Math.pow(x2-position[0],2)+Math.pow(y2-position[1], 2));

            if (dist1 < dist2) {
                x2 = (x1+x2)/2;
                y2 = (y1+y2)/2;
            }
            else {
                x1 = (x1+x2)/2;
                y1 = (y1+y2)/2;
            }

        }
        double x = (x1+x2)/2;
        double y = (y1+y2)/2;
        double dist = Math.sqrt(Math.pow(x-position[0],2)+Math.pow(y-position[1], 2));
        if (dist > radius) return false;

        double velMag = Math.sqrt(Math.pow(velocity[0],2) + Math.pow(velocity[1],2));
        
        double diffX = position[0]-x;
        double diffY = position[1]-y;
        
        velocity = new double[] {elasticness * velMag * diffX/dist, elasticness * velMag * diffY/dist};

        return true;
    }
    public boolean collide(Square other) {
        if (collideLine(other.getPos()[0]-other.getLen(), other.getPos()[1]-other.getLen(),
                        other.getPos()[0]-other.getLen(), other.getPos()[1]+other.getLen()));

        if (collideLine(other.getPos()[0]-other.getLen(), other.getPos()[1]+other.getLen(),
                        other.getPos()[0]+other.getLen(), other.getPos()[1]+other.getLen()));

        if (collideLine(other.getPos()[0]+other.getLen(), other.getPos()[1]+other.getLen(),
                        other.getPos()[0]+other.getLen(), other.getPos()[1]-other.getLen()));

        if (collideLine(other.getPos()[0]+other.getLen(), other.getPos()[1]-other.getLen(),
                        other.getPos()[0]-other.getLen(), other.getPos()[1]-other.getLen()));


        /*
        double[][] verts = new double[40][2];
        double[][] squareVerts = {
            {other.getPos()[0]-other.getLen(),other.getPos()[1]-other.getLen()},
            {other.getPos()[0]-other.getLen(),other.getPos()[1]+other.getLen()},
            {other.getPos()[0]+other.getLen(),other.getPos()[1]+other.getLen()},
            {other.getPos()[0]+other.getLen(),other.getPos()[1]-other.getLen()}
        };
        int point = 0;

        for (int i = 0; i < verts.length; i++) {
            if (i ==   verts.length/4) point++;
            if (i ==   verts.length/2) point++;
            if (i == 3*verts.length/4) point++;
            verts[i] = new double[] {
                squareVerts[(point + 1) % 4][0] + (squareVerts[point][0] - squareVerts[(point + 1) % 4][0]) * (i % (verts.length/4))/(verts.length/4),
                squareVerts[(point + 1) % 4][1] + (squareVerts[point][1] - squareVerts[(point + 1) % 4][1]) * (i % (verts.length/4))/(verts.length/4)
            };
        }

        for (int i = 0; i < verts.length; i++) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.filledCircle(verts[i][0],verts[i][1],0.01);
            double dist = Math.sqrt(Math.pow(verts[i][0]-position[0],2)+Math.pow(verts[i][1]-position[1],2));
            if (dist <= radius) {
                if (i < verts.length/4 || (verts.length/2 < i) && (3*verts.length/4 > i)) velocity[0] *= -1;
                else velocity[1] *= -1;
                return true;
            }
        }
        */
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
