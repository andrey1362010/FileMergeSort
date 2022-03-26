package org.cons.mergesort;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Run {
    private static int MAX_ROWS_TO_PROCESS = 1000; //Максимальное количество строк помещающееся в оперативу
    private static String INPUT_PATH = "/media/andrey/big/datasets/MS/data.txt"; //Входной файл
    private static String TMP_PATH = "/media/andrey/big/datasets/MS/tmp"; //tmp папка

    public static void main(String[] args) throws IOException {
        Files.createDirectories(Paths.get(TMP_PATH));
        Queue<Path> processFilesQueue = new LinkedList<>();
        int counter = 0;

        // Читаем N=1000 строчек из файла, сортируем и записываем в отдельный файл в TMP папке
        // Повторяем операцию до конца файла
        System.out.println("RUN PRE_SORTING...");
        try(NLinesReader reader = new NLinesReader(new BufferedReader(new FileReader(INPUT_PATH)))) {
            List<String> lines;
            while (!(lines = reader.readNLines(MAX_ROWS_TO_PROCESS)).isEmpty()) {
                lines.sort(Comparator.naturalOrder());
                String saveFileString = TMP_PATH + "/" + counter;
                Path savePathPath = Path.of(saveFileString);
                Files.write(savePathPath, lines);
                processFilesQueue.add(savePathPath);
                counter += 1;
            }
        }

        // Берём 2 файла из очереди и делаем мерджинг, стырые файлы удаляем
        // Повторяем операцию пока не останется один файл
        // Он и будет итоговым результатом
        System.out.println("RUN MERGING...");
        while (processFilesQueue.size() > 1) {
            String saveFileString = TMP_PATH + "/" + counter;
            Path savePathPath = Path.of(saveFileString);
            System.out.println("PROCESS FILE:" + counter);
            mergeFiles(processFilesQueue.poll(), processFilesQueue.poll(), savePathPath);
            processFilesQueue.add(savePathPath);
            counter += 1;
        }

        System.out.println("Done");
    }


    /**
     * @param path1 Путь к первому файлу
     * @param path2 Путь ко второму файлу
     * @param outputPath Куда сохранить итоговый результат
     * @throws IOException
     *
     * Итеративно мерджим 2 файла в один и удаляем
     */
    public static void mergeFiles(Path path1, Path path2, Path outputPath) throws IOException {
        Comparator<String> comparator = Comparator.naturalOrder();
        try(BufferedReader reader1 = new BufferedReader(new FileReader(path1.toFile()));
            BufferedReader reader2 = new BufferedReader(new FileReader(path2.toFile()));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath.toFile()))) {
            String currentString1 = reader1.readLine();
            String currentString2 = reader2.readLine();
            while (currentString1 != null || currentString2 != null) {
                if(currentString1 != null) {
                    if (currentString2 == null || comparator.compare(currentString1, currentString2) <= 0) {
                        writer.write(currentString1);
                        writer.write("\n");
                        currentString1 = reader1.readLine();
                    }
                }
                if(currentString2 != null) {
                    if (currentString1 == null || comparator.compare(currentString2, currentString1) < 0) {
                        writer.write(currentString2);
                        writer.write("\n");
                        currentString2 = reader2.readLine();
                    }
                }
            }


        }
        path1.toFile().deleteOnExit();
        path2.toFile().deleteOnExit();
    }
}
