package com.niken.assignment.ui.user.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.niken.assignment.MainActivity;
import com.niken.assignment.R;
import com.niken.assignment.component.EditTextFormView;
import com.niken.assignment.component.UploaderView;
import com.niken.assignment.model.CallBack;
import com.niken.assignment.utils.ImageUtils;
import com.niken.assignment.viewmodel.UserViewModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateUserFragment extends Fragment {
    private static final String TAG = "CreateUserFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String imageUri = "";
    private Long createdDate = 0L;

    //viewmodel
    private UserViewModel userViewModel;

    //view
    private RadioGroup radioGroup;
    private EditTextFormView nameView;
    private EditTextFormView emailView;
    private EditTextFormView mobileNumberView;
    private EditTextFormView deviceView;
    private UploaderView uploaderView;
    private TextInputEditText calendarView;
    private TextInputLayout calendarLayout;
    private Button btnAddUser;


    public CreateUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel();
        initViews(view);

        createGenderRadioButton();
        setOnTypeListener();

        calendarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .build();
                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        calendarLayout.setError("");
                        createdDate = selection;
                        Date date = new Date(selection);

                        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
                        String dateText = df2.format(date);
                        calendarView.setText(dateText);
                    }
                });
                datePicker.show(getActivity().getSupportFragmentManager(), TAG);
            }
        });


        uploaderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAllowFlipping(false)
                        .setCropMenuCropButtonIcon(R.drawable.ic_check)
                        .start(getActivity(), CreateUserFragment.this);
            }
        });

        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAllInputValid()) {
                    saveData(view);
                }
            }
        });
    }

    private void initViewModel() {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == getActivity().RESULT_OK) {
                    Uri resultUri = result.getUri();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    bitmap = ImageUtils.scaleDownLargeBitmap(bitmap);

                    Uri uri = ImageUtils.saveBitmapAndGetUri(getActivity(), bitmap, "image_" + System.currentTimeMillis() + ".jpeg");
                    if (uri == null) {
                        Toast.makeText(getContext(), "Error saving image", Toast.LENGTH_SHORT).show();
                    } else {
                        imageUri = String.valueOf(uri);
                        uploaderView.setImage(getContext(), uri);
                    }


                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private void setOnTypeListener() {
        nameView.setTextWatcher();
        emailView.setTextWatcher();
        mobileNumberView.setTextWatcher();
        deviceView.setTextWatcher();
    }

    private void saveData(View view) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = view.findViewById(selectedId);

        userViewModel.insertUser(nameView.getValue(), emailView.getValue(), mobileNumberView.getValue(), selectedRadioButton.getText().toString(), deviceView.getValue(), imageUri, createdDate, new CallBack() {
            @Override
            public void onSuccess() {
                showAlertDialog("Success", "User has been successfully added.");
            }

            @Override
            public void onError() {
                showAlertDialog("Error", "Failed to add user.");

            }
        });
    }

    private void showAlertDialog(String title, String description) {
        AlertDialog alert = new AlertDialog.Builder(getContext()).setTitle(title).setMessage(description).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        }).show();
    }

    private boolean isAllInputValid() {
        if (nameView.getValue().isEmpty()) {
            nameView.setError("Required");
            return false;
        }
        if (emailView.getValue().isEmpty()) {
            emailView.setError("Required");
            return false;
        }

        if (!isValidToRegex(
                emailView.getValue(),
                "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,64}"
        )
        ) {
            emailView.setError("Invalid email");
            return false;
        }

        if (mobileNumberView.getValue().isEmpty()) {
            mobileNumberView.setError("Required");
            return false;
        }
        if(mobileNumberView.getValue().length() < 10) {
            mobileNumberView.setError("Mobile number must be of 10 digits");
            return false;
        }
        if (imageUri.length() == 0) {
            Toast.makeText(getContext(), "Please upload a image", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (deviceView.getValue().isEmpty()) {
            deviceView.setError("Required");
            return false;
        }
        if (calendarView.getText().length() == 0) {
            calendarLayout.setError("Required");
            return false;
        }

        return true;
    }

    private Boolean  isValidToRegex(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text.trim());
        return matcher.matches();
    }

    /**
     * init all views here
     */
    private void initViews(View view) {
        radioGroup = view.findViewById(R.id.radio_group);
        mobileNumberView = view.findViewById(R.id.mobile_view);
        nameView = view.findViewById(R.id.name_view);
        emailView = view.findViewById(R.id.email_view);
        deviceView = view.findViewById(R.id.device_view);
        btnAddUser = view.findViewById(R.id.btn_add);
        calendarView = view.findViewById(R.id.calendar);
        uploaderView = view.findViewById(R.id.upload_image_view);
        calendarLayout = view.findViewById(R.id.calendar_layout);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateUserFragment newInstance(String param1, String param2) {
        CreateUserFragment fragment = new CreateUserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void createGenderRadioButton() {
        radioGroup.removeAllViews();
        ArrayList<String> radioButtonList = new ArrayList<>();
        radioButtonList.add("Male");
        radioButtonList.add("Female");
        radioButtonList.add("Others");
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 20, 50, 0);
        // RadioButton ColorStateList
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_enabled}, //disabled
                        new int[]{android.R.attr.state_enabled} //enabled
                },
                new int[]{
                        Color.BLACK, //disabled
                        getResources().getColor(R.color.purple_500) //enabled
                }
        );
        for (String ele : radioButtonList
        ) {
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setText(ele);
            radioButton.setId(View.generateViewId());
            radioButton.setLayoutParams(params);
            radioButton.setButtonTintList(colorStateList);
            radioGroup.addView(radioButton);
        }
        radioGroup.check(radioGroup.getChildAt(0).getId());

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_user, container, false);
    }
}