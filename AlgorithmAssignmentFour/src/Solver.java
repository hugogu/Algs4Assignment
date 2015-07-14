import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

/**
 *
 * @author Qiangqiang Gu
 */
public class Solver {
    private static final Comparator<Tracable<Board>> manhattan = new Comparator<Tracable<Board>>() {
        @Override
        public int compare(Tracable<Board> x, Tracable<Board> y) {
            if (x.depth == y.depth)
                return Integer.compare(x.value.manhattan(), y.value.manhattan());
            return Integer.compare(x.depth, y.depth);
        }
    };
    private final MinPQ<Tracable<Board>> queue = new MinPQ<Tracable<Board>>(manhattan);
    private final ArrayList<Board> solution = new ArrayList<Board>();

    public Solver(final Board initial) {
        queue.insert(new Tracable<Board>(initial, null));
        Tracable<Board> result = resolve();
        while(result != null) {
            solution.add(0, result.value);
            result = result.parent;
        }
    }

    public boolean isSolvable() {
        return moves() > 0;
    }

    public int moves() {
        return solution.size() - 1;
    }

    public Iterable<Board> solution() {
        return solution;
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
        while(!queue.isEmpty()) {
            final Tracable<Board> current = queue.delMin();
            for (final Board next : current.value.neighbors()) {
                if (next.isGoal())
                    return new Tracable<Board>(next, current);

                if (next.manhattan() <= 2 && next.twin().isGoal())
                    return null;
                
                if (contains(current, next))
                    continue;

                queue.insert(new Tracable<Board>(next, current));
            }
        }
        return null;
    }
    
    private static <T> boolean contains(final Iterable<T> iterable, final T item) {
        if (iterable == null)
            return false;

        final Iterator<T> iterator = iterable.iterator();
        while(iterator.hasNext()) {
            if (iterator.next().equals(item)) {
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
