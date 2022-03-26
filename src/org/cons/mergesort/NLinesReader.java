package org.cons.mergesort;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NLinesReader implements AutoCloseable{

    private final BufferedReader reader;

    public NLinesReader(BufferedReader reader) {
        this.reader = reader;
    }

    public List<String> readNLines(int maxLinesCount) throws IOException {
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
            if(lines.size() >= maxLinesCount) break;
        }
        return lines;
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
