package com.niken.assignment.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {
    public static Bitmap scaleDownLargeBitmap(@NonNull Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float aspectRatio = (float) width / (float) height;

        Bitmap resultBitmap = bitmap;
        if (aspectRatio > 1 && width > 800) {

            // For wide images - set maximum width to 800px and scale down height maintaining aspect ratio
            int scaledWidth = 800;
            int scaledHeight = Math.round(scaledWidth / aspectRatio);
            resultBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, false);

        } else if (aspectRatio < 1 && height > 800) {

            // For tall images
            int scaledHeight = 800;
            int scaledWidth = Math.round(scaledHeight * aspectRatio);
            resultBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, false);
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);



        return resultBitmap;
    }

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static Uri saveBitmapAndGetUri(Context context, Bitmap bitmap, String fileName) {
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Toast.makeText(context, "Cannot access device storage", Toast.LENGTH_SHORT).show();
            return null;
        }

        try {

            File directory = new File(context.getExternalFilesDir(""), "data/images");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File file = new File(context.getExternalFilesDir("data/images"), fileName); // the File to save
            FileOutputStream fOut = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush(); // Not really required
            fOut.close(); // do not forget to close the stream

            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());

            return Uri.fromFile(file);
        } catch (IOException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return null;
        }
    }
}
