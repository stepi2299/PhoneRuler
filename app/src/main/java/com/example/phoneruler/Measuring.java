package com.example.phoneruler;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
    private SensorManager sensorManager;
    private Sensor gyroscope;
    private SensorEventListener gyroscopeEvent;
    private TextView tvx, tvy;
    private Float x_measure, y_measure;

    public Measuring() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phone_dimension = new ViewModelProvider(requireActivity()).get(PhoneDimension.class);
        mWidth = phone_dimension.getWidthItem().getValue();
        mHeight = phone_dimension.getHeightItem().getValue();
        mThickness = phone_dimension.getThicknessItem().getValue();
        sensorManager = (SensorManager) this.getActivity().getSystemService(Context.SENSOR_SERVICE);
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if(gyroscope == null)
        {
            Toast.makeText(this.getContext(), "Device has no gyroscope", Toast.LENGTH_SHORT).show();
        }
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvx = binding.updatingMeasurementX;
        tvy = binding.updatingMeasurementY;
        binding.stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Measuring.this)
                        .navigate(R.id.action_MeasureFragment_to_EndMeasureFragment);
            }
        });
        gyroscopeEvent = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                Float x_deg = Measuring.radian_into_degrees(event.values[0]);
                Float y_deg = Measuring.radian_into_degrees(event.values[2]);
                x_measure = (float)((int)(x_deg*100)) / 100;
                y_measure = (float)((int)(y_deg*100)) / 100;
                x_measure = 56.0f;
                tvx.setText(String.valueOf(x_measure));
                tvy.setText(String.valueOf(y_measure));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = MeasuringBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(gyroscopeEvent, gyroscope, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(gyroscopeEvent);
    }

    public static Float radian_into_degrees(Float rad)
    {
        return (rad*180)/(float)Math.PI;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        phone_dimension.x_measure(x_measure);
        phone_dimension.y_measure(y_measure);
    }
}