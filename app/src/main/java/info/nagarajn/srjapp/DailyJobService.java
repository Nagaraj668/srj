package info.nagarajn.srjapp;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import java.util.Date;

public class DailyJobService extends JobService {
    private static final String TAG = "DailyJobService";
    boolean isWorking = false;
    boolean jobCancelled = false;

    private static final int JOB_ID = 1;
    public static final long ONE_DAY_INTERVAL =  16 * 60 * 1000; // 1 Day

    public static void schedule(Context context, long intervalMillis) {
        JobScheduler jobScheduler = (JobScheduler)
                context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        ComponentName componentName =
                new ComponentName(context, DailyJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, componentName);
        builder.setPeriodic(intervalMillis);
        jobScheduler.schedule(builder.build());
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d(TAG, "Job started!");
        isWorking = true;
        startWorkOnNewThread(params); // Services do NOT run on a separate thread
        return isWorking;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d(TAG, "Job cancelled before being completed.");
        jobCancelled = true;
        boolean needsReschedule = isWorking;
        jobFinished(params, needsReschedule);
        return needsReschedule;
    }

    private void startWorkOnNewThread(final JobParameters jobParameters) {

        notificationService();

        new Thread() {
            @Override
            public void run() {
                String path = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                        .getAbsolutePath() + "/srj/sri_rama_jayam.txt";

                FileOperations.writeFile(path, new Date().toString());
                for (int i = 0; i < 11; i++) {
                    FileOperations.writeFile(path, "sri rama jayam");
                }
            }
        }.start();
    }

    private void notificationService() {
        Intent intent = new Intent(this, NotificationIntentService.class);
        startService(intent);
    }
}
