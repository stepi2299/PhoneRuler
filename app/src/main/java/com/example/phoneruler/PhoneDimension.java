package com.example.phoneruler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PhoneDimension extends ViewModel {
    private final MutableLiveData<Float> heightItem = new MutableLiveData<Float>();
    private final MutableLiveData<Float> widthItem = new MutableLiveData<Float>();
    private final MutableLiveData<Float> thicknessItem = new MutableLiveData<Float>();
    private final MutableLiveData<Float> x_measure = new MutableLiveData<Float>();
    private final MutableLiveData<Float> y_measure = new MutableLiveData<Float>();
    public void heightItem(Float item) {
        heightItem.setValue(item);
    }
    public void widthItem(Float item) {
        widthItem.setValue(item);
    }
    public void thicknessItem(Float item) {
        thicknessItem.setValue(item);
    }
    public void x_measure(Float item) {
        x_measure.setValue(item);
    }
    public void y_measure(Float item) {
        y_measure.setValue(item);
    }
    public LiveData<Float> getHeightItem() {
        return heightItem;
    }
    public LiveData<Float> getWidthItem() {
        return widthItem;
    }
    public LiveData<Float> getThicknessItem() {
        return thicknessItem;
    }
    public LiveData<Float> getX_measure() {
        return x_measure;
    }
    public LiveData<Float> getY_measure() {
        return y_measure;
    }

}
