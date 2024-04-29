public class Ball {
    private double[] position;
    private double radius;
    private double elasticness;
    private double gravity;
    private double[] velocity;
    final private double tiny = 0.000000001;
    
    public Ball(double[] pos, double[] vel, double r, double elastic) {
        position = pos;
        velocity = vel;
        radius = r;
        elasticness = elastic;
        gravity = 9.8 * 0.001; //9.5m/s^2
    }

    public Ball() {
        position = new double[] {0,0};
        velocity = new double[] {0,0};
        radius = 0.1;
        elasticness = 0.5;
        gravity = 0.001;
    }
    public void move() {
        position[0] += velocity[0];
        position[1] += velocity[1];
        velocity[1] -= gravity;
    }
    
    public void bounceWall() {
        if (position[0] - radius <= -1 || position[0] + radius >= 1) velocity[0] *= -elasticness;
        if (position[1] - radius <= -1 || position[1] + radius >= 1) velocity[1] *= -elasticness;

    }

    public void collide(Ball otherBall) {
        double dist = Math.sqrt(Math.pow(otherBall.position[0] - position[0],2) + Math.pow(otherBall.position[1] - position[1],2));
        if (dist > radius + otherBall.radius) return;
        
        double velMag = Math.sqrt(Math.pow(velocity[0],2) + Math.pow(velocity[1],2));
        double othMag = Math.sqrt(Math.pow(otherBall.velocity[0],2) + Math.pow(otherBall.velocity[1],2));
        
        double diffX = position[0]-otherBall.position[0];
        double diffY = position[1]-otherBall.position[1];

        velocity = new double[] {velMag * diffX/dist, velMag * diffY/dist};
        otherBall.velocity = new double[] {-othMag * diffX/dist, -othMag * diffY/dist};


    }
    
    public void collide(Square otherSquare) {
        // collides with a Square
        return;
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
