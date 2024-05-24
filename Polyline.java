public class Polyline {
    private double elasticness;
    private int sides;
    private double[][] points;

    public Polyline(double[][] pts, double elastic) {
        points = pts;
        elasticness = elastic;
        sides = points.length;
        

    }
    public double[][] closestPoint(double[] point, double error) {
        double lowestDist = 100;
        int lowestDistInd = 0;
        double[] closePoint = points[0];
        for (int i = 0; i < points.length-1; i++) {
            double x1 = points[i][0];
            double y1 = points[i][1];
            double x2 = points[(i+1) % points.length][0];
            double y2 = points[(i+1) % points.length][1];
            while (Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2)) > error) {
                double dist1 = Math.sqrt(Math.pow(x1 - point[0], 2) + Math.pow(y1 - point[1], 2));
                double dist2 = Math.sqrt(Math.pow(x2 - point[0], 2) + Math.pow(y2 - point[1], 2));

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
            double dist = Math.sqrt(Math.pow(x-point[0],2)+Math.pow(y-point[1],2));

            if (dist < lowestDist) {
                lowestDist = dist;
                lowestDistInd = i;
                closePoint = new double[] {x,y};
            }
        }
        return new double[][] {closePoint,points[lowestDistInd],points[(lowestDistInd+1) % points.length]};
    }

    public void draw() {
        StdDraw.setPenColor();
        for (int i = 0; i < sides-1; i++) {
            StdDraw.line(points[i][0],points[i][1],points[i+1][0],points[i+1][1]);
        }
    }

    public void setPoints(double[][] pts) {
        points = pts;
    }

    public double[][] getPoints() {
        return points;
    }

    public double getElastic() {
        return elasticness;
    }

    public void setElastic(double elastic) {
        elasticness = elastic;
    }

}
