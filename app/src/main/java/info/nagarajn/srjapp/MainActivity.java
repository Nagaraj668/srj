package info.nagarajn.srjapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FileReadListener {

    private static final int PERMISSION_CODE = 1;
    private RecyclerView recyclerView;
    private SrjRecyclerViewAdapter srjRecyclerViewAdapter;
    private List<String> stringList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.srj_list);
        stringList = new ArrayList<>();
        srjRecyclerViewAdapter = new SrjRecyclerViewAdapter(stringList, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(srjRecyclerViewAdapter);


        DailyJobService.schedule(this, DailyJobService.ONE_DAY_INTERVAL);

        readFile();
    }

    private void readFile() {
        if (!PermissionsFactory.isPermissionGranted(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            String[] permissions = {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            PermissionsFactory.requestPermission(this, permissions, PERMISSION_CODE);
            return;
        }

        (new ReadFileTask(this)).execute(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                .getAbsolutePath() + "/srj/sri_rama_jayam.txt");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            readFile();
        } else {
            Toast.makeText(this, "Please provide file access permission",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFileReadComplete(List<String> stringList) {
        this.stringList = stringList;
        srjRecyclerViewAdapter.setStringList(stringList);
    }
}
