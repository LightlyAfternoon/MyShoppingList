package ru.rsue.Karnaukhova.dto;

import java.util.UUID;

public class WeightUnitDTO {
    private UUID uuid;
    private String mName;

    public WeightUnitDTO() {}

    public WeightUnitDTO(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}