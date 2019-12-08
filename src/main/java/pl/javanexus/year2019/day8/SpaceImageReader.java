package pl.javanexus.year2019.day8;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SpaceImageReader {

    public static final int BLACK = 0;
    public static final int WHITE = 1;
    public static final int TRANSPARENT = 2;

    public List<int[][]> readInput(int width, int height, String input) {
        int[] pixels = Arrays.stream(input.split("")).mapToInt(Integer::parseInt).toArray();
        return readInput(width, height, pixels);
    }

    public List<int[][]> readInput(int width, int height, int[] pixels) {
        final int layerLength = width * height;

        List<int[][]> layers = new LinkedList<>();
        for (int i = 0; i < pixels.length; i += layerLength) {
            int[][] layer = getLayer(width, height);
            fillLayer(layer, pixels, i, width);
            layers.add(layer);

//            printLayer(layer);
        }

        return layers;
    }

    public int getLayerWithMinNumberOfDigit(List<int[][]> layers, int digit) {
        int minNumberOfDigit = -1;
        int result = -1;
        for (int[][] layer : layers) {
            int[] digitsCount = countDigits(layer);
            int numberOfDigit = digitsCount[digit];
            if (minNumberOfDigit == -1 || numberOfDigit < minNumberOfDigit) {
                minNumberOfDigit = numberOfDigit;
                result = digitsCount[1] * digitsCount[2];
            }
        }

        return result;
    }

    public void decode(List<int[][]> layers, int width, int height) {
        int[][] decodedImage = getLayer(width, height);
        for (int y = 0; y < decodedImage.length; y++) {
            for (int x = 0; x < decodedImage[y].length; x++) {
                decodedImage[y][x] = getFirstOpaquePixel(layers, x, y);
            }
        }

        printLayer(decodedImage);
    }

    private int[][] getLayer(int width, int height) {
        int[][] layer = new int[height][];
        for (int y = 0; y < layer.length; y++) {
            layer[y] = new int[width];
        }

        return layer;
    }

    private void fillLayer(int[][] layer, int[] pixels, int from, int width) {
        for (int y = 0; y < layer.length; y++) {
            for (int x = 0; x < layer[y].length; x++) {
                layer[y][x] = pixels[from + y * width + x];
            }
        }
    }

    private void printLayer(int[][] layer) {
        for (int y = 0; y < layer.length; y++) {
            System.out.println(Arrays.toString(layer[y]));
        }
    }

    private int[] countDigits(int[][] layer) {
        int[] digits = new int[10];
        Arrays.fill(digits, 0);

        for (int y = 0; y < layer.length; y++) {
            for (int x = 0; x < layer[y].length; x++) {
                int digit = layer[y][x];
                digits[digit]++;
            }
        }

        return digits;
    }

    /**
     * 0 is black, 1 is white, and 2 is transparent
     *
     * @param layers
     * @param x
     * @param y
     * @return
     */
    private int getFirstOpaquePixel(List<int[][]> layers, int x, int y) {
        int pixel = TRANSPARENT;
        Iterator<int[][]> iterator = layers.iterator();
        while (pixel == TRANSPARENT && iterator.hasNext()) {
            pixel = iterator.next()[y][x];
        }

        return pixel;
    }
}
