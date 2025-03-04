package ru.rsue.Karnaukhova.dto;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ItemDTO {
    UUID uuid;
    String name;
    UUID weightUnitId;
    double priceForOne;
    String color;
    UUID userId;

    public ItemDTO() {}

    public ItemDTO(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public UUID getWeightUnit() {
        return weightUnitId;
    }
    public void setWeightUnit(UUID weightUnit) {
        weightUnitId = weightUnit;
    }

    public double getPriceForOne() {
        return priceForOne;
    }
    public void setPriceForOne(double priceForOne) {
        this.priceForOne = priceForOne;
    }

    public String getColor() { return color; }
    public void setColor(String color) {
        this.color = color;
    }

    public UUID getUserId() {
        return userId;
    }
    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @NotNull
    @Override
    public String toString() {
        return name;
    }
}