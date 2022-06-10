package com.example.phoneruler;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.phoneruler.databinding.MeasuringBinding;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Measuring extends Fragment {
    private MeasuringBinding binding;
    private PhoneDimension phone_dimension;
    private Float mWidth, mHeight, mThickness;

    public Measuring() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phone_dimension = new ViewModelProvider(requireActivity()).get(PhoneDimension.class);
        mWidth = phone_dimension.getWidthItem().getValue();
        mHeight = phone_dimension.getHeightItem().getValue();
        mThickness = phone_dimension.getThicknessItem().getValue();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Measuring.this)
                        .navigate(R.id.action_MeasureFragment_to_EndMeasureFragment);
            }
        });
        TextView tvx = binding.updatingMeasurementX;
        TextView tvy = binding.updatingMeasurementY;
        tvx.setText(String.valueOf(mWidth));
        tvy.setText(String.valueOf(mHeight));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = MeasuringBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}