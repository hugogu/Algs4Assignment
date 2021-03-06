import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Qiangqiang Gu
 */
public class Board {
    private final int[][] blocks;
    private final List<Board> neighbors = new ArrayList<Board>();
    private final BlockInitializer blockInitializer = new BlockInitializer();
    private final HammingMatcher hammingMatcher = new HammingMatcher();
    private final ManhattanMatcher manhattanMatcher = new ManhattanMatcher();
    private int blankRow;
    private int blankColumn;

    public Board(final int[][] blocks) {
        this.blocks = blocks;
        final List<BlockVisitor> visitors = new ArrayList<BlockVisitor>();
        visitors.add(blockInitializer);
        visitors.add(new MisMatchVisitor(hammingMatcher));
        visitors.add(new MisMatchVisitor(manhattanMatcher));
        this.visitBlocks(new CompositeVisitor(visitors));
    }

    public int dimension() {
        return blocks.length;
    }

    public int hamming() {
        return hammingMatcher.counter;
    }

    public int manhattan() {
        return manhattanMatcher.counter;
    }

    public boolean isGoal() {
        return hamming() == 0;
    }

    public Board twin() {
        final int[][] copy = copy();
        if (this.visitBlocks(new MisMatchVisitor(new BlockVisitor() {
            @Override
            public boolean visit(final int row, final int column) {
                final int tmp = getValue(row, column);
                if (column + 1 < blocks.length && getValue(row, column + 1) != 0 && !isAtRightPlace(row, column + 1)) {
                    copy[row][column] = getValue(row, column + 1);
                    copy[row][column + 1] = tmp;
                    return false;
                } else if (column - 1 > 0 && getValue(row, column - 1) != 0) {
                    copy[row][column] = getValue(row, column - 1);
                    copy[row][column - 1] = tmp;
                    return false;
                } else {
                    return true;
                }
            }
        }))) {
            if (blocks.length > 1) {
                int row = 0;
                while (getValue(row, 0) == 0 || getValue(row, 1) == 0)
                    row++;
                copy[row][1] = getValue(row, 0);
                copy[row][0] = getValue(row, 1);
            }
        }
        return new Board(copy);
    }

    public Iterable<Board> neighbors() {
        if (neighbors.isEmpty()) {
            findNeighbors();
        }
        return neighbors;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(blocks.length).append("\n");
        visitBlocks(new ToStringVisitor(builder));
        return builder.toString();
    }

    @Override
    public boolean equals(final Object that) {
        if (that == null) {
            return false;
        }
        if (this == that) {
            return true;
        }
        if (that instanceof Board) {
            final Board another = (Board) that;
            if (this.blockInitializer.hashCode() != another.blockInitializer.hashCode()) {
                return false;
            }
            for (int row = 0; row < blocks.length; row++) {
                for (int column = 0; column < blocks.length; column++) {
                    if (this.getValue(row, column) != another.getValue(row, column)) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
    }

    private boolean isAtRightPlace(final int row, final int column) {
        final int value = getValue(row, column);
        return value == 0 || value == row * blocks.length + column + 1;
    }

    private void findNeighbors() {
        if (blankColumn + 1 < blocks.length) {
            neighbors.add(copyAndMove(blankRow, blankColumn + 1));
        }
        if (blankRow + 1 < blocks.length) {
            neighbors.add(copyAndMove(blankRow + 1, blankColumn));
        }
        if (blankColumn - 1 >= 0) {
            neighbors.add(copyAndMove(blankRow, blankColumn - 1));
        }
        if (blankRow - 1 >= 0) {
            neighbors.add(copyAndMove(blankRow - 1, blankColumn));
        }
    }

    private Board copyAndMove(final int row, final int column) {
        final int[][] copy = copy();
        copy[blankRow][blankColumn] = this.blocks[row][column];
        copy[row][column] = 0;
        return new Board(copy);
    }

    private int getValue(final int row, final int column) {
        return this.blocks[row][column] & 0x0000FFFF;
    }

    private int getDistance(final int row, final int column) {
        return (this.blocks[row][column] & 0xFFFF0000) >> 16;
    }

    private void setDistance(final int row, final int column, final int dist) {
        this.blocks[row][column] = (dist << 16) + getValue(row, column);
    }

    private int[][] copy() {
        final int[][] blockCopy = new int[blocks.length][blocks.length];
        final BlockVisitor copier = new BlockVisitor() {
            public boolean visit(final int row, final int column) {
                blockCopy[row][column] = getValue(row, column);
                return true;
            }
        };
        this.visitBlocks(copier);
        return blockCopy;
    }

    private boolean visitBlocks(final BlockVisitor visitor) {
        for (int row = 0; row < blocks.length; row++) {
            for (int column = 0; column < blocks.length; column++) {
                if (!visitor.visit(row, column)) {
                    return false;
                }
            }
        }
        return true;
    }

    private interface BlockVisitor {

        /**
         *
         * @param row
         * @param column
         * @return true if continue to visit next.
         */
        boolean visit(int row, int column);
    }

    private class CompositeVisitor implements BlockVisitor {

        private final Collection<BlockVisitor> visitors;

        private CompositeVisitor(final Collection<BlockVisitor> visitors) {
            this.visitors = visitors;
        }

        @Override
        public boolean visit(final int row, final int column) {
            boolean goon = true;
            for (final BlockVisitor visitor : visitors) {
                goon &= visitor.visit(row, column);
            }
            return goon;
        }
    }

    private class MisMatchVisitor implements BlockVisitor {
        private final BlockVisitor innerVisitor;

        private MisMatchVisitor(final BlockVisitor visitor) {
            this.innerVisitor = visitor;
        }

        @Override
        public boolean visit(final int row, final int column) {
            return isAtRightPlace(row, column) || innerVisitor.visit(row, column);
        }
    }

    private class BlockInitializer implements BlockVisitor {
        private int hash = 0;

        @Override
        public boolean visit(final int row, final int column) {
            final int value = getValue(row, column);
            hash ^= value + (row << 10) + (column << 20);
            if (value == 0) {
                blankRow = row;
                blankColumn = column;
            }
            return true;
        }

        @Override
        public int hashCode() {
            return hash;
        }
    }

    private class HammingMatcher implements BlockVisitor {
        private int counter = 0;

        @Override
        public boolean visit(final int row, final int column) {
            counter++;
            return true;
        }
    }

    private class ManhattanMatcher implements BlockVisitor {
        private int counter = 0;

        @Override
        public boolean visit(final int row, final int column) {
            final int value = getValue(row, column);
            final int distance = Math.abs(row - (value - 1) / blocks.length) + 
                                 Math.abs(column - (value - 1) % blocks.length);
            setDistance(row, column, distance);
            counter += distance;
            return true;
        }
    }

    private class ToStringVisitor implements BlockVisitor {
        private final StringBuilder builder;

        private ToStringVisitor(final StringBuilder builder) {
            this.builder = builder;
        }

        @Override
        public boolean visit(final int row, final int column) {
            builder.append(getValue(row, column)).append(" ");
            if (column == blocks.length - 1) {
                builder.append("\n");
            }
            return true;
        }
    }
}
