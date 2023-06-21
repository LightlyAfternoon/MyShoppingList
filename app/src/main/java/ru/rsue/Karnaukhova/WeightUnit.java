package ru.rsue.Karnaukhova;

import java.util.UUID;

import ru.rsue.Karnaukhova.database.ItemCursorWrapper;

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
