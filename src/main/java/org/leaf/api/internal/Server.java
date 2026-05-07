package org.leaf.api.internal;

import org.leaf.api.command.Command;
import org.leaf.api.http.dto.v2.NewApiDTO;
import org.leaf.api.internal.command.CommandName;
import org.leaf.api.internal.fields.*;

import java.util.ArrayList;
import java.util.List;

public class Server {
    private volatile String serverName;
    private final long ownerId;
    private volatile List<Long> coOwnerIds                = new ArrayList<>();
    private volatile int currentPlayers;
    private volatile int maxPlayers;
    private volatile String joinKey;
    private volatile String accVerifiedReq;
    private volatile boolean teamBalance;

    private volatile List<AbstractPlayer>  players        = new ArrayList<>();
    private volatile List<AbstractPlayer>  admins         = new ArrayList<>();
    private volatile List<AbstractPlayer>  mods           = new ArrayList<>();
    private volatile List<AbstractPlayer>  helpers        = new ArrayList<>();
    private volatile List<JoinLogEntry>    joinLogs       = new ArrayList<>();
    private volatile List<Long>            queue          = new ArrayList<>();
    private volatile List<KillLogEntry>    killLogs       = new ArrayList<>();
    private volatile List<CommandLogEntry> commandLogs    = new ArrayList<>();
    private volatile List<ModCall>         modCalls       = new ArrayList<>();
    private volatile List<EmergencyCall>   emergencyCalls = new ArrayList<>();
    private volatile List<Vehicle>         vehicles       = new ArrayList<>();

    public Server() {
        this.ownerId = -1;
    }

    public Server(NewApiDTO _dto) {

        NewApiDTO dto = NewApiDTO.from(_dto);

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
        for (var entry: dto.EmergencyCalls()) {
            emergencyCalls.add(new EmergencyCall(entry));
        }
        for (var entry: dto.Vehicles()) {
            vehicles.add(new Vehicle(entry));
        }

    }

    /// Get the fetched private server name.
    public String getServerName() {
        return serverName;
    }
    void setServerName(String serverName) {
        this.serverName = serverName;
    }

    /// Get the owner ID. Please note that this is unique and constant per server, but multiple servers can be owned by the same user.
    public long getOwnerId() {
        return ownerId;
    }

    /// Get a list containing the IDs of all server co owners.
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

    /// Get the number of current online players.
    public int getCurrentPlayers() {
        return currentPlayers;
    }
    void setCurrentPlayers(int currentPlayers) {
        this.currentPlayers = currentPlayers;
    }

    /// Get the maximum player number allowed on this server
    public int getMaxPlayers() {
        return maxPlayers;
    }
    void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    /// Get the server join key. Is can be used to join the server directly from the server list in ERLC.
    public String getJoinKey() {
        return joinKey;
    }
    void setJoinKey(String joinKey) {
        this.joinKey = joinKey;
    }

    /// Returns the account verification method required by the server. (Required ERLC XP, Playtime, both, etc.).
    public String getAccVerifiedReq() {
        return accVerifiedReq;
    }
    void setAccVerifiedReq(String accVerifiedReq) {
        this.accVerifiedReq = accVerifiedReq;
    }

    /// Returns true if the server has auto team balance features enabled.
    public boolean hasTeamBalance() {
        return teamBalance;
    }
    void setTeamBalance(boolean teamBalance) {
        this.teamBalance = teamBalance;
    }

    /// Returns the list of abstract players currently online. Plese note that these are {@link AbstractPlayer} instances, which mean they do not hold real data. To get true player data, call {@link PlayerProvider#get(AbstractPlayer)}.
    public List<AbstractPlayer> getPlayers() {
        return players;
    }
    void setPlayers(List<AbstractPlayer> players) {
        this.players = players;
    }
    void _setPlayers(List<NewApiDTO.v2PlayerDTO> players) {
        this.players = players.stream().map(AbstractPlayer::from).toList();
    }

    /// Returns the list of abstract staff members currently online.
    /// Plese note that these are {@link AbstractPlayer} instances, which mean they do not hold real data.
    /// To get true player data, call {@link PlayerProvider#get(AbstractPlayer)}.
    public List<AbstractPlayer> getAdmins() {
        return admins;
    }
    void setAdmins(List<AbstractPlayer> admins) {
        this.admins = admins;
    }

    /// Returns the list of abstract staff members currently online.
    /// Plese note that these are {@link AbstractPlayer} instances, which mean they do not hold real data.
    /// To get true player data, call {@link PlayerProvider#get(AbstractPlayer)}.
    public List<AbstractPlayer> getMods() {
        return mods;
    }
    void setMods(List<AbstractPlayer> mods) {
        this.mods = mods;
    }

    /// Returns the list of abstract staff members currently online.
    /// Plese note that these are {@link AbstractPlayer} instances, which mean they do not hold real data.
    /// To get true player data, call {@link PlayerProvider#get(AbstractPlayer)}.
    public List<AbstractPlayer> getHelpers() {
        return helpers;
    }
    void setHelpers(List<AbstractPlayer> helpers) {
        this.helpers = helpers;
    }

    /// Returns a list of {@link JoinLogEntry} instances, which contain information about all players that have joined the server.
    public List<JoinLogEntry> getJoinLogs() {
        return joinLogs;
    }
    void setJoinLogs(List<JoinLogEntry> joinLogs) {
        this.joinLogs = joinLogs;
    }

    /// Returns the list of player IDs who are currently waiting in your server queue.
    public List<Long> getQueue() {
        return queue;
    }
    void setQueue(List<Long> queue) {
        this.queue = queue;
    }

    /// Returns a list of {@link KillLogEntry} instances, which contain information about all kills that have occurred on the server.
    public List<KillLogEntry> getKillLogs() {
        return killLogs;
    }
    void setKillLogs(List<KillLogEntry> killLogs) {
        this.killLogs = killLogs;
    }

    /// Returns a list of {@link CommandLogEntry} instances, which contain information about all commands that have been executed on the server.
    /// More info about commands at {@link CommandLogEntry} and {@link Command}.
    ///
    /// <i>Documentation about {@link Command} contains information and links to {@link CommandName} enum fields, which contain detailed information about every supported command.</i>
    public List<CommandLogEntry> getCommandLogs() {
        return commandLogs;
    }
    void setCommandLogs(List<CommandLogEntry> commandLogs) {
        this.commandLogs = commandLogs;
    }
}
