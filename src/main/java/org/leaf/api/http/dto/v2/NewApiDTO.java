package org.leaf.api.http.dto.v2;

import java.util.List;
import java.util.Map;

public record NewApiDTO(
        String Name,
        long OwnerId,
        List<Long> CoOwnerIds,
        int CurrentPlayers,
        int MaxPlayers,
        String JoinKey,
        String AccVerifiedReq,
        boolean TeamBalance,
        List<v2PlayerDTO> Players,
        v2StaffDTO Staff,
        List<v2JoinLogDTO> JoinLogs,
        List<Long> Queue,
        List<v2KillLogDTO> KillLogs,
        List<v2CommandLogDTO> CommandLogs,
        List<v2ModCallDTO> ModCalls,
        List<v2VehicleDTO> Vehicles
) {

    public record v2PlayerDTO(
            String Team,
            String Player,
            String Callsign,
            v2LocationDTO Location,
            String Permission,
            int WantedStars
    ) {}

    public record v2LocationDTO(
            double LocationX,
            double LocationZ,
            String PostalCode,
            String StreetName,
            String BuildingNumber
    ) {}

    public record v2StaffDTO(
            Map<Long, String> Admins,
            Map<Long, String> Mods,
            Map<Long, String> Helpers
    ) {}

    public record v2JoinLogDTO(
            boolean Join,
            long Timestamp,
            String Player
    ) {}

    public record v2KillLogDTO(
            String Killed,
            long Timestamp,
            String Killer
    ) {}

    public record v2CommandLogDTO(
            String Player,
            long Timestamp,
            String Command
    ) {}

    public record v2ModCallDTO(
            String Caller,
            String Moderator,
            long Timestamp
    ) {}

    public record v2VehicleDTO(
            String Name,
            String Owner,
            String Texture,
            String ColorHex,
            String ColorName
    ) {}
}