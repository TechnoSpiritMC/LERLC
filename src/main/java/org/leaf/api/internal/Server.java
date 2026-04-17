package org.leaf.api.internal;

import org.leaf.api.http.dto.v2.NewApiDTO;

import java.util.ArrayList;
import java.util.List;

public class Server {
    private volatile String serverName;
    private final long ownerId;
    private volatile List<Long> coOwnerIds             = new ArrayList<>();
    private volatile int currentPlayers;
    private volatile int maxPlayers;
    private volatile String joinKey;
    private volatile String accVerifiedReq;
    private volatile boolean teamBalance;

    private volatile List<AbstractPlayer>  players     = new ArrayList<>();
    private volatile List<AbstractPlayer>  admins      = new ArrayList<>();
    private volatile List<AbstractPlayer>  mods        = new ArrayList<>();
    private volatile List<AbstractPlayer>  helpers     = new ArrayList<>();
    private volatile List<JoinLogEntry>    joinLogs    = new ArrayList<>();
    private volatile List<Long>            queue       = new ArrayList<>();
    private volatile List<KillLogEntry>    killLogs    = new ArrayList<>();
    private volatile List<CommandLogEntry> commandLogs = new ArrayList<>();
    private volatile List<ModCall>         modCalls    = new ArrayList<>();
    private volatile List<Vehicle>         vehicles    = new ArrayList<>();

    public Server() {
        this.ownerId = -1;
    }

    public Server(NewApiDTO dto) {
        serverName = dto.Name();
        ownerId = dto.OwnerId();
        coOwnerIds = dto.CoOwnerIds();
        currentPlayers = dto.CurrentPlayers();
        maxPlayers = dto.MaxPlayers();
        joinKey = dto.JoinKey();
        accVerifiedReq = dto.AccVerifiedReq();
        teamBalance = dto.TeamBalance();

        for (var player: dto.Players()) {
            players.add(AbstractPlayer.from(player));
        }

        for (var entry : dto.Staff().Admins().entrySet()) {
            admins.add(AbstractPlayer.from(entry.getValue(), entry.getKey()));
        }
        for (var entry : dto.Staff().Mods().entrySet()) {
            mods.add(AbstractPlayer.from(entry.getValue(), entry.getKey()));
        }
        for (var entry : dto.Staff().Helpers().entrySet()) {
            helpers.add(AbstractPlayer.from(entry.getValue(), entry.getKey()));
        }
        for (var entry: dto.JoinLogs()) {
            joinLogs.add(new JoinLogEntry(entry));
        }

        queue.addAll(dto.Queue());

        for (var entry: dto.KillLogs()) {
            killLogs.add(new KillLogEntry(entry));
        }
        for (var entry: dto.CommandLogs()) {
            commandLogs.add(new CommandLogEntry(entry));
        }
        for (var entry: dto.ModCalls()) {
            modCalls.add(new ModCall(entry));
        }
        for (var entry: dto.Vehicles()) {
            vehicles.add(new Vehicle(entry));
        }

    }

    public String getServerName() {
        return serverName;
    }
    void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public List<Long> getCoOwnerIds() {
        return coOwnerIds;
    }
    void setCoOwnerIds(List<Long> coOwnerIds) {
        this.coOwnerIds = coOwnerIds;
    }
    void addCoOwnerId(long id) {
        coOwnerIds.add(id);
    }
    void removeCoOwnerId(long id) {
        coOwnerIds.remove(id);
    }

    public int getCurrentPlayers() {
        return currentPlayers;
    }
    void setCurrentPlayers(int currentPlayers) {
        this.currentPlayers = currentPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }
    void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public String getJoinKey() {
        return joinKey;
    }
    void setJoinKey(String joinKey) {
        this.joinKey = joinKey;
    }

    public String getAccVerifiedReq() {
        return accVerifiedReq;
    }
    void setAccVerifiedReq(String accVerifiedReq) {
        this.accVerifiedReq = accVerifiedReq;
    }

    public boolean hasTeamBalance() {
        return teamBalance;
    }
    void setTeamBalance(boolean teamBalance) {
        this.teamBalance = teamBalance;
    }

    public List<AbstractPlayer> getPlayers() {
        return players;
    }
    void setPlayers(List<AbstractPlayer> players) {
        this.players = players;
    }
    void _setPlayers(List<NewApiDTO.v2PlayerDTO> players) {
        this.players = players.stream().map(AbstractPlayer::from).toList();
    }

    public List<AbstractPlayer> getAdmins() {
        return admins;
    }
    void setAdmins(List<AbstractPlayer> admins) {
        this.admins = admins;
    }

    public List<AbstractPlayer> getMods() {
        return mods;
    }
    void setMods(List<AbstractPlayer> mods) {
        this.mods = mods;
    }

    public List<AbstractPlayer> getHelpers() {
        return helpers;
    }
    void setHelpers(List<AbstractPlayer> helpers) {
        this.helpers = helpers;
    }

    public List<JoinLogEntry> getJoinLogs() {
        return joinLogs;
    }
    void setJoinLogs(List<JoinLogEntry> joinLogs) {
        this.joinLogs = joinLogs;
    }

    public List<Long> getQueue() {
        return queue;
    }
    void setQueue(List<Long> queue) {
        this.queue = queue;
    }

    public List<KillLogEntry> getKillLogs() {
        return killLogs;
    }
    void setKillLogs(List<KillLogEntry> killLogs) {
        this.killLogs = killLogs;
    }

    public List<CommandLogEntry> getCommandLogs() {
        return commandLogs;
    }
    void setCommandLogs(List<CommandLogEntry> commandLogs) {
        this.commandLogs = commandLogs;
    }
}
