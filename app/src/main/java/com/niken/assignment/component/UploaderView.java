package com.niken.assignment.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.niken.assignment.R;

public class UploaderView  extends FrameLayout {
    public static final int INPUT_TYPE_TEXT = 0;
    public static final int INPUT_TYPE_NUMBER = 1;
    public static final int INPUT_TYPE_CALENDAR = 2;
    public static final int INPUT_TYPE_EMAIL = 3;

    private ImageView imageView;
    private ConstraintLayout uploadLayout;

    public UploaderView(Context context) {
        super(context);
        init(context, null);
    }

    public UploaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_uploader_view, this, true);
        imageView = findViewById(R.id.image_inflate);
        uploadLayout = findViewById(R.id.uploader);
    }


    public void setImage(Context context, Uri uri) {
        Glide.with(context).load(uri).into(imageView);
        imageView.setVisibility(View.VISIBLE);
        uploadLayout.setVisibility(View.GONE);
    }



}

