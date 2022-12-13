package problemtwo.minheap;

import java.util.ArrayList;
import java.util.List;

public class BinaryMinHeap<T extends Comparable<T>> {
    private final ArrayList<T> heap;
    private int size;

    public BinaryMinHeap() {
        this.heap = new ArrayList<>();
        this.size = 0;
    }

    public BinaryMinHeap(List<T> heap) {
        this.heap = (ArrayList<T>) heap;
        this.size = heap.size();
    }

    private void heapify() {
        try {
            for (int i = size / 2 - 1; i > -1; i--) {
                int current = i;
                int left = 2 * i + 1;
                int right = left + 1;

                while (left < size && heap.get(left).compareTo(heap.get(current)) < 0)
                    current = left;
                while (right < size && heap.get(right).compareTo(heap.get(current)) < 0)
                    current = right;
                if (current != i)
                    swap(i, current);
            }
        } catch (Exception ignored) {
        }
    }

    private void swap(int i, int current) {
        T temp = heap.get(i);
        heap.set(i, heap.get(current));
        heap.set(current, temp);
    }

    public void add(T element) {
        heap.add(element);
        size++;
        int parent = (size / 2) - 1;
        int child = size - 1;
        while (size > 1 && heap.get(parent).compareTo(heap.get(child)) > 0) {
            swap(child, parent);
            child = parent;
            parent = (child - 1) / 2;
        }
        heapify();
    }

    public T poll() {
        T element = heap.get(0);
        System.out.println(((HeapNode<T>) heap.get(0)).getElement());
        heap.set(0, heap.get(--size));
        heapify();
        System.out.println(heap);
        heap.set(size + 1, element);
        return element;
    }

    public boolean remove(T element) {
        if (this.heap.contains(element)) {
            heap.set(heap.indexOf(element), heap.get(0));
            heapify();
            poll();
            return true;
        }
        return false;
    }
}
