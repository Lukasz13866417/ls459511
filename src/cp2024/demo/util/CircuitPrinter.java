package cp2024.demo.util;

import cp2024.circuit.CircuitNode;
import cp2024.circuit.NodeType;

public class CircuitPrinter {

    /**
     * Prints the given CircuitNode in a human-readable format.
     *
     * @param node the root CircuitNode to print
     * @return a string representation of the circuit
     */
    public static String printCircuit(CircuitNode node) {
        if (node == null) {
            return "null";
        }

        NodeType type = node.getType();
        try {
            // Handle leaf nodes
            if (type == NodeType.LEAF) {
                return handleLeafNode(node);
            }

            // Handle other node types recursively
            StringBuilder sb = new StringBuilder();
            sb.append(type.name()).append("(");

            CircuitNode[] args = node.getArgs();
            for (int i = 0; i < args.length; i++) {
                sb.append(printCircuit(args[i]));
                if (i < args.length - 1) {
                    sb.append(",");
                }
            }
            sb.append(")");
            return sb.toString();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "[Interrupted]";
        }
    }

    /**
     * Handles leaf nodes (TRUE/FALSE values).
     *
     * @param node the leaf node
     * @return a string representation of the leaf node
     */
    private static String handleLeafNode(CircuitNode node) throws InterruptedException {
        if (node instanceof cp2024.circuit.LeafNode) {
            boolean value = ((cp2024.circuit.LeafNode) node).value();
            return value ? "TRUE" : "FALSE";
        }
        return "[Unknown Leaf Node]";
    }

    public static void main(String[] args) {
        try {
            // Example CircuitNode for testing
            CircuitNode circuit = CircuitNode.mk(NodeType.AND,
                CircuitNode.mk(NodeType.OR, CircuitNode.mk(true), CircuitNode.mk(NodeType.AND, CircuitNode.mk(false), CircuitNode.mk(false))),
                CircuitNode.mk(NodeType.IF, CircuitNode.mk(true), CircuitNode.mk(true), CircuitNode.mk(true))
            );

            // Print the circuit
            System.out.println("Circuit Structure:");
            System.out.println(printCircuit(circuit));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
