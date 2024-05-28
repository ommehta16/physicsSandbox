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
