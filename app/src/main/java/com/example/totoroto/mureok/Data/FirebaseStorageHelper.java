package com.example.totoroto.mureok.Data;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FirebaseStorageHelper {
    public interface StorageImageResult{
        void applyImage(Uri uri);
    }
    public interface StorageProfileResult{
        void applyProfile(Uri uri);
    }

    private StorageImageResult mImageResult;
    private StorageProfileResult mProfileResult;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();

    public void setStorageImageResult(StorageImageResult imageResult){
        mImageResult = imageResult;
    }

    public void setStorageProfileResult(StorageProfileResult profileResult){
        mProfileResult = profileResult;
    }

    public void imageUpload(String filePath, String firebaseKey){
        StorageReference imageRef = storageRef.child("images/"+firebaseKey+".jpg");

        imageRef.putFile(Uri.parse(filePath))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        @SuppressWarnings("VisibleForTests")
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                });
    }

    public void profileUpload(String profilePath, String firebaseKey){
        StorageReference profileRef = storageRef.child("profiles/"+firebaseKey+".jpg");
        profileRef.putFile(Uri.parse(profilePath))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        @SuppressWarnings("VisibleForTests")
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                });
    }

    public void imageDownLoad(String firebaseKey){
        StorageReference imageRef = storageRef.child("images/"+firebaseKey+".jpg");

        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                mImageResult.applyImage(uri);
            }
        });

    }
    public void profileDownLoad(String firebaseKey){
        StorageReference profileRef = storageRef.child("profiles/"+firebaseKey+".jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                mProfileResult.applyProfile(uri);
            }
        });
    }

    public void imageDelete(String firebaseKey){
        StorageReference storageRef = storage.getReferenceFromUrl("gs://mureok-b0def.appspot.com/");
        StorageReference imgRef = storageRef.child("images/"+firebaseKey+".jpg");

        imgRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
              //  Log.d("SOLBIN", "delete image");
            }
        });
    }

    public void profileDelete(String firebaseKey){
        StorageReference storageRef = storage.getReferenceFromUrl("gs://mureok-b0def.appspot.com/");
        StorageReference profileRef = storageRef.child("profiles/"+firebaseKey+".jpg");

        profileRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
              //  Log.d("SOLBIN", "delete profile");
            }
        });
    }
}
