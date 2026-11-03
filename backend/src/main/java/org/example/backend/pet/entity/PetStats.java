package org.example.backend.pet.entity;


import jakarta.persistence.Embeddable;

@Embeddable
public class PetStats {

    private static final int MIN = 0;
    private static final int MAX = 100;

    private int hungerLevel;
    private int happinessLevel;
    private int energyLevel;


    public static PetStats initial() {
        PetStats s = new PetStats();
        s.hungerLevel = 50;
        s.happinessLevel = 50;
        s.energyLevel = 50;
        return s;
    }

    protected PetStats() {}

    public void addHunger(int amount)     { hungerLevel    = clamp(hungerLevel    + amount); }
    public void addHappiness(int amount)  { happinessLevel = clamp(happinessLevel + amount); }
    public void addEnergy(int amount)     { energyLevel    = clamp(energyLevel    + amount); }

    public int getHungerLevel()    { return hungerLevel; }
    public int getHappinessLevel() { return happinessLevel; }
    public int getEnergyLevel()    { return energyLevel; }

    private static int clamp(int value) {
        return Math.clamp(value, MIN, MAX);
    }

}
