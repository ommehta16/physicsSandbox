# Physics Sandbox
Om Mehta

Java 18

5/28/24

# Description
The program offers a semi-accurate simulation of physics in a 2D environment, using regular polygons, circles, and polylines with arbitrary geometry. After being added to the canvas, the objects will collide with the walls and with other objects, and will have gravity applied to them. The user can then zap (to delete one object), clear (to delete ALL objects), or add an object while the program is running.

While making the program, the scope expanded dramatically. When I started, I just wanted to add gravity, and add collisions between squares and circles. As I was fixing bugs, my solutions tended to be more general than the problem that they were responding to (i.e. using an algorithm for collisions between an arbitrary polygon and circles, rather than squares and circles), so allowed (encouraged?) me to expand it. The zap and clear features were also completely outside of the original scope, and were added because they seemed fun :).

The most intensive part of the program were collisions. These required ternary search, line intersection, vector manipulation, and fun damping mechanics to get to a _workable_ state. Though they definitely aren't perfect (especially polygon-polygon collisions), they work in most situations that the program is put in.

# Requirements
 - Java 18 must be installed
 - All necessary files must be in the parent folder. These files include:
    * `Ball.java`
    * `bang.wav`
    * `boing.wav`
    * `exit.png`
    * `Polygon.java`
    * `Polyline.java`
    * `polyLine.png`
    * `reload.png`
    * `Sandbox.java`
    * `StdAudio.java`
    * `StdDraw.java`
    * `StdOut.java`

# How to Use
 - To use the sandbox, run `javac Sandbox.java` and then `java Sandbox` in the terminal
 - The buttons on the left determine what action is being taken:
    * The first "block" of them allows you to choose between a circle, polyline, or regular polygon
    * The bottom "block" (the X and reload button) allow you to reset the entire sandbox, or delete specific objects
 - Circles and polygons can be created by clicking on the screen, then dragging out to establish the radius and rotation
 - Polylines are created by clicking on each desired point (or click-dragging over them), and then pressing the space bar to finalize the shape
 - The "X" button *zaps* the closest object: the circle or polygon that is closest to the mouse pointer will be deleted
 - The reload button *resets* the entire environment. All circles, polygons and polylines will be deleted.

# Data Types
 - The project uses the `double` type for most values, and mostly stores those doubles in 1D and 2D arrays.
 - It uses the `Color` type to set the color of objects
 - `Arraylist` was used to track intersections
 - I created the ADTs `Ball`, `Polygon` and `Polyline` to store info about each type of object
# Methods in ADTs
 - `void draw()` to draw the object (Ball, Polygon and Polyline)
 - `void playBoing()` which plays a boing sound if the object has audio turned on (Ball, Polygon and Polyline)
 - `void bounceWall()` which checks, and subsequently bounces the object off of all applicable walls (Ball, Polygon and Polyline)
 - `boolean collide(Polygon|Polyline|Ball other)` collides the two if applicable, then returns true if they collided, false if they didn't

For Polygon:
 - `void updatePoints()` which sets the points array of a Polygon to match its current position (Polygon)
 - `double[] closestPoint(double[] point, double error)` which provides the point on a Polygon/Polyline that is closest to the inputted point, within the specified margin of error
 - `double[] intersectLine(double[] start, double[] end)` which finds where a given line intersects with the edges of a Polygon (will return infinity,infinity if it can't find an intersection)
 - `boolean containsPoint(double[] point)` which tells whether a Polygon contains a point
 - All applicable getters and setters are self-explanatory (get/set Pos, Points, Vel, Len, Elastic)


For Polyline:
 - `double[] closestPoint(double[] point, double error)` which provides the point on a Polygon/Polyline that is closest to the inputted point, within the specified margin of error
 - All applicable getters and setters are self-explanatory (get/set Points, Elastic)

For Ball:
 - All applicable getters and setters are self-explanatory (get/set Pos, Vel, Rad, Elastic)

# Known Bugs
 - Polygon-Polygon collisions will occassionally fail by colliding at the y-coord of a vertex on one object, even though the other object does not physically collide