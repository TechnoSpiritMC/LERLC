package org.leaf.api.http.dto.v1;

import java.util.List;

public record ServerStatusDTO(
        String Name,
        long OwnerId,
        List<Long> CoOwnerIds,
        int CurrentPlayers,
        int MaxPlayers,
        String JoinKey,
        String AccVerifiedReq,
        boolean TeamBalance
) {}