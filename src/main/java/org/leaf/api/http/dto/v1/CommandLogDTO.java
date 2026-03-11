package org.leaf.api.http.dto.v1;

public record CommandLogDTO(String Player,
                            long Timestamp,
                            String Command) {}
