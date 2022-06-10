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

import com.example.phoneruler.databinding.EndMeasuringBinding;
import com.example.phoneruler.databinding.MeasuringBinding;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class EndMeasuring extends Fragment {

    private EndMeasuringBinding binding;
    private TextView tvx, tvy;
    private PhoneDimension phone_dimension;
    private Float x_measure, y_measure;

    public EndMeasuring() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phone_dimension = new ViewModelProvider(requireActivity()).get(PhoneDimension.class);
        x_measure = phone_dimension.getX_measure().getValue();
        y_measure = phone_dimension.getY_measure().getValue();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvx = binding.updatingMeasurementX;
        tvy = binding.updatingMeasurementY;
        tvx.setText(String.valueOf(x_measure));
        tvy.setText(String.valueOf(y_measure));

        binding.comeBackAfterMeasurement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(EndMeasuring.this)
                        .navigate(R.id.correct_input_dimension);
            }
        });

        binding.repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(EndMeasuring.this)
                        .navigate(R.id.repeat_measurement);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = EndMeasuringBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}