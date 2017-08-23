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
    private PassImageResult mPassImage;
    private PassProfileResult mPassProfile;

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();

    public interface PassImageResult{
        void pass(Uri uri);
    }

    public interface PassProfileResult{
        void pass(Uri uri);
    }

    public void setPassImageResult(PassImageResult imageResult){
        mPassImage = imageResult;
    }

    public void setPassProfileResult(PassProfileResult profileResult){
        mPassProfile = profileResult;
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
                        mPassImage.pass(downloadUrl); ///
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
                        mPassProfile.pass(downloadUrl);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
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
