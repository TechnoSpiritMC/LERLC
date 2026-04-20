package org.leaf.api.internal.fields;

import org.leaf.api.http.dto.v2.NewApiDTO;
import org.leaf.api.http.dto.v2.Vec2d;

public class Location {
    Vec2d<Double> location;
    String postalCode;
    String streetName;
    String houseNumber;

    public Location(Vec2d<Double> location, String postalCode, String streetName, String houseNumber) {
        this.location = location;
        this.postalCode = postalCode;
        this.streetName = streetName;
        this.houseNumber = houseNumber;
    }

    public static Location from(NewApiDTO.v2LocationDTO dto) {
        return new Location(
                new Vec2d<>(
                        dto.LocationX(),
                        dto.LocationZ()
                ),
                dto.PostalCode(),
                dto.StreetName(),
                dto.BuildingNumber()
        );
    }

    public Vec2d<Double> getLocation() {
        return location;
    }
    public String getPostalCode() {
        return postalCode;
    }
    public String getStreetName() {
        return streetName;
    }
    public String getHouseNumber() {
        return houseNumber;
    }
}
