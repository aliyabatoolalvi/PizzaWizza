package com.example.pizzawizza.utils;

import static android.Manifest.permission.CAMERA;
import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.util.Pair;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class MediaUtils {
    Activity context;
    private String currentPhotoPath;
    public Uri choosedImageuri;
    public boolean isFromCamera;
    MediaUtilsInterface mediaUtilsInterface;
    boolean allowFromGallery = true;
    int rqstCode;
    
    public MediaUtils(Activity context, MediaUtilsInterface mediaUtilsInterface) {
        this.context = context;
        this.mediaUtilsInterface = mediaUtilsInterface;
        this.rqstCode = new Random().nextInt(1000);
    }
    
    public MediaUtils setAllowFromGallery(boolean value) {
        allowFromGallery = value;
        return this;
    }
    
    public void startImageSelectionActivity() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            photoFile = createImageFile();
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(context,
                        context.getPackageName() + ".fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            }
        }
        
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        Intent chooser = Intent.createChooser(galleryIntent, "Pick image");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePictureIntent});
        if (allowFromGallery)
            context.startActivityForResult(chooser, rqstCode);
        else
            context.startActivityForResult(Intent.createChooser(takePictureIntent, "Pick image"), rqstCode);
        
        if (!checkPermission()) {
            requestPermission();
        }
    }

    public String getSelectedImagePath() {
        final ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver == null)
            return null;
        // Create file path inside app's data dir
        String filePath = context.getCacheDir() + File.separator + "" + System.currentTimeMillis();
        File file = new File(filePath);
        try {
            InputStream inputStream = contentResolver.openInputStream(choosedImageuri);
            Log.d("testing", "getAbsolutePath: " + URLConnection.guessContentTypeFromStream(inputStream));
            if (inputStream == null)
                return null;
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0)
                outputStream.write(buf, 0, len);
            outputStream.close();
            inputStream.close();
        } catch (IOException ignore) {
            return null;
        }
        Log.d("testingg", "getAbsolutePath: " + file.getAbsolutePath());
        return file.getAbsolutePath();
    }
    
    private File createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getCacheDir();
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    
    public void checkActivityResult(int requestCode, int resultCode, Intent data) {
        Toast.makeText(context, requestCode+"-"+resultCode, Toast.LENGTH_SHORT).show();
        if (requestCode == rqstCode) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = null;
                if (data != null)
                    selectedImage = data.getData();
                if (selectedImage == null&&currentPhotoPath!=null) {
                    isFromCamera = true;
                    imageChoosed(Uri.fromFile(new File(currentPhotoPath)));
//                    launchCropActivity(Uri.fromFile(new File(currentPhotoPath)));
                } else if (selectedImage != null){
                    imageChoosed(selectedImage);
//                    launchCropActivity(selectedImage);
                    isFromCamera = true;
                }else {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    
    private void imageChoosed(Uri resultUri) {
        choosedImageuri = resultUri;
        Log.d("testinggg", "compressAndContinue: " + choosedImageuri.toString());
        mediaUtilsInterface.imageChoosed(resultUri);
    }
    
    public interface MediaUtilsInterface {
        void imageChoosed(Uri uri);
    }
    
    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(context, CAMERA) == PackageManager.PERMISSION_GRANTED);
    }
    
    private void requestPermission() {
        ActivityCompat.requestPermissions(context, new String[]{CAMERA}, 11);
    }
}
