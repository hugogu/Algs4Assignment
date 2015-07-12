/*************************************************************************
 * Name:  Qiangqiang Gu
 * Email: gqq@outlook.com
 *
 * Compilation:  javac Fast.java
 * Execution:
 * Dependencies: StdDraw.java
 *
 * Description: A quicker implementation of collinear points finder.
 *
 *************************************************************************/

import java.util.Arrays;

public class Fast {

    public static void main(String[] args) {
        final String filename = args[0];
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        final In in = new In(filename);
        final int N = in.readInt();
        final Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            final Point p = new Point(x, y);
            points[i] = p;
            p.draw();
        }
        for (int i = 0; i < points.length; i++) {
            final Point[] others = copyExcept(points, i);
            findLinesegmentsFromPoint(others, points[i]);
        }
    }
    
    private static Point[] copyExcept(final Point[] items, int index) {
        final Point[] result = new Point[items.length - 1];
        if (index == 0) {
            System.arraycopy(items, 1, result, 0, items.length - 1);
        } else if (index == items.length - 1) {
            System.arraycopy(items, 0, result, 0, items.length - 1);
        } else {
            System.arraycopy(items, 0, result, 0, index);
            System.arraycopy(items, index + 1, result, index, items.length - index - 1);
        }
        return result;
    }

    private static void findLinesegmentsFromPoint(final Point[] points, final Point axis) {
        Arrays.sort(points, axis.SLOPE_ORDER);
        double slope = Double.NaN;
        int startIndex = 0;
        for (int i = 0; i < points.length; i++) {
            final double currentSlope = axis.slopeTo(points[i]);
            if (Double.isNaN(slope) || currentSlope != slope) {
                if (i - startIndex >= 3) {
                    connectPoints(Arrays.copyOfRange(points, startIndex, i), axis);
                }
                startIndex = i;
                slope = currentSlope;
            }
            if (i == points.length - 1 && currentSlope == slope && i - startIndex >= 2) {
                connectPoints(Arrays.copyOfRange(points, startIndex, i + 1), axis);
            }
        }
    }

    private static void connectPoints(final Point[] points, final Point axis) {
        final Point[] allPoints = new Point[points.length + 1];
        System.arraycopy(points, 0, allPoints, 0, points.length);
        allPoints[points.length] = axis;
        Arrays.sort(allPoints);
        if (axis.compareTo(allPoints[0]) == 0) {
            allPoints[0].drawTo(allPoints[points.length]);
            for (int i = 0; i < allPoints.length; i++) {
                StdOut.print(allPoints[i]);
                if (i < points.length) {
                    StdOut.print(" -> ");
                }
            }
            StdOut.println();
        }
    }
}
