package com.example.phoneruler;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.phoneruler.databinding.InputDataBinding;
import com.google.android.material.snackbar.Snackbar;

public class InputData extends Fragment {

    private InputDataBinding binding;
    private EditText mHeight, mWidth, mThickness;
    private PhoneDimension phone_dims;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = InputDataBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    private boolean validateFields() {
        if (mHeight.getText().length() == 0) {
            mHeight.setError("Your input cannot be empty");
            return false;
        } else if (mWidth.getText().length() == 0) {
            mWidth.setError("Your input cannot be empty");
            return false;
        } else if (mThickness.getText().length() == 0) {
            mThickness.setError("Your input cannot be empty");
            return false;
        } else {
            return true;
        }
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mHeight = binding.inputHeight;
        mWidth = binding.inputWidth;
        mThickness = binding.inputThickness;

        phone_dims = new ViewModelProvider(requireActivity()).get(PhoneDimension.class);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateFields()) {
                    phone_dims.heightItem(Float.valueOf(mHeight.getText().toString()));
                    phone_dims.widthItem(Float.valueOf(mWidth.getText().toString()));
                    phone_dims.thicknessItem(Float.valueOf(mThickness.getText().toString()));
                    NavHostFragment.findNavController(InputData.this)
                            .navigate(R.id.action_FirstFragment_to_SecondFragment);
                }
                else
                    Snackbar.make(view, "All forms must be fullfilled", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}