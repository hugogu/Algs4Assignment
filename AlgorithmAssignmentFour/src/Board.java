
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Qiangqiang Gu
 */
public class Board {
    private final int[][] blocks;
    private int blankRow;
    private int blankColumn;

    public Board(final int[][] blocks) {
        this.blocks = blocks;
        final BlockVisitor blankFinder = new BlockVisitor() {
            public void visit(int row, int column) {
                if (blocks[row][column] == 0) {
                    blankRow = row;
                    blankColumn = column;
                }
            }
        };
        this.visitBlocks(blankFinder);
    }

    public int dimension() {
        return blocks.length;
    }

    public int hamming() {
        final HammingMatcher matcher = new HammingMatcher();
        this.iterateMisMatchBlocks(matcher);
        return matcher.counter;
    }

    public int manhattan() {
        final ManhattanMatcher matcher = new ManhattanMatcher();
        this.iterateMisMatchBlocks(matcher);
        return matcher.counter;
    }

    public boolean isGoal() {
        return hamming() == 0;
    }

    public Board twin() {
        if (blankColumn + 1 < blocks.length) {
            return copyAndMove(blankRow, blankColumn + 1);
        } else {
            return copyAndMove(blankRow, blankColumn - 1);
        }
    }

    public boolean equals(final Object that) {
        if (that == null) {
            return false;
        }
        if (this == that) {
            return true;
        }
        if (that instanceof Board) {
            final Board another = (Board) that;
            for (int row = 0; row < blocks.length; row++) {
                for (int column = 0; column < blocks.length; column++) {
                    if (this.blocks[row][column] != another.blocks[row][column]) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {
        final List<Board> neighbors = new ArrayList<Board>();
        if (blankColumn + 1 < blocks.length) {
            neighbors.add(copyAndMove(blankRow, blankColumn + 1));
        }
        if (blankColumn - 1 >= 0) {
            neighbors.add(copyAndMove(blankRow, blankColumn - 1));
        }
        if (blankRow + 1 < blocks.length) {
            neighbors.add(copyAndMove(blankRow + 1, blankColumn));
        }
        if (blankRow - 1 >= 0) {
            neighbors.add(copyAndMove(blankRow - 1, blankColumn));
        }
        return neighbors;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(blocks.length).append(System.lineSeparator());
        for (int[] block : blocks) {
            for (int value : block) {
                builder.append(value).append(" ");
            }
            builder.append(System.lineSeparator());
        }
        return builder.toString();
    }

    public static void main(String[] args) {

    }
    
    private Board copyAndMove(final int row, final int column) {
        final int[][] copy = copy(this.blocks);
        copy[blankRow][blankColumn] = this.blocks[row][column];
        copy[row][column] = 0;
        return new Board(copy);
    }

    private int[][] copy(final int[][] blocks) {
        final int[][] blockCopy = new int[blocks.length][blocks.length];
        final BlockVisitor copier = new BlockVisitor() {
            public void visit(int row, int column) {
                blockCopy[row][column] = blocks[row][column];
            }
        };
        this.visitBlocks(copier);
        return blockCopy;
    }

    private interface BlockVisitor {
        void visit(int row, int column);
    }

    private class MisMatchVisitor implements BlockVisitor {
        private final BlockVisitor innerVisitor;

        private MisMatchVisitor(final BlockVisitor visitor) {
            this.innerVisitor = visitor;
        }

        @Override
        public void visit(int row, int column) {
            final int value = blocks[row][column];
            if (value != row * blocks.length + column + 1 && value != 0) {
                innerVisitor.visit(row, column);
            }
        }
    }

    private class HammingMatcher implements BlockVisitor {
        private int counter = 0;

        @Override
        public void visit(int row, int column) {
            counter++;
        }
    }

    private class ManhattanMatcher implements BlockVisitor {
        private int counter = 0;

        @Override
        public void visit(int row, int column) {
            final int value = blocks[row][column];
            counter += Math.abs(row - value / blocks.length)
                    +  Math.abs(column - value % blocks.length - 1);
        }
    }

    private void visitBlocks(final BlockVisitor visitor) {
        for (int row = 0; row < blocks.length; row++) {
            for (int column = 0; column < blocks.length; column++) {
                visitor.visit(row, column);
            }
        }
    }

    private void iterateMisMatchBlocks(final BlockVisitor visitor) {
        visitBlocks(new MisMatchVisitor(visitor));
    }
}
