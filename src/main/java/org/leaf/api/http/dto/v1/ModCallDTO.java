package org.leaf.api.http.dto.v1;

public record ModCallDTO(String Caller,
                         String Moderator,
                         long Timestamp) {
}
