package kirouter;

import java.util.Map;

public abstract class Route<V> implements Cloneable {
    private String path;

    public Route(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    abstract public V process(Map params);
}
