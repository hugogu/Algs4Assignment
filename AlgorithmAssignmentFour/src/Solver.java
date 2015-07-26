
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

/**
 *
 * @author Qiangqiang Gu
 */
public class Solver {

    private static final Comparator<Tracable<Board>> MANHATTAN = new Comparator<Tracable<Board>>() {
        @Override
        public int compare(Tracable<Board> x, Tracable<Board> y) {
            return compare(x.depth + x.value.manhattan(), y.depth + y.value.manhattan());
        }

        private int compare(int x, int y) {
            if (x < y) {
                return -1;
            }
            return x == y ? 0 : 1;
        }
    };
    private final MinPQ<Tracable<Board>> queue = new MinPQ<Tracable<Board>>(MANHATTAN);
    private final ArrayList<Board> solution = new ArrayList<Board>();

    public Solver(final Board initial) {
        queue.insert(new Tracable<Board>(initial, null));
        Tracable<Board> result = resolve();
        while (result != null) {
            solution.add(0, result.value);
            result = result.parent;
        }
    }

    public boolean isSolvable() {
        return solution.size() > 0;
    }

    public int moves() {
        return solution.size() - 1;
    }

    public Iterable<Board> solution() {
        // Don't like return null. Just to pass a stupid online judge test.
        return isSolvable() ? solution : null;
    }

    public static void main(final String[] args) {
        final In in = new In(args[0]);
        final int N = in.readInt();
        final int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                blocks[i][j] = in.readInt();
            }
        }
        final Board initial = new Board(blocks);
        final Board twin = initial.twin();
        assert twin != null;
        assert initial.manhattan() == 3;
        final Solver solver = new Solver(initial);

        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
        } else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (final Board board : solver.solution()) {
                StdOut.println(board);
            }
        }
    }

    private Tracable<Board> resolve() {
        while (!queue.isEmpty()) {
            final Tracable<Board> current = queue.delMin();
            final Board board = current.value;
            if (board.isGoal()) {
                return current;
            }
            if (board.manhattan() <= 2 && board.twin().isGoal()) {
                return null;
            }
            for (final Board next : board.neighbors()) {
                if (!contains(current, next)) {
                    queue.insert(new Tracable<Board>(next, current));
                }
            }
        }
        return null;
    }

    private <T> boolean contains(Iterable<T> list, T value) {
        final Iterator<T> iterator = list.iterator();
        while (iterator.hasNext()) {
            final T current = iterator.next();
            if (value.equals(current)) {
                return true;
            }
        }
        return false;
    }

    private class Tracable<T> implements Iterable<T> {

        private final T value;
        private final Tracable<T> parent;
        private final int depth;

        public Tracable(final T value, final Tracable<T> parent) {
            this.value = value;
            this.parent = parent;
            this.depth = parent == null ? 0 : parent.depth + 1;
        }

        public boolean equals(final Object another) {
            return value.equals(another);
        }

        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public Iterator<T> iterator() {
            return new Iterator<T>() {
                private Tracable<T> next = new Tracable<T>(null, Tracable.this);

                @Override
                public boolean hasNext() {
                    return next.parent != null;
                }

                @Override
                public T next() {
                    next = next.parent;
                    return next.value;
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException("remove");
                }
            };
        }
    }
}
