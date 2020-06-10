package com.example.pixag.Interfaces;

import android.os.Bundle;

public interface FragmentUploadListener {
    String UPLOAD_KEY = "upload_key";

    void onUploadPerformed(Bundle bundle);
}