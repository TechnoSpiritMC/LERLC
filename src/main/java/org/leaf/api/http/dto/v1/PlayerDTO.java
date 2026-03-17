package org.leaf.api.http.dto.v1;

public record PlayerDTO(String Player,
                        String Permission,
                        String Callsign,
                        String Team) {
    @Override
    public boolean equals(Object o) {
        if (o instanceof PlayerDTO other) {
            return other.Player.equals(Player);
        } else {
            return false;
        }
    }
}
