/*************************************************************************
 * Name:  Qiangqiang Gu
 * Email: gqq@outlook.com
 *
 * Compilation:  javac Point.java
 * Execution:
 * Dependencies: StdDraw.java
 *
 * Description: An immutable data type for points in the plane.
 *
 *************************************************************************/

import java.util.Comparator;

public class Point implements Comparable<Point> {

    // compare points by slope
    public final Comparator<Point> SLOPE_ORDER = new Comparator<Point>() {
        @Override
        public int compare(final Point thiz, final Point that) {
            final double thisSlope = Point.this.slopeTo(thiz);
            final double thatSlope = Point.this.slopeTo(that);
            
            return Double.compare(thisSlope, thatSlope);
        }
    };

    private final int x;                              // x coordinate
    private final int y;                              // y coordinate

    // create the point (x, y)
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    // plot this point to standard drawing
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    // draw line between this point and that point to standard drawing
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // slope between this point and that point
    public double slopeTo(Point that) {
        final int deltaX = that.x - x;
        final int deltaY = that.y - y;
        if (deltaX == 0 && deltaY == 0)
            return Double.NEGATIVE_INFINITY;
        else if (deltaX == 0)
            return Double.POSITIVE_INFINITY;
        else if (deltaY == 0)
            return 0;
        else
            return deltaY * 1.0 / deltaX;
    }

    // is this point lexicographically smaller than that one?
    // comparing y-coordinates and breaking ties by x-coordinates
    public int compareTo(Point that) {
        if (y == that.y)
            return x - that.x;
        return y - that.y;
    }

    // return string representation of this point
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    // unit test
    public static void main(String[] args) {
        final Point p = new Point(5, 4);
        final Point q = new Point(5, 4);
        final Point r = new Point(5, 3);
        assert p.SLOPE_ORDER.compare(q, r) == -1;
        assert p.slopeTo(q) == Double.NEGATIVE_INFINITY;
        assert p.slopeTo(r) == Double.POSITIVE_INFINITY;
    }
}