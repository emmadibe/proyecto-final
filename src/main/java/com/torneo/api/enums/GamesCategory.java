package com.torneo.api.enums;

/**
 * Enum que define las diferentes categorías de juegos posibles en los torneos.
 * Cada categoría tiene un nombre y un orden que puede usarse para organizar o filtrar.
 */

public enum GamesCategory
{
    RPG("Rpg", 1),
    SHOOTER("Shooter", 2),
    SPORTS("Deportes", 3),
    SURVIVALHORROR("Survival Horrar", 4),
    BUILDER("Builder", 5);

    private String name;
    private int orden;

    private GamesCategory(String name, int orden)
    {
        this.setName(name);
        this.setOrden(orden);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }
}
