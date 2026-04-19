package org.leaf.api.http.dto.v2;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.leaf.utils.OnNull;

import java.util.ArrayList;
import java.util.HashMap;
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
        List<EmergencyCallDTO> EmergencyCalls,
        List<v2VehicleDTO> Vehicles
) {

    public record EmergencyCallDTO(
            String Team,
            long Caller,
            List<String> Players,
            List<Float> Position,
            long StartedAt,
            long CallNumber,
            String Description,
            String PositionDescriptor
    ) {}

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
            @JsonDeserialize(using = EmptyArrayAsMapDeserializer.class)
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

    public static NewApiDTO from(NewApiDTO maybeNull) {
        return new NewApiDTO(
                OnNull.ensureNotNull(maybeNull.Name, "Unknown"),
                maybeNull.OwnerId,
                OnNull.ensureNotNull(maybeNull.CoOwnerIds, new ArrayList<>()),
                maybeNull.CurrentPlayers,
                maybeNull.MaxPlayers,
                OnNull.ensureNotNull(maybeNull.JoinKey, "Unknown"),
                OnNull.ensureNotNull(maybeNull.AccVerifiedReq, "Unknown"),
                maybeNull.TeamBalance,
                OnNull.ensureNotNull(maybeNull.Players, new ArrayList<>()),
                OnNull.ensureNotNull(maybeNull.Staff, new v2StaffDTO(new HashMap<>(), new HashMap<>(), new HashMap<>())),
                OnNull.ensureNotNull(maybeNull.JoinLogs, new ArrayList<>()),
                OnNull.ensureNotNull(maybeNull.Queue, new ArrayList<>()),
                OnNull.ensureNotNull(maybeNull.KillLogs, new ArrayList<>()),
                OnNull.ensureNotNull(maybeNull.CommandLogs, new ArrayList<>()),
                OnNull.ensureNotNull(maybeNull.ModCalls, new ArrayList<>()),
                OnNull.ensureNotNull(maybeNull.EmergencyCalls, new ArrayList<>()),
                OnNull.ensureNotNull(maybeNull.Vehicles, new ArrayList<>())
        );
    }
}