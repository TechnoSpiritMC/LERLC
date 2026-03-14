package org.leaf.api.internal;

public enum ConnectionMethod {
    POST,
    GET;

    @Override
    public String toString(ConnectionMethod this) {
        return this.name();
    }
}
