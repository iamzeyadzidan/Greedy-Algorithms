package huffman;

import java.util.ArrayList;
import java.util.List;

public class HuffmanNode<T extends Comparable<T>> implements Comparable<HuffmanNode<T>> {
    private final T element;
    private final ArrayList<HuffmanNode<T>> children;
    private int frequency;

    public HuffmanNode() {
        element = null;
        frequency = 1;
        children = new ArrayList<>();
    }

    public HuffmanNode(T element, int frequency) {
        this.element = element;
        this.frequency = frequency;
        children = new ArrayList<>();
    }

    public T getElement() {
        return element;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public void addToChildren(HuffmanNode<T> node) {
        if (children.size() < 2)
            children.add(node);
    }

    public HuffmanNode<T> getLeft() {
        try {
            return children.get(0);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public HuffmanNode<T> getRight() {
        try {
            return children.get(1);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public List<HuffmanNode<T>> getChildren() {
        return this.children;
    }

    @Override
    public int compareTo(HuffmanNode o) {
        return Integer.compare(frequency, o.frequency);
    }

    @Override
    public String toString() {
        try {
            return "E = " + element.toString() + ", F = " + frequency;
        } catch (Exception e) {
            return "E = " + ", F = " + frequency;
        }
    }
}
