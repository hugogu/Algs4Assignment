/**
 *
 * @author Qiangqiang Gu
 */
public class Percolation {
    private WeightedQuickUnionUF percolateUnion;
    private WeightedQuickUnionUF topUnion;
    private boolean[] opened;
    private int top;
    private int bottom;
    private int size;
    private int amount;

    // create N-by-N grid, with all sites blocked
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N");
        }
        size = N;
        amount = N * N;
        top = amount + 1;
        bottom = top + 1;
        opened = new boolean[amount];
        percolateUnion = new WeightedQuickUnionUF(bottom + 1);
        topUnion = new WeightedQuickUnionUF(amount + 1);
    }

    // open site (row i, column j) if it is not open already
    public void open(int i, int j) {
        validate(i, j);
        final int index = getIndex(i, j);
        opened[index] = true;
        if (index < size) {
            percolateUnion.union(index, top);
            topUnion.union(index, amount);
        }
        tryUnion(i, j, i, j - 1);
        tryUnion(i, j, i, j + 1);
        tryUnion(i, j, i - 1, j);
        tryUnion(i, j, i + 1, j);
        if (index >= amount - size)
            percolateUnion.union(index, bottom);
    }
    
    // is site (row i, column j) open?
    public boolean isOpen(int i, int j) {
        validate(i, j);
        return opened[getIndex(i, j)];
    }

    // is site (row i, column j) full?
    public boolean isFull(int i, int j) {
        validate(i, j);
        return topUnion.connected(amount, getIndex(i, j));
    }
    
    // does the system percolate?
    public boolean percolates() {
        return percolateUnion.connected(top, bottom);
    }

    // test client (optional)
    public static void main(String[] args) {

    }
    
    private void tryUnion(int fi, int fj, int i, int j) {
        if (i > 0 && i <= size && j > 0 && j <= size) {
            if (isOpen(i, j)) {
                topUnion.union(getIndex(i, j), getIndex(fi, fj));
                percolateUnion.union(getIndex(i, j), getIndex(fi, fj));
            }
        }
    }
    
    private void validate(int i, int j) {
        if (i <= 0 || i > size)
            throw new IndexOutOfBoundsException("i");
        if (j <= 0 || j > size)
            throw new IndexOutOfBoundsException("j");
    }
    
    private int getIndex(int i, int j) {
        return (i - 1) * size + j - 1;
    }
}