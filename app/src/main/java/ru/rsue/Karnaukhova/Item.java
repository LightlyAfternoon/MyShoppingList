package ru.rsue.Karnaukhova;

import java.util.UUID;

public class Item {
    UUID mId;
    String mName;
    UUID mWeightUnitId;
    double mPriceForOne;

    public Item(UUID id) {
        mId = id;
    }

    public UUID getId() {
        return mId;
    }

    public String getName() { return mName; }

    public void setName(String name) { mName = name; }

    public UUID getWeightUnit() {
        return mWeightUnitId;
    }

    public void setWeightUnit(UUID weightUnit) {
        mWeightUnitId = weightUnit;
    }

    public double getPriceForOne() {
        return mPriceForOne;
    }

    public void setPriceForOne(double priceForOne) {
        mPriceForOne = priceForOne;
    }
}