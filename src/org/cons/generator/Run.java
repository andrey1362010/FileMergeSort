package org.cons.generator;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Run {
    private static int ROWS_COUNT = 11_111_111;
    private static int MAX_ROWS_SIZE = 100;
    private static int MIN_ROWS_SIZE = 10;
    private static String OUTPUT_PATH = "/media/andrey/big/datasets/MS/data.txt";

    private static String randomString() {
        Random random = new Random();
        int strSize = random.nextInt(MAX_ROWS_SIZE - MIN_ROWS_SIZE) + MIN_ROWS_SIZE;
        StringBuilder buffer = new StringBuilder(strSize);
        int from = 'a';
        int to = 'z';
        for(int i=0; i< strSize; i++) {
            char selectedChar = (char) (random.nextInt(to - from) + from);
            buffer.append(selectedChar);
        }
        return buffer.toString();
    }

    public static void main(String[] args) throws IOException {
        try(FileWriter fileWriter = new FileWriter(OUTPUT_PATH)) {
            for (int count = 0; count < ROWS_COUNT; count++) {
                fileWriter.write(randomString());
                fileWriter.write("\n");
            }
        }
    }
}
