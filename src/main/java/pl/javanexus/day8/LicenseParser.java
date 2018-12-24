package pl.javanexus.day8;

import java.util.Stack;

public class LicenseParser {

    /**
     * 2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2
     *
     * @param license
     * @return
     */
    public Node parseLicense(String license) {
        final Node root = new Node(1, 1);
        root.addMetadata(0, 1);

        final Stack<Node> nodes = new Stack<>();
        nodes.push(root);

        int i = 0;
        String[] numbers = license.split(" ");
        while (i < numbers.length) {
            Node parent = nodes.pop();
            if (parent.getNumberOfChildren() > 0 && !parent.areAllChildrenParsed()) {
                Node childNode = parseNode(i, numbers);
                parent.addChild(childNode);

                nodes.push(parent);
                nodes.push(childNode);

                i += 2;
            } else {
                for (int metadataIndex = 0; metadataIndex < parent.getNumberOfMetadata(); metadataIndex++) {
                    parent.addMetadata(metadataIndex, Integer.parseInt(numbers[i++]));
                }
            }
        }

        return root;
    }

    private Node parseNode(int i, String[] numbers) {
        int numberOfChildren = Integer.parseInt(numbers[i]);
        int numberOfMetadata = Integer.parseInt(numbers[i + 1]);

        return new Node(numberOfChildren, numberOfMetadata);
    }
}
