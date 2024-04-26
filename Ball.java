public class Ball {
    private double[] position;
    private double radius;
    private double elasticness;
    private double[] velocity;
    
    public Ball(double[] pos, double[] vel, double r, double elastic) {
        position = pos;
        velocity = vel;
        radius = r;
        elasticness = elastic;
    }

    public Ball() {
        position = new double[] {0,0};
        radius = 0.1;
        elasticness = 1;
    }

    public void collide(Ball otherBall) {
        // collides with a Ball
        return;
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
