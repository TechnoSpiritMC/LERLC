package org.leaf.api.internal;

import org.leaf.api.http.dto.v1.VehicleDTO;
import org.leaf.roblox.RobloxPlayer;

public class Vehicle {
    private String texture;
    private final String name;
    private RobloxPlayer owner;

    public Vehicle(VehicleDTO dto) {
        this.name = dto.Name();
        this.texture = dto.Texture();
        this.owner = RobloxPlayer.parse(dto.Owner());
    }

    /// Get the name of the vehicle.
    public String getName() {
        return name;
    }
    /// Get the texture of the vehicle.
    public String getTexture() {
        return texture;
    }
    /// Get the owner of the vehicle.
    public RobloxPlayer getOwner() {
        return owner;
    }
}
