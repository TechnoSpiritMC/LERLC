package org.leaf.api.internal;

import org.leaf.api.http.dto.v1.VehicleDTO;
import org.leaf.api.http.dto.v2.NewApiDTO;

public class Vehicle {
    private String texture;
    private final String name;
    private AbstractPlayer owner;
    private String colorHex = null;
    private String colorName = null;

    public Vehicle(VehicleDTO dto) {
        this.name = dto.Name();
        this.texture = dto.Texture();
        this.owner = AbstractPlayer.from(dto.Owner());
    }

    public Vehicle(NewApiDTO.v2VehicleDTO dto) {
        this.name = dto.Name();
        this.texture = dto.Texture();
        this.owner = AbstractPlayer.from(dto.Owner());
        this.colorHex = dto.ColorHex();
        this.colorName = dto.ColorName();
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
    public AbstractPlayer getOwner() {
        return owner;
    }
}
