package ru.rsue.Karnaukhova.dto;

import java.util.UUID;

public class ItemInListDTO {
    UUID uuid;
    float count;
    long addDate;
    float quantityBought;
    UUID itemId;
    UUID listId;
    long buyOnDate;
    boolean isPriority;
    UUID userId;

    public ItemInListDTO() {}

    public ItemInListDTO(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public float getCount() {
        return count;
    }
    public void setCount(float count) {
        this.count = count;
    }

    public long getAddDate() {
        return addDate;
    }
    public void setAddDate(long addDate) {
        this.addDate = addDate;
    }

    public float getQuantityBought() {
        return quantityBought;
    }
    public void setQuantityBought(float quantityBought) {
        this.quantityBought = quantityBought;
    }

    public UUID getItemId() {
        return itemId;
    }
    public void setItemId(UUID item) {
        itemId = item;
    }

    public UUID getListId() {
        return listId;
    }
    public void setListId(UUID listId) {
        this.listId = listId;
    }

    public long getBuyOnDate() {
        return buyOnDate;
    }
    public void setBuyOnDate(long buyOnDate) {
        this.buyOnDate = buyOnDate;
    }

    public boolean getIsPriority() {
        return isPriority;
    }
    public void setIsPriority(boolean isPriority) {
        this.isPriority = isPriority;
    }

    public UUID getUserId() {
        return userId;
    }
    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}