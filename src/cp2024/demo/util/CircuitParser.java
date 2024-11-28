package cp2024.demo.util;

import cp2024.circuit.CircuitNode;
import cp2024.circuit.NodeType;

import java.util.ArrayList;
import java.util.List;

public class CircuitParser {

    // Entry point for parsing a circuit from a string
    public static CircuitNode parse(String input) {
        input = input.trim();

        if (input.equals("TRUE")) {
            return CircuitNode.mk(true); // LeafNode for TRUE
        } else if (input.equals("FALSE")) {
            return CircuitNode.mk(false); // LeafNode for FALSE
        }

        // Extract the type of node (e.g., AND, OR, etc.)
        int openParen = input.indexOf('(');
        if (openParen == -1 || !input.endsWith(")")) {
            throw new IllegalArgumentException("Invalid input format: " + input);
        }

        String typeStr = input.substring(0, openParen).trim();
        NodeType type;
        try {
            type = NodeType.valueOf(typeStr); // Get the NodeType enum
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown node type: " + typeStr);
        }

        // Extract the arguments inside the parentheses
        String argsStr = input.substring(openParen + 1, input.length() - 1).trim();
        List<String> arguments = splitArguments(argsStr);

        // Recursively parse arguments into CircuitNodes
        List<CircuitNode> childNodes = new ArrayList<>();
        for (String arg : arguments) {
            childNodes.add(parse(arg));
        }

        // Create and return the CircuitNode
        return CircuitNode.mk(type, childNodes.toArray(new CircuitNode[0]));
    }

    // Helper method to split arguments by commas while handling nested parentheses
    private static List<String> splitArguments(String argsStr) {
        List<String> arguments = new ArrayList<>();
        StringBuilder currentArg = new StringBuilder();
        int depth = 0;

        for (char c : argsStr.toCharArray()) {
            if (c == ',' && depth == 0) {
                arguments.add(currentArg.toString().trim());
                currentArg.setLength(0); // Clear the StringBuilder
            } else {
                currentArg.append(c);
                if (c == '(') depth++;
                if (c == ')') depth--;
            }
        }

        // Add the last argument
        if (currentArg.length() > 0) {
            arguments.add(currentArg.toString().trim());
        }

        return arguments;
    }

    // Test the parser
    public static void main(String[] args) {
        String input = "AND(OR(TRUE,AND(FALSE,FALSE)),IF(TRUE,TRUE,TRUE))";

        try {
            CircuitNode root = parse(input);
            System.out.println("Parsed circuit successfully: " + CircuitPrinter.printCircuit(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
