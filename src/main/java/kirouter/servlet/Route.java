package kirouter.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

abstract public class Route extends kirouter.Route {
    protected Map<String, String> params;
    protected HttpServletRequest request;
    protected HttpServletResponse response;

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

    public void run(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        run();
    }

    public abstract void run();

    public void puts(String s) {
        writer().write(s);
    }

    public void status(int code) {
        response.setStatus(code);
    }

    public void contentType(String s) {
        response.setContentType(s);
    }

    public PrintWriter writer() {
        try {
            return response.getWriter();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
