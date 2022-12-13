package huffman;

import huffman.files.Compressor;
import huffman.files.Decompressor;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class Huffman {
    public static Map<String, String> prefixCode = new HashMap<>();

    public static void main(String[] args) {
        String operation = args[0];
        long start = System.currentTimeMillis();
        switch (operation) {
            case "c" ->
                    System.out.println(Compressor.compress(Integer.parseInt(args[2]), args[1]));    // False means an error has occurred.
            case "d" -> System.out.println(Decompressor.decompress(args[1]));   // False means an error has occurred.
            default -> System.out.println("Error: Not a valid operation.");
        }
        long end = System.currentTimeMillis();
        System.out.println("Time: " + (end - start) + "ms");
    }

    public static HuffmanNode<String> constructHuffmanTree(Map<String, Integer> frequencies) {
        PriorityQueue<HuffmanNode<String>> heap = new PriorityQueue<>(Comparator.comparingInt(HuffmanNode::getFrequency));
        for (Map.Entry<String, Integer> entry : frequencies.entrySet()) {
            heap.add(new HuffmanNode<>(entry.getKey(), entry.getValue()));
        }
        HuffmanNode<String> node;
        while (heap.size() > 1) {
            node = new HuffmanNode<>();
            node.addToChildren(heap.poll());
            node.addToChildren(heap.poll());
            node.setFrequency((node.getLeft().getFrequency() + node.getRight().getFrequency()));
            heap.add(node);
        }
        return heap.poll();
    }

    public static void constructPrefixCode(HuffmanNode root, StringBuilder bits) {
        if (root.getChildren().isEmpty()) {
            prefixCode.put(root.getElement().toString(), bits.toString());
        } else {
            constructPrefixCode(root.getLeft(), bits.append(0));
            bits.deleteCharAt(bits.length() - 1);
            if (root.getChildren().size() > 1) {
                constructPrefixCode(root.getRight(), bits.append(1));
                bits.deleteCharAt(bits.length() - 1);
            }
        }
    }
}
