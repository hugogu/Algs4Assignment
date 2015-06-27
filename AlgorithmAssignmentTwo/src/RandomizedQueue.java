
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author Qiangqiang Gu
 */
public class RandomizedQueue<Item> implements Iterable<Item> {
    private final ArrayList<Item> list = new ArrayList<Item>();

    private class ArrayList<T> {
        private int count = 0;
        private int head = 0;
        private T[] items = null;

        private ArrayList() {
            items = (T[]) new Object[1];
        }

        private ArrayList(final ArrayList<T> another) {
            items = copyTo(another.items, (T[])new Object[another.size()]);
            head = items.length;
            count = items.length;
        }

        private int size() {
            return count;
        }

        private void add(final T item) {
            if (head >= items.length) {
                resizeTo(items.length * 2);
            }
            items[head] = item;
            head++;
            count++;
        }

        private T remove(final int index) {
            final T value = get(index);
            if (value == null) {
                throw new UnsupportedOperationException();
            }
            items[index] = null;
            count--;
            if (count < items.length / 4) {
                resizeTo(items.length / 2);
            }
            return value;
        }

        private T get(final int index) {
            return items[index];
        }

        private void resizeTo(final int newSize) {
            final T[] newItems = (T[]) new Object[newSize];
            items = copyTo(items, newItems);
            head = count;
        }

        private T[] copyTo(final T[] existingItems, final T[] newItems) {
            int j = 0;
            for (int i = 0; i < existingItems.length; i++) {
                final T value = existingItems[i];
                if (value != null) {
                    newItems[j++] = value;
                }
            }
            return newItems;
        }
    }

    public RandomizedQueue() {
    }

    public boolean isEmpty() {
        return list.count == 0;
    }

    public int size() {
        return list.count;
    }

    public void enqueue(final Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        list.add(item);
    }

    public Item dequeue() {
        return list.remove(sampleIndex(list));
    }

    public Item sample() {
        return list.get(sampleIndex(list));
    }

    public Iterator<Item> iterator() {
        final ArrayList<Item> iteration = new ArrayList<Item>(list);
        StdRandom.shuffle(iteration.items);
        return new Iterator<Item>() {
            private int pointer = list.count - 1;
            @Override
            public boolean hasNext() {
                return pointer >= 0;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove");
            }

            @Override
            public Item next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                return iteration.remove(pointer--);
            }
        };
    }

    private int sampleIndex(final ArrayList<Item> data) {
        if (data.count == 0) {
            throw new NoSuchElementException();
        }
        int index = StdRandom.uniform(data.count);
        while (data.get(index) == null) {
            index++;
            if (index >= data.items.length) {
                index = 0;
            }
        }
        return index;
    }

    public static void main(final String[] args) {
        final RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        for (int i = 0; i < 10; i++) {
            queue.enqueue(i);
        }
        while (queue.size() > 0) {
            StdOut.println(queue.dequeue());
        }
        for (int i = 0; i < 10; i++) {
            queue.enqueue(i);
        }
        for (final Integer value : queue) {
            StdOut.println(value);
        }
    }
}
