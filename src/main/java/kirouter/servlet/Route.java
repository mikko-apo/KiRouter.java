package kirouter.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

abstract public class Route extends kirouter.Route {
    protected Map params;

    public Route(String path) {
        super(path);
    }

    @Override
    public Object process(Map params) {
        try {
            // User specified Route has defined execute method that needs params:
            // * param needs to be set and we need to support parallel operation -> clone
            Route clone = (Route) this.clone();
            clone.params = params;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    abstract public void execute(HttpServletRequest request, HttpServletResponse response);
}
