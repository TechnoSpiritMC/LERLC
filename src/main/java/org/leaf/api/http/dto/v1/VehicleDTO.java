package org.leaf.api.http.dto.v1;

/// Docs are unclear. Is owner the owner's name, the owner's id, or a formatted string?
public record VehicleDTO(String Texture,
                         String Name,
                         String Owner) {
}
