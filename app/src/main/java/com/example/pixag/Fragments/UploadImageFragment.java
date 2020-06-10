package com.example.pixag.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.pixag.DataModels.MyLocation;
import com.example.pixag.Interfaces.FragmentUploadListener;
import com.example.pixag.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

public class UploadImageFragment extends Fragment {
    private static final int PICK_IMAGE_GALLERY = 1;
    private static final int PICK_IMAGE_CAMERA = 0;
    private Uri imageUri;
    private ImageView pictureFromCamera;
    private Button BTN_upload;
    private Button BTN_choose_file;
    private Bitmap bitmap;
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private MyLocation location;
    private FragmentUploadListener uploadListener;
    private Bundle myBundle;


    public UploadImageFragment() {
        // Required empty public constructor
    }

    public UploadImageFragment(MyLocation location, FragmentUploadListener fragmentUploadListener) {
        this.location = location;
        this.uploadListener = fragmentUploadListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_image, container, false);
        pictureFromCamera = view.findViewById(R.id.imageView_picture);
        BTN_upload = view.findViewById(R.id.BTN_upload);
        BTN_choose_file = view.findViewById(R.id.BTN_choose_file);

        myBundle = new Bundle();

        BTN_choose_file.setOnClickListener(v -> selectImage(getActivity()));
        BTN_upload.setOnClickListener(v -> justUpload());

        return view;


    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            switch (requestCode) {
                case PICK_IMAGE_GALLERY:
                    imageUri = data.getData();
                    Glide.with(this).load(imageUri).into(pictureFromCamera);

                    Glide.with(this)
                            .asBitmap()
                            .load(imageUri)
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    if (imageUri != null) {
                                        bitmap = resource;
                                        uploadAndSendPhoto();
                                    }
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                }
                            });
                    break;
                case PICK_IMAGE_CAMERA:
                    bitmap = (Bitmap) data.getExtras().get("data");
                    pictureFromCamera.setImageBitmap(bitmap);
                    uploadAndSendPhoto();
                    break;
            }
        }
    }

    private void uploadAndSendPhoto() {
        if (bitmap != null && uploadListener != null) {
            myBundle.putParcelable(FragmentUploadListener.UPLOAD_KEY,
                    bitmap);
            uploadListener.onUploadPerformed(myBundle);
        }
    }

    public void justUpload() {
        Bitmap bitmap = Objects.requireNonNull(myBundle).
                getParcelable(FragmentUploadListener.UPLOAD_KEY);
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteArrayData = baos.toByteArray();
            UUID specialID = UUID.randomUUID();


            UploadTask uploadTask = firebaseStorage.getReference()
                    .child("photo")
                    .child(specialID
                            .toString()).
                            putBytes(byteArrayData);


            uploadTask.addOnFailureListener(exception -> {
                Toast.makeText(getContext(), "Upload Failed", Toast.LENGTH_LONG).show();
            }).addOnSuccessListener(taskSnapshot -> {
                Toast.makeText(getContext(), "Uploaded Successfully", Toast.LENGTH_LONG).show();
                // Get reference to the file
                StorageReference imageRef = firebaseStorage.getReference().child("photo").child(specialID.toString());

                // Create file metadata including the content type
                StorageMetadata metadata = new StorageMetadata.Builder()
                        .setCustomMetadata("Location", location.toString())
                        .build();

                // Update metadata properties
                imageRef.updateMetadata(metadata)
                        .addOnSuccessListener(storageMetadata -> {
                            // Updated metadata is in storageMetadata
                            Toast.makeText(getContext(), "meta good", Toast.LENGTH_LONG).show();
                        })
                        .addOnFailureListener(exception -> {
                            Toast.makeText(getContext(), "meta bad", Toast.LENGTH_LONG).show();
                        });
            });
        }
    }


    private void selectImage(Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo")) {
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), PICK_IMAGE_CAMERA);
            } else if (options[item].equals("Choose from Gallery")) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_IMAGE_GALLERY);
            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }


}