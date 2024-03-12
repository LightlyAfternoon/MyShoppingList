package ru.rsue.Karnaukhova;

import java.util.UUID;

public class ItemInList {
    UUID mId;
    int mCount;
    long mAddDate;
    int mQuantityBought;
    UUID mItemId;
    UUID mListId;
    long mBuyOnDate;
    int mIsPriority;

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

    public int getQuantityBought() {
        return mQuantityBought;
    }

    public void setQuantityBought(int quantityBought) {
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
        this.mListId = listId;
    }

    public long getBuyOnDate() {
        return mBuyOnDate;
    }
    public void setBuyOnDate(long buyOnDate) {
        this.mBuyOnDate = buyOnDate;
    }

    public int getIsPriority() {
        return mIsPriority;
    }
    public void setIsPriority(int isPriority) {
        this.mIsPriority = isPriority;
    }
}