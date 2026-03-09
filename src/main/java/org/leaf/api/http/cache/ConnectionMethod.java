package org.leaf.api.http.cache;

public enum ConnectionMethod {
    POST,
    GET;

    @Override
    public String toString(ConnectionMethod this) {
        return this.name();
    }
}
