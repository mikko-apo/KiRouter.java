package kirouter;

import java.util.*;

public class KiRouter<V> {
    private List<StoredRoute> routes = new ArrayList<StoredRoute>();
    private long renderCount = 0;
    private ParamVerifier paramVerifier = null;

    public void add(Route<V> route) {
        routes.add(new StoredRoute(route, new SinatraRouteParser(route.getPath())));
    }

    public V exec(String path) {
        ResolvedRoute<V> route = find(path);
        if (route != null) {
            renderCount++;
            try {
                return route.getRoute().process(route.getParams());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public ResolvedRoute<V> find(String path) {
        for (StoredRoute candidate : routes) {
            Map params = candidate.parser.parse(path, paramVerifier);
            if (params != null) {
                return new ResolvedRoute<V>(params, candidate.route, candidate.parser);
            }
        }
        return null;
    }

    class StoredRoute {
        Route<V> route;
        SinatraRouteParser parser;

        StoredRoute(Route<V> route, SinatraRouteParser parser) {
            this.route = route;
            this.parser = parser;
        }
    }

    public void setParamVerifier(ParamVerifier paramVerifier) {
        this.paramVerifier = paramVerifier;
    }
}

