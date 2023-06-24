package ru.rsue.Karnaukhova;

import java.util.UUID;

public class Item {
    UUID mId;
    String mName;
    int mCount;
    UUID mWeightUnitId;
    double mPriceForOne;
    long mAddDate;
    int mIsBought;

    public Item(UUID id) {
        mId = id;
    }

    public UUID getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int count) {
        mCount = count;
    }

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

    public long getAddDate() {
        return mAddDate;
    }

    public void setAddDate(long addDate) {
        mAddDate = addDate;
    }

    public int isBought() {
        return mIsBought;
    }

    public void setBought(int bought) {
        mIsBought = bought;
    }
}