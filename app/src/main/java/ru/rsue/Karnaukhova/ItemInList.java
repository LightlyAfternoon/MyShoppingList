package ru.rsue.Karnaukhova;

import java.util.UUID;

public class ItemInList {
    UUID mId;
    int mCount;
    long mAddDate;
    int mIsBought;
    UUID mItemId;

    public ItemInList(UUID id) {
        mId = id;
    }

    public UUID getId() {
        return mId;
    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int count) {
        mCount = count;
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

    public UUID getItemId() {
        return mItemId;
    }

    public void setItemId(UUID item) {
        mItemId = item;
    }
}