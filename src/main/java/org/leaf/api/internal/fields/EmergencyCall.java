package org.leaf.api.internal.fields;

import org.leaf.api.http.dto.v2.NewApiDTO;
import org.leaf.api.http.dto.v2.Vec2d;
import org.leaf.api.internal.AbstractPlayer;

import java.time.Instant;
import java.util.List;

public class EmergencyCall {
    final Team team;
    final AbstractPlayer caller;
    final List<AbstractPlayer> responders;
    final Vec2d<Float> location;
    final Instant startedAt;
    final long callNumber;
    final String description;
    final String positionDescriptor;

    public EmergencyCall(Team team, AbstractPlayer caller, List<AbstractPlayer> responders, Vec2d<Float> location, Instant startedAt, long callNumber, String description, String positionDescriptor) {
        this.team = team;
        this.caller = caller;
        this.responders = responders;
        this.location = location;
        this.startedAt = startedAt;
        this.callNumber = callNumber;
        this.description = description;
        this.positionDescriptor = positionDescriptor;
    }

    public EmergencyCall (NewApiDTO.EmergencyCallDTO dto) {
        this.team = Team.fromString(dto.Team());
        this.caller = AbstractPlayer.from(dto.Caller());
        this.responders = dto.Players().stream().map(AbstractPlayer::from).toList();
        this.location = new Vec2d<>(dto.Position().getFirst(), dto.Position().get(1));
        this.startedAt = Instant.ofEpochSecond(dto.StartedAt());
        this.callNumber = dto.CallNumber();
        this.description = dto.Description();
        this.positionDescriptor = dto.PositionDescriptor();
    }

    /// Get the team the user called. Please note that Police and Sheriff are considered both as Police.
    public Team getTeam() {
        return team;
    }

    /// Get the player who called. Please note that only their user ID is available. To get the full player object, request a {@link org.leaf.api.internal.FullPlayer} object to the PlayerProvider.
    public AbstractPlayer getCaller() {
        return caller;
    }

    /// Get the players who responded to the call.
    public List<AbstractPlayer> getResponders() {
        return List.copyOf(responders);
    }


}
