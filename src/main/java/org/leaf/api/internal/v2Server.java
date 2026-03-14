package org.leaf.api.internal;

import org.leaf.api.http.dto.v1.QueuePlayerDTO;
import org.leaf.api.http.dto.v2.NewApiDTO;

import java.util.List;

public class v2Server {
    String Name;
    long OwnerId;
    List<Long> CoOwnerIds;
    int CurrentPlayers;
    int MaxPlayers;
    String JoinKey;
    String AccVerifiedReq;
    boolean TeamBalance;

    List<Player> Players;
    ServerStaff Staff;
    List<JoinLogEntry> JoinLogs;
    List<QueuePlayerDTO> Queue;
    List<KillLogEntry> KillLogs;
    List<CommandLogEntry> CommandLogs;
    List<ModCall> ModCalls;
    List<Vehicle> Vehicles;

    public v2Server(NewApiDTO dto) {
        Name = dto.Name();
        OwnerId = dto.OwnerId();
        CoOwnerIds = dto.CoOwnerIds();
        CurrentPlayers = dto.CurrentPlayers();
        MaxPlayers = dto.MaxPlayers();
        JoinKey = dto.JoinKey();
        AccVerifiedReq = dto.AccVerifiedReq();
        TeamBalance = dto.TeamBalance();

        for (var player: dto.Players()) {

        }
    }
}

