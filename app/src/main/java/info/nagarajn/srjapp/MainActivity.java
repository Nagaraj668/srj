package info.nagarajn.srjapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
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

        if (!checkPrefs()) {
            DailyJobService.schedule(this, DailyJobService.ONE_DAY_INTERVAL);
            updatePrefs();
        }

        readFile();

        // addNotification();
    }


    void updatePrefs() {
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("job_started", true);
        editor.apply();
    }

    boolean checkPrefs() {
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", MODE_PRIVATE);
        return sharedPreferences.getBoolean("job_started", false);
    }

    private void addNotification() {
        String CHANNEL_ID = "asd";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("notify")
                .setContentText("notify")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(1, builder.build());
        }
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
