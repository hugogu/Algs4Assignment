
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Qiangqiang Gu
 */
public class Solver {
    private static final Comparator<Board> manhattan = new Comparator<Board>() {
        @Override
        public int compare(Board x, Board y) {
            return Integer.compare(x.manhattan(), y.manhattan());
        }
    };
    private final MinPQ<Board> queue = new MinPQ<Board>(manhattan);
    private final Collection<Board> solution;

    public Solver(final Board initial) {
        queue.insert(initial);
        solution = resolve(null);
    }

    private Collection<Board> resolve(final Board father) {
        final List<Board> succeeded = new LinkedList<Board>();
        final Board current = queue.delMin();
        for (final Board next : current.neighbors()) {
            if (!next.isGoal()) {
                if (!next.equals(father)) {
                    queue.insert(next);
                }
            } else {
                succeeded.add(current);
                succeeded.add(next);
                return succeeded;
            }
        }
        final Collection<Board> result = resolve(current);
        if (!result.isEmpty()) {
            succeeded.add(current);
        }
        succeeded.addAll(result);
        return succeeded;
    }

    public boolean isSolvable() {
        return true;
    }

    public int moves() {
        return solution.size();
    }

    public Iterable<Board> solution() {
        return solution;
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                blocks[i][j] = in.readInt();
            }
        }
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable()) {
            StdOut.println("No solution possible");
        } else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        }
    }
}
