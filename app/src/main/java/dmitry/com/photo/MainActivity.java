package dmitry.com.photo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    private View main;
    private ImageView imageView;

    String TAG = "mylog";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main = findViewById(R.id.main);
        Button button = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Bitmap b = Screenshot.takeScreenshotOfRootView(imageView);
                imageView.setImageBitmap(b);
                main.setBackgroundColor(Color.parseColor("#999999"));

                saveBitmapToGallery(b, "KEKEKEK");



            }
        });


    }

    private void saveBitmapToGallery(Bitmap bitmap, String fName) {

        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest
                .permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest
                .permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return;
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        }

        String root = Environment.getExternalStoragePublicDirectory(Environment
                .DIRECTORY_PICTURES).toString();

        Log.d(TAG, "Корневая директория = " + root);
        File myDir = new File(root + "/SavedImg");
        myDir.mkdirs();
        fName += ".jpg";
        File file = new File(myDir, fName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String path, Uri uri) {
                Log.d(TAG, "scanned " + path + ": ");
                Log.d(TAG, "Uri = " + uri);

            }
        });


    }

//        Glide.with(this).load("http://goo.gl/gEgYUd").into(imageView);



}




