public class Square {
    private double[] position;
    private double length;
    private double elasticness;
    private double[] velocity;
    
    public Square(double[] pos, double[] vel, double l, double elastic) {
        position = pos;
        velocity = vel;
        length = l;
        elasticness = elastic;
    }

    public Square() {
        position = new double[] {0,0};
        length = 0.1;
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
