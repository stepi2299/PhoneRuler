package com.example.phoneruler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PhoneDimension extends ViewModel {
    private final MutableLiveData<Float> heightItem = new MutableLiveData<Float>();
    private final MutableLiveData<Float> widthItem = new MutableLiveData<Float>();
    private final MutableLiveData<Float> thicknessItem = new MutableLiveData<Float>();
    public void heightItem(Float item) {
        heightItem.setValue(item);
    }
    public void widthItem(Float item) {
        widthItem.setValue(item);
    }
    public void thicknessItem(Float item) {
        thicknessItem.setValue(item);
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

}
