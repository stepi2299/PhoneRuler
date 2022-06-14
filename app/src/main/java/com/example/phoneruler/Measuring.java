package com.example.phoneruler;

import android.content.Context;
import android.content.res.Configuration;
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

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Measuring extends Fragment {

    private MeasuringBinding binding;
    private PhoneDimension phone_dimension;
    private Float mWidth, mHeight, mThickness;
    private SensorManager sensorManager;
    private Sensor accelerometer, magnetic_field;
    private SensorEventListener accelerometerEvent, magnetic_fieldEvent;
    private TextView tvx, tvy;
    private float x_measure, y_measure, final_x_measure, final_y_measure,
            memory_orient_angle, prev_memory_orient_angle;
    private float x_angle_measure, y_angle_measure;
    private int memory_orientation, current_orient, memory_x_orientation, current_x_orientation;
    private float[] force_struct = new float[3];
    private float[] magnetic_force_struct = new float[3];
    private float[] orientation_struct = new float[3];
    private float [] rotation_matrix = new float[9];

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
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetic_field = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        memory_x_orientation = 1;
        current_x_orientation = 1;
        memory_orient_angle = Measuring.radian_into_degrees((float)y_angle_measure);
        prev_memory_orient_angle = Measuring.radian_into_degrees((float)y_angle_measure);

        if ((accelerometer == null) || (magnetic_field == null))
        {
            Toast.makeText(this.getContext(), "Device has no accelerometer", Toast.LENGTH_SHORT).show();
        }
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvx = binding.updatingMeasurementX;
        tvy = binding.updatingMeasurementY;
        final_y_measure = 0.0f;
        final_x_measure = 0.0f;
        current_orient = 0;
        memory_orientation = 0;
        binding.stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(Measuring.this)
                        .navigate(R.id.action_MeasureFragment_to_EndMeasureFragment);
            }
        });
        accelerometerEvent = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                force_struct = event.values;

                SensorManager.getRotationMatrix(rotation_matrix, null, force_struct, magnetic_force_struct);
                SensorManager.getOrientation(rotation_matrix, orientation_struct);

                x_angle_measure = (float)Math.atan2(event.values[1], event.values[2]);
                x_measure = calculateXMeasurement();
                x_measure = (float)((int)(x_measure*100)) / 100;
                tvx.setText(String.valueOf(final_x_measure + x_measure));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        magnetic_fieldEvent = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                magnetic_force_struct = event.values;

                SensorManager.getRotationMatrix(rotation_matrix, null, force_struct, magnetic_force_struct);
                SensorManager.getOrientation(rotation_matrix, orientation_struct);

                y_angle_measure = orientation_struct[1];

                calculateYMeasurement();

                y_measure = (float)((int)(y_measure*100)) / 100;

                tvy.setText(String.valueOf(final_y_measure + y_measure));

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
        sensorManager.registerListener(accelerometerEvent, accelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorManager.registerListener(magnetic_fieldEvent, magnetic_field, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(accelerometerEvent);
        sensorManager.unregisterListener(magnetic_fieldEvent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        phone_dimension.x_measure(final_x_measure + x_measure);
        phone_dimension.y_measure(final_y_measure + y_measure);
    }

    public static float radian_into_degrees(float rad)
    {
        return (rad*180)/(float)Math.PI;
    }

    public void calculateYMeasurement()
    {
        int new_orient = calculate_orientation();
        boolean add_dimension = false;
        if (new_orient != current_orient)
            add_dimension = true;
        if (new_orient == 1)
        {
            if (add_dimension){
                final_y_measure += mWidth;
            }
            y_measure = (float)Math.abs(Math.cos(y_angle_measure))*mHeight;
            current_orient = new_orient;
        }
        else {
            if (add_dimension){
                final_y_measure += mHeight;
            }
            y_measure = (float)Math.abs(Math.cos(y_angle_measure))*mWidth;
            current_orient = new_orient;
        }
    }
    public float calculateXMeasurement()
    {
        float tmp_x_measure;
        int current_orientation = calculateXorientation();
        boolean add_dimension = memory_x_orientation != current_orientation;

        if (current_orientation == 1)
        {
            if (add_dimension){
                final_x_measure += mThickness;
            }
            tmp_x_measure = (float)Math.abs(Math.cos(x_angle_measure))*mHeight;
        }
        else if (current_orientation == 0) {
            if (add_dimension){
                final_x_measure += mHeight;
            }
            tmp_x_measure = (float)Math.abs(Math.cos(x_angle_measure))*mThickness;
        }
        else
            tmp_x_measure = (float)Math.abs(Math.cos(x_angle_measure))*mThickness;
        memory_x_orientation = current_orientation;
        return tmp_x_measure;
    }

    public int calculateXorientation()
    {
        float current_x_angle = Measuring.radian_into_degrees(x_angle_measure);
        System.out.println(current_x_angle);
        if (((current_x_angle > 5)&&(current_x_angle<85))||((current_x_angle > -175)&&(current_x_angle<-95))) {
            current_x_orientation = 0;
        }
        else if (((current_x_angle > 95)&&(current_x_angle<175))||((current_x_angle > -85)&&(current_x_angle<-5))){
            current_x_orientation = 1;
        }
        return current_x_orientation;
    }

    public int calculate_orientation()
    {
        float current_orient_angle = Measuring.radian_into_degrees(y_angle_measure);
        if (((prev_memory_orient_angle<memory_orient_angle)&&(memory_orient_angle<current_orient_angle)&&(current_orient_angle<0))||
                ((prev_memory_orient_angle>memory_orient_angle)&&(memory_orient_angle>current_orient_angle)&&(current_orient_angle>0))) {
            memory_orientation = 1;
        }
        else if (((prev_memory_orient_angle<memory_orient_angle)&&(memory_orient_angle<current_orient_angle)&&(current_orient_angle>0))||
                ((prev_memory_orient_angle>memory_orient_angle)&&(memory_orient_angle>current_orient_angle)&&(current_orient_angle<0))){
            memory_orientation = 0;
        }
        prev_memory_orient_angle = memory_orient_angle;
        memory_orient_angle = current_orient_angle;
        return memory_orientation;
    }
}