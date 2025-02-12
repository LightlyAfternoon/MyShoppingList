package ru.rsue.Karnaukhova.entity;

import java.util.UUID;

public class ItemList {
    UUID mId;
    String mListName;
    UUID mOwnerUserId;

    public ItemList(UUID id) {
        mId = id;
    }

    public UUID getId() {
        return mId;
    }

    public String getListName() {
        return mListName;
    }

    public void setListName(String listName) {
        this.mListName = listName;
    }

    public UUID getOwnerUserId() {
        return mOwnerUserId;
    }

    public void setOwnerUserId(UUID ownerUserId) {
        this.mOwnerUserId = ownerUserId;
    }

    @Override
    public String toString() {
        return mListName;
    }
}