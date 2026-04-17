package org.leaf.api.internal;

import org.leaf.api.http.dto.v1.ServerStaffDTO;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class ServerStaff {
    List<AbstractPlayer> coOwners;
    List<AbstractPlayer> admins = new ArrayList<>();
    List<AbstractPlayer> mods = new ArrayList<>();

    @Deprecated
    public ServerStaff(ServerStaffDTO dto) {
        coOwners = dto.CoOwners().stream().map(AbstractPlayer::from).toList();

        for (var entry: dto.Admins().entrySet()) {
            admins.add(new AbstractPlayer(entry.getValue(), entry.getKey()));
        }
        for (var entry: dto.Mods().entrySet()) {
            mods.add(new AbstractPlayer(entry.getValue(), entry.getKey()));
        }
    }

    /// Get the list of server co-owners.
    @Deprecated
    public List<AbstractPlayer> getCoOwners() {
        return coOwners;
    }

    /// Get the list of server admins.
    @Deprecated
    public List<AbstractPlayer> getAdmins() {
        return admins;
    }

    /// Get the list of server mods.
    @Deprecated
    public List<AbstractPlayer> getMods() {
        return mods;
    }
}
