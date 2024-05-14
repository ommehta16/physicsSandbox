import java.awt.Color;

public class Square {
    private double[] position;
    private double length;
    private double elasticness;
    private double gravity;
    private double[] velocity;
    private Color color;

    final Color[] colors = {StdDraw.RED, StdDraw.ORANGE, StdDraw.YELLOW, StdDraw.GREEN, StdDraw.BLUE, StdDraw.MAGENTA, StdDraw.BLACK, StdDraw.CYAN, StdDraw.LIGHT_GRAY, StdDraw.PINK, StdDraw.PINK, StdDraw.YELLOW, StdDraw.DARK_GRAY};
    
    public Square(double[] pos, double[] vel, double len, double elastic, Color col) {
        position = pos;
        velocity = vel;
        length = len;
        elasticness = elastic;
        gravity = 0*0.001;
        color = col;
    }

    public Square() {
        position = new double[] {0,0};
        velocity = new double[] {0,0};
        length = 0.1;
        elasticness = 0.5;
        gravity = 0*0.001;
        color = colors[(int)(Math.random() * colors.length)];
    }
    public void move() {
        position[0] += velocity[0];
        position[1] += velocity[1];
        velocity[1] -= gravity;
    }
    public void bounceWall() {
        if (position[0] - length <= -1) {velocity[0] *= -elasticness; position[0] = (-1+length + 3*position[0])/4;}
        if (position[0] + length >= 1)  {velocity[0] *= -elasticness; position[0] = (1-length + 3*position[0])/4;}
        if (position[1] - length <= -1) {velocity[1] *= -elasticness; position[1] = (-1+length+3*position[1])/4;}
        if (position[1] + length >= 1)  {velocity[1] *= -elasticness; position[1] = (1-length+3*position[1])/4;}

    }
    public boolean containsPoint (double x, double y) {
        if ((position[0]-length <= x && position[0]+length >= x) && (position[1]-length <= y && position[1]+length >= y)) return true;
        return false;
    }
    // TODO: FIX COLLIDE 
    public boolean collide(Square other) {
        double[] vertXs = {position[0]-length,position[0]+length,position[0]+length,position[0]-length};
        double[] vertYs = {position[1]-length,position[1]-length,position[1]+length,position[1]+length};
        boolean collided = false;

        for (int i = 0; i < vertXs.length; i++) if (other.containsPoint(vertXs[i], vertYs[i])) collided = true;

        if (!collided) return false;

        double dist = Math.sqrt(Math.pow(other.position[0] - position[0],2) + Math.pow(other.position[1] - position[1],2));

        double velMag = Math.sqrt(Math.pow(velocity[0],2) + Math.pow(velocity[1],2));
        double othMag = Math.sqrt(Math.pow(other.velocity[0],2) + Math.pow(other.velocity[1],2));
        
        double diffX = position[0]-other.position[0];
        double diffY = position[1]-other.position[1];
        
        velocity = new double[] {elasticness * velMag * diffX/dist, elasticness * velMag * diffY/dist};
        position = new double[] {position[0]+velocity[0], position[1]+velocity[1]};
        
        other.velocity = new double[] {elasticness * -othMag * diffX/dist, elasticness * -othMag * diffY/dist};
        other.position = new double[] {other.position[0]+2*other.velocity[0], other.position[1]+2*other.velocity[1]};
        return true;
    }

    // ^^^^^

    public void draw() {
        StdDraw.setPenColor(color);
        StdDraw.filledSquare(position[0], position[1], length);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.square(position[0], position[1], length);
        //StdDraw.picture(position[0],position[1],"dvd.png",4*length,2*length);
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
