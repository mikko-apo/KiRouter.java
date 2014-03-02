package kirouter;

import java.util.Map;

class ResolvedRoute<V> {
    private final Map params;
    private final Route<V> route;
    private final SinatraRouteParser parser;

    public ResolvedRoute(Map params, Route route, SinatraRouteParser parser) {
        this.params = params;
        this.route = route;
        this.parser = parser;
    }

    public Map getParams() {
        return params;
    }

    public Route<V> getRoute() {
        return route;
    }

    public SinatraRouteParser getParser() {
        return parser;
    }
}
