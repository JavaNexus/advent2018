package pl.javanexus.year2018.day8;

import java.util.Arrays;

public class Node {

    private final int[] metadata;
    private final Node[] children;

    private int parsedChildren = 0;

    public Node(int numberOfChildren, int numberOfMetadata) {
        this.children = new Node[numberOfChildren];
        this.metadata = new int[numberOfMetadata];
    }

    public int getNumberOfChildren() {
        return children.length;
    }

    public int getNumberOfMetadata() {
        return metadata.length;
    }

    public void addMetadata(int i, int metadata) {
        this.metadata[i] = metadata;
    }

    public void addChild(Node childNode) {
        this.children[parsedChildren] = childNode;
        parsedChildren++;
    }

    public boolean areAllChildrenParsed() {
        return parsedChildren == children.length;
    }

    public int getMetadataSum() {
        return Arrays.stream(metadata).sum();
    }

    public int getTotalMetadataSum() {
        int sum = getMetadataSum();
        sum += Arrays.stream(children).mapToInt(Node::getTotalMetadataSum).sum();

        return sum;
    }

    public int getValue() {
        int value = 0;
        if (children.length == 0) {
            value = Arrays.stream(metadata).sum();
        } else {
            for (int m : metadata) {
                if (m - 1 < children.length) {
                    value += children[m - 1].getValue();
                }
            }
        }

        return value;
    }

    @Override
    public String toString() {
        return "Node{" +
                "metadata=" + metadata.length +
                ", children=" + children.length +
                '}';
    }
}
