/**
 *
 * @author Qiangqiang Gu
 */
public class Subset {
    public static void main(final String[] args) {
        final int subSetSize = Integer.parseInt(args[0]);
        final RandomizedQueue<String> queue = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            queue.enqueue(StdIn.readString());
        }
        int i = 0;
        while (i++ < subSetSize) {
            StdOut.println(queue.dequeue());
        }
    }
}
