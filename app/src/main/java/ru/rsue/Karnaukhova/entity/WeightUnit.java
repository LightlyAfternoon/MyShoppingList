package ru.rsue.Karnaukhova.entity;

import java.util.UUID;

public class WeightUnit {
    private UUID mId;
    private String mName;

    public WeightUnit(UUID id) {
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
}