package info.nagarajn.srjapp;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class ReadFileTask extends AsyncTask<String, Void, List<String>> {

    private FileReadListener fileReadListener;

    public ReadFileTask(FileReadListener fileReadListener) {
        this.fileReadListener = fileReadListener;
    }

    @Override
    protected List<String> doInBackground(String... args) {
        List<String> strings = new ArrayList<>();

        try {
            FileOperations.writeFile(args[0], "sri rama jayam");
            strings = FileOperations.readFile(args[0]);
        } catch (Exception e) {
            e.printStackTrace();
            FileOperations.writeFile(args[0], "sri rama jayam");
        }

        return strings;
    }

    @Override
    protected void onPostExecute(List<String> stringList) {
        fileReadListener.onFileReadComplete(stringList);
    }
}