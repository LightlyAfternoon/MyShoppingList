package ru.rsue.Karnaukhova.dto;

import java.util.UUID;

public class ItemListDTO {
    UUID uuid;
    String listName;
    UUID ownerUserId;

    public ItemListDTO(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public UUID getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(UUID ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    @Override
    public String toString() {
        return listName;
    }
}