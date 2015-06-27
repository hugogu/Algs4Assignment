
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author Qiangqiang Gu
 */
public class Deque<Item> implements Iterable<Item> {
    private Node<Item> first = null;
    private Node<Item> last = null;
    private int count = 0;

    private class Node<Item> {
        private Item value;
        private Node<Item> next;
        private Node<Item> previous;
    }

    public Deque() {
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public int size() {
        return count;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        final Node<Item> node = new Node<Item>();
        node.value = item;
        node.next = first;
        if (first != null) {
            first.previous = node;
        }
        first = node;
        if (last == null) {
            last = node;
        }
        count++;
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        final Node<Item> node = new Node<Item>();
        node.value = item;
        node.previous = last;
        if (last != null) {
            last.next = node;
        }
        last = node;
        if (first == null) {
            first = node;
        }
        count++;
    }

    public Item removeFirst() {
        if (first == null) {
            throw new NoSuchElementException();
        }
        final Item item = first.value;
        final Node<Item> next = first.next;
        first.next = null;
        first.previous = null;
        first = next;
        if (next == null) {
            last = null;
        }
        count--;
        return item;
    }

    public Item removeLast() {
        if (last == null) {
            throw new NoSuchElementException();
        }
        final Item item = last.value;
        final Node<Item> previous = last.previous;
        last.previous = null;
        last.next = null;
        last = previous;
        if (previous == null) {
            first = null;
        }
        count--;
        return item;
    }

    public Iterator<Item> iterator() {
        final Node<Item> start = new Node<Item>();
        start.next = first;
        return new Iterator<Item>() {
            private Node<Item> current = start;

            @Override
            public boolean hasNext() {
                return current.next != null;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove");
            }

            @Override
            public Item next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                current = current.next;
                return current.value;
            }
        };
    }

    public static void main(String[] args) {
        final Deque<Integer> queue = new Deque<Integer>();
        assert queue.isEmpty();
        queue.addFirst(1);
        assert !queue.isEmpty();
        final int pop = queue.removeLast();
        assert 1 == pop;
        assert queue.isEmpty();
        queue.addLast(1);
        queue.addFirst(2);
        queue.addLast(3);
        final int last = queue.removeLast();
        assert 3 == last;
    }
}
