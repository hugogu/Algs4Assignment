/**
 *
 * @author Qiangqiang Gu
 */
public class PercolationStats {
    private final double mean;
    private final double stddev;
    private final double confidenceLo;
    private final double confidenceHi;

    /**
     * Perform T independent experiments on an N-by-N grid
     * @param N
     * @param T 
     */
    public PercolationStats(int N, int T) {
        if (N <= 0) {
            throw new IllegalArgumentException("N");
        }
        if (T <= 0) {
            throw new IllegalArgumentException("T");
        }
        int size = T;
        final double[] thresholds = new double[T];
        while (--size >= 0) {
            thresholds[size] = fillTillPercolates(new Percolation(N), N);
        }
        mean = StdStats.mean(thresholds);
        stddev = StdStats.stddev(thresholds);
        double confidenceWidth = 1.96 * stddev / Math.sqrt(thresholds.length);
        this.confidenceLo = mean - confidenceWidth;
        this.confidenceHi = mean + confidenceWidth;
    }
    
    /**
     * sample mean of percolation threshold
     * @return 
     */
    public double mean() {
        return mean;
    }


    /**
     * sample standard deviation of percolation threshold
     * @return 
     */
    public double stddev() {
        return stddev;
    }


    /**
     * low endpoint of 95% confidence interval
     * @return 
     */
    public double confidenceLo() {
        return confidenceLo;
    }

    /**
     * high endpoint of 95% confidence interval
     * @return 
     */
    public double confidenceHi() {
        return confidenceHi;
    }
    
    /**
     * test client (described below)
     * @param args 
     */
    public static void main(String[] args) {
        final int N = Integer.parseInt(args[0]);
        final int T = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(N, T);
        StdOut.printf("mean\t=%s", stats.mean());
        StdOut.println();
        StdOut.printf("stddev\t=%s", stats.stddev());
        StdOut.println();
        String format = "95%% confidence interval\t=%s, %s";
        StdOut.printf(format, stats.confidenceLo(), stats.confidenceHi());
    }
    
    private double fillTillPercolates(Percolation percolation, int N) {
        double count = 0;
        int row, column = 0;
        while (!percolation.percolates()) {
            do {
                row = StdRandom.uniform(N) + 1;
                column = StdRandom.uniform(N) + 1;
            } while (percolation.isOpen(row, column));
            percolation.open(row, column);
            count++;
        }
        return count / (N * N);
    }
}
