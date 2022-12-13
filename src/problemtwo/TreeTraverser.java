package problemtwo;

public class TreeTraverser {
    private TreeTraverser() {}

    public static int size = 0;

    public static void printTree(HuffmanNode root) {
        if (!root.getChildren().isEmpty()) {
            printTree(root.getLeft());
        }
        if (root.getElement() != null)
            System.out.println(root);
        if (root.getChildren().size() > 1) {
            printTree(root.getRight());
        }
    }

    public static void leavesCount(HuffmanNode root) {
        if (!root.getChildren().isEmpty()) {
            leavesCount(root.getLeft());
        }
        if (root.getElement() != null)
            size++;
        if (root.getChildren().size() > 1) {
            leavesCount(root.getRight());
        }
    }
}
