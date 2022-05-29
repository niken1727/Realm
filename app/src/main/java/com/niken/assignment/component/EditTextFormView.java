package com.niken.assignment.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.niken.assignment.R;

public class EditTextFormView extends FrameLayout {
    public static final int INPUT_TYPE_TEXT = 0;
    public static final int INPUT_TYPE_NUMBER = 1;
    public static final int INPUT_TYPE_CALENDAR = 2;
    public static final int INPUT_TYPE_EMAIL = 3;

    private TextInputLayout mTextInputLayout;
    private TextInputEditText mEditText;

    private String hint = "";
    private int inputType = INPUT_TYPE_TEXT;
    private String label = "";

    public EditTextFormView(Context context) {
        super(context);
        init(context, null);
    }

    public EditTextFormView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout_edit_text_form, this, true);
        mTextInputLayout = findViewById(R.id.text_input_layout);
        mEditText = findViewById(R.id.text_input_edittext);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EditTextFormView, 0, 0);
        try {
            hint = a.getString(R.styleable.EditTextFormView_hint);
            label = a.getString(R.styleable.EditTextFormView_label);
            inputType = a.getInt(R.styleable.EditTextFormView_inputType, INPUT_TYPE_TEXT);
        } finally {
            a.recycle();
        }


        if (!hint.isEmpty()) {
            mTextInputLayout.setHint(hint);
        }
        setInputType(inputType);
    }

    public String getValue(){
        return mEditText.getText().toString();
    }

    public void setError(String error) {
        mTextInputLayout.setError(error);
    }

    public void removeError() {
        mTextInputLayout.setError("");
    }

    public void setTextWatcher() {
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                removeError();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void setInputType(int inputType) {
        switch (inputType) {
            case INPUT_TYPE_NUMBER:
                mEditText.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                mEditText.setRawInputType(InputType.TYPE_CLASS_PHONE | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
                mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                break;
            case INPUT_TYPE_EMAIL:
                mEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;
            case INPUT_TYPE_CALENDAR:
                mEditText.setInputType(InputType.TYPE_NULL);
                mEditText.setClickable(true);
                mEditText.setFocusable(false);
                break;
            default:
                mEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
        }
        invalidate();
    }
}
