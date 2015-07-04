/*************************************************************************
 * Name:  Qiangqiang Gu
 * Email: gqq@outlook.com
 *
 * Compilation:  javac Brute.java
 * Execution:
 * Dependencies: StdDraw.java
 *
 * Description: A standard implementation of collinear points finder.
 *
 *************************************************************************/

import java.util.ArrayList;
import java.util.List;

public class Brute {

    public static void main(String[] args) {
        final String filename = args[0];
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        final In in = new In(filename);
        final int N = in.readInt();
        final List<Point> points = new ArrayList<Point>(N);
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            final Point p = new Point(x, y);
            points.add(p);
            p.draw();
        }
        for (final Point a : points) {
            for (final Point b : points) {
                if (b.compareTo(a) <= 0) {
                    continue;
                }
                for (final Point c : points) {
                    if (c.compareTo(a) <= 0 || c.compareTo(b) <= 0) {
                        continue;
                    }
                    for (final Point d : points) {
                        if (d.compareTo(a) <= 0 || d.compareTo(b) <= 0 || d.compareTo(c) <= 0) {
                            continue;
                        }
                        if (a.slopeTo(b) == b.slopeTo(c) && b.slopeTo(c) == c.slopeTo(d)) {
                            StdOut.println(a + " -> " + b + " -> " + c + " -> " + d);
                            a.drawTo(d);
                        }
                    }
                }
            }
        }
    }
}
