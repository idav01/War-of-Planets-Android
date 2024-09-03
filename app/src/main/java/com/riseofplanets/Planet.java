package com.riseofplanets;

public class Planet {
    private String name;
    private int power;
    private int influence;
    private int commerce;
    private int level; // Add this line

    public Planet(String name, int power, int influence, int commerce) {
        this.name = name;
        this.power = power;
        this.influence = influence;
        this.commerce = commerce;
        this.level = 1; // Default level is 1
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getInfluence() {
        return influence;
    }

    public void setInfluence(int influence) {
        this.influence = influence;
    }

    public int getCommerce() {
        return commerce;
    }

    public void setCommerce(int commerce) {
        this.commerce = commerce;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return name + "," + power + "," + influence + "," + commerce + "," + level;
    }

    public static Planet fromString(String planetString) {
        String[] parts = planetString.split(",");
        Planet planet = new Planet(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        planet.setLevel(Integer.parseInt(parts[4])); // Set the level
        return planet;
    }
}
