package info.nagarajn.srjapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileOperations {

    public static List<String> readFile(String path) {
        List<String> strings = new ArrayList<>();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(path)));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                strings.add(line);
                System.out.println("reading: " + line);
            }
            System.out.println("length: " + strings.size());
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            writeFile(path, "sri rama jayam");
        }

        return strings;
    }

    public static void writeFile(String path, String text) {
        try {
            File file = new File(path);

            if (!file.exists())
                file.createNewFile();

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            bufferedWriter.write(text);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
