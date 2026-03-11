package org.leaf.api.http.dto.v1;

import java.util.List;
import java.util.Map;

// Todo: Remove this as soon as ERLC Stops supporting it!
/**
 * <b>This DTO adds support for the deprecated {@code v1/server/staff} endpoint. This may be unstable and / or lead to errors!</b>
 */
@Deprecated
public record ServerStaffDTO(
        List<Long> CoOwners,
        Map<Long, String> Admins,
        Map<Long, String> Mods
) {}