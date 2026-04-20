package org.leaf.api.internal.fields;

public enum Team {
    Civilian,
    Police,
    Sheriff,
    Fire,
    DOT;

    @Override
    public String toString() {
        return this.name();
    }

    public static Team fromString(String name) {
        return switch (name) {
            case "Police" -> Police;
            case "Fire" -> Fire;
            case "DOT" -> DOT;
            default -> null;
        };
    }
}
