package ru.rsue.Karnaukhova;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Item {
    private UUID mId;
    private String mName;
    private int mCount;
    private String mWeightUnit;
    private double mPriceForOne;
    private long mAddDate;

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

    public String getWeightUnit() {
        return mWeightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        mWeightUnit = weightUnit;
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
}
