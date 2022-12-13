package huffman.minheap;

import java.util.ArrayList;

public class HeapNode<T extends Comparable<T>> implements Comparable<HeapNode<T>> {
    private T element;
    private ArrayList<HeapNode<T>> children;

    public HeapNode() {
        this.element = null;
        this.children = new ArrayList<>();
    }

    public HeapNode(T element) {
        this.element = element;
        this.children = new ArrayList<>();
        this.children.add(null);
        this.children.add(null);
    }

    public T getElement() {
        return this.element;
    }

    public void setElement(T element) {
        this.element = element;
    }

    public ArrayList<HeapNode<T>> getChildren() {
        return this.children;
    }

    public void setChildren(ArrayList<HeapNode<T>> children) {
        this.children = children;
    }

    public HeapNode<T> getLeft() {
        return this.children.get(0);
    }

    public void setLeft(HeapNode<T> left) {
        this.children.set(0, left);
    }

    public HeapNode<T> getRight() {
        return this.children.get(1);
    }

    public void setRight(HeapNode<T> right) {
        this.children.set(1, right);
    }

    @Override
    public int compareTo(HeapNode<T> o) {
        if (this.element.compareTo(o.getElement()) > 0) {
            return 1;
        } else if (this.element.compareTo(o.getElement()) < 0) {
            return -1;
        } else if (this.element.compareTo(o.getElement()) == 0) {
            return 0;
        }
        return 0;
    }
}
