package ru.rsue.Karnaukhova.entity;

import java.util.UUID;

public class ItemInList {
    UUID mId;
    float mCount;
    long mAddDate;
    float mQuantityBought;
    UUID mItemId;
    UUID mListId;
    long mBuyOnDate;
    boolean mIsPriority;
    UUID mUserId;

    public ItemInList(UUID id) {
        mId = id;
    }

    public UUID getId() {
        return mId;
    }

    public float getCount() {
        return mCount;
    }
    public void setCount(float count) {
        mCount = count;
    }

    public long getAddDate() {
        return mAddDate;
    }
    public void setAddDate(long addDate) {
        mAddDate = addDate;
    }

    public float getQuantityBought() {
        return mQuantityBought;
    }
    public void setQuantityBought(float quantityBought) {
        mQuantityBought = quantityBought;
    }

    public UUID getItemId() {
        return mItemId;
    }
    public void setItemId(UUID item) {
        mItemId = item;
    }

    public UUID getListId() {
        return mListId;
    }
    public void setListId(UUID listId) {
        mListId = listId;
    }

    public long getBuyOnDate() {
        return mBuyOnDate;
    }
    public void setBuyOnDate(long buyOnDate) {
        mBuyOnDate = buyOnDate;
    }

    public boolean getIsPriority() {
        return mIsPriority;
    }
    public void setIsPriority(boolean isPriority) {
        mIsPriority = isPriority;
    }

    public UUID getUserId() {
        return mUserId;
    }
    public void setUserId(UUID userId) {
        this.mUserId = userId;
    }
}