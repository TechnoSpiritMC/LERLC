package org.leaf.api.internal;

import org.leaf.api.http.dto.v1.ServerStatusDTO;

import java.util.List;

public class ServerStatus {
    private String name;
    private long ownerID;
    private List<Long> coOwnerIds;
    private int currentPlayers;
    private int maxPlayers;
    private String joinKey;
    private String accVerifiedReq;
    private boolean teamBalance;

    public ServerStatus(ServerStatusDTO dto) {
        name = dto.Name();
        ownerID = dto.OwnerId();
        coOwnerIds = dto.CoOwnerIds();
        currentPlayers = dto.CurrentPlayers();
        maxPlayers = dto.MaxPlayers();
        joinKey = dto.JoinKey();
        accVerifiedReq = dto.AccVerifiedReq();
        teamBalance = dto.TeamBalance();
    }

    /// Get the private server name.
    public String getName() {
        return name;
    }
    /// Get the owner's id.
    public long getOwnerID() {
        return ownerID;
    }
    /// Get the co-owners' ids.
    public List<Long> getCoOwnerIds() {
        return coOwnerIds;
    }
    /// Get the current player count.
    public int getCurrentPlayers() {
        return currentPlayers;
    }
    /// Get the maximum player count.
    public int getMaxPlayers() {
        return maxPlayers;
    }
    /// Get the join key.
    public String getJoinKey() {
        return joinKey;
    }
    /// Get the account verification requirement.
    public String getAccVerifiedReq() {
        return accVerifiedReq;
    }
    /// Get whether team balance is enabled.
    public boolean isTeamBalance() {
        return teamBalance;
    }


    void setCurrentPlayers(int currentPlayers) {
        this.currentPlayers = currentPlayers;
    }
    void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }
    void setJoinKey(String joinKey) {
        this.joinKey = joinKey;
    }
    void setAccVerifiedReq(String accVerifiedReq) {
        this.accVerifiedReq = accVerifiedReq;
    }
    void setTeamBalance(boolean teamBalance) {
        this.teamBalance = teamBalance;
    }
    void setName(String name) {
        this.name = name;
    }
    void setOwnerID(long ownerID) {
        this.ownerID = ownerID;
    }
    void setCoOwnerIds(List<Long> coOwnerIds) {
        this.coOwnerIds = coOwnerIds;
    }
}
