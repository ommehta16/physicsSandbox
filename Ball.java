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
    
    public boolean collide(Square other) {
        
        double[][] verts = {
            {other.getPos()[0]+other.getLen(),other.getPos()[1]+other.getLen()},
            {other.getPos()[0]+other.getLen(),other.getPos()[1]               },
            {other.getPos()[0]+other.getLen(),other.getPos()[1]-other.getLen()},
            {other.getPos()[0]               ,other.getPos()[1]-other.getLen()},
            {other.getPos()[0]-other.getLen(),other.getPos()[1]-other.getLen()},
            {other.getPos()[0]-other.getLen(),other.getPos()[1]               },
            {other.getPos()[0]-other.getLen(),other.getPos()[1]+other.getLen()},
            {other.getPos()[0]               ,other.getPos()[1]+other.getLen()}
        };

        boolean collided = false;
        double dist = Double.POSITIVE_INFINITY;
        for (int i = 0; i < verts.length; i++) {
            dist = Math.sqrt(Math.pow(verts[i][0] - position[0],2) + Math.pow(verts[i][1] - position[1],2));
            if (dist < radius) {collided = true; break;}
        }
        if (!collided) return false;

        dist = Math.sqrt(Math.pow(other.getPos()[0] - position[0],2) + Math.pow(other.getPos()[0] - position[1],2));
        double velMag = Math.sqrt(Math.pow(velocity[0],2) + Math.pow(velocity[1],2));
        double othMag = Math.sqrt(Math.pow(other.getVel()[0],2) + Math.pow(other.getVel()[1],2));
        
        double diffX = position[0]-other.getPos()[0];
        double diffY = position[1]-other.getPos()[1];
        
        velocity = new double[] {elasticness * velMag * diffX/dist, elasticness * velMag * diffY/dist};
        position = new double[] {position[0]+velocity[0], position[1]+velocity[1]};
        
        other.setVel(new double[] {elasticness * -othMag * diffX/dist, elasticness * -othMag * diffY/dist});
        other.setPos(new double[] {other.getPos()[0]+(other.getVel()[0]), other.getPos()[1]+other.getVel()[1]});

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
