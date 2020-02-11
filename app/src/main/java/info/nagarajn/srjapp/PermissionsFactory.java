package info.nagarajn.srjapp;

import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionsFactory {

    public static boolean isPermissionGranted(AppCompatActivity context, String permission) {
        return (ContextCompat.checkSelfPermission(context,
                permission)
                == PackageManager.PERMISSION_GRANTED);
    }

    public static void requestPermission(AppCompatActivity context, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(context, permissions,
                requestCode);
    }
}
