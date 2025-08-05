package com.torneo.api.enums;

/**
 * Enum que indica el estado de un torneo.
 * Se utiliza para saber si un torneo está activo, finalizado o próximo a comenzar.
 */

public enum GamesState
{
    ACTIVE("Active", 1),
    FINISHED("Finished", 2),
    INSCRIPTION("Inscription", 3),
    NEXT("Next", 3);

    private String name;
    private int orden;

    private GamesState(String name, int orden)
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
