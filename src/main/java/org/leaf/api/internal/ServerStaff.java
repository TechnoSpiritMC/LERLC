package org.leaf.api.internal;

import org.leaf.api.http.dto.v1.ServerStaffDTO;
import org.leaf.roblox.RobloxPlayer;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class ServerStaff {
    List<RobloxPlayer> coOwners;
    List<RobloxPlayer> admins = new ArrayList<>();
    List<RobloxPlayer> mods = new ArrayList<>();

    @Deprecated
    public ServerStaff(ServerStaffDTO dto) {
        coOwners = dto.CoOwners().stream().map(RobloxPlayer::new).toList();

        for (var entry: dto.Admins().entrySet()) {
            admins.add(new RobloxPlayer(entry.getValue(), entry.getKey()));
        }
        for (var entry: dto.Mods().entrySet()) {
            mods.add(new RobloxPlayer(entry.getValue(), entry.getKey()));
        }
    }

    /// Get the list of server co-owners.
    @Deprecated
    public List<RobloxPlayer> getCoOwners() {
        return coOwners;
    }

    /// Get the list of server admins.
    @Deprecated
    public List<RobloxPlayer> getAdmins() {
        return admins;
    }

    /// Get the list of server mods.
    @Deprecated
    public List<RobloxPlayer> getMods() {
        return mods;
    }
}
