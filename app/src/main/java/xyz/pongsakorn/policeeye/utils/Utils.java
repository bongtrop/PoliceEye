package xyz.pongsakorn.policeeye.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Porpeeranut on 11/4/2559.
 */
public class Utils {

    public static final String file_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/PoliceEye";

    public static void saveBitmapToJPG(Context context, String fileName, Bitmap bm) {
        try {
            File dir = new File(file_path);
            if (!dir.exists())
                dir.mkdirs();

            File file = new File(dir, fileName);
            FileOutputStream fOut = null;
            fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, fOut);
            fOut.flush();
            fOut.close();
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteJpgFile(Context context, String fileName) {
        File dir = new File(file_path);
        File file = new File(dir, fileName);
        file.delete();
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    }

    public static Bitmap getSketchJpgFile(String filePath, String fileName) {
        File file = new File(filePath, fileName);
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    public static void saveSketchCacheFile(Context context, String name, Bitmap bm) {
        FileOutputStream outputStream = null;
        try {
            int bytes = bm.getByteCount();
            ByteBuffer buffer = ByteBuffer.allocate(bytes);
            bm.copyPixelsToBuffer(buffer);
            byte[] array = buffer.array();
            outputStream = context.openFileOutput(name, Context.MODE_PRIVATE);
            outputStream.write(array);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*File file;
        FileOutputStream outputStream;
        try {
            int bytes = bm.getByteCount();
            ByteBuffer buffer = ByteBuffer.allocate(bytes);
            bm.copyPixelsToBuffer(buffer);
            byte[] array = buffer.array();
            file = new File(getCacheDir(), name);

            outputStream = new FileOutputStream(file);
            outputStream.write(array);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public static Bitmap getSketchCacheFile(Context context, String filePath) {
        File file = new File(context.getFilesDir(), "jaw");
        return BitmapFactory.decodeFile(file.getAbsolutePath());
        /*File file;
        try {
            String fileName = Uri.parse(url).getLastPathSegment();
            file = File.createTempFile(fileName, null, context.getCacheDir());
        } catch (IOException e) {
            // Error while creating file
        }
        return file;*/

        /*BufferedReader input = null;
        File file = null;
        try {
            file = new File(getFilesDir(), "jaw");
            BitmapFactory.decodeFile(image,bmOptions);

            input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = input.readLine()) != null) {
                buffer.append(line);
            }

            Log.d(TAG, buffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public static String createPhotoName() {
        return new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss'.jpg'").format(new Date());
    }
}
