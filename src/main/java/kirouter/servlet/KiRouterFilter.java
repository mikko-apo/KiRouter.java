package kirouter.servlet;

import kirouter.StrictUrlParameterVerifier;
import kirouter.KiRouter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class KiRouterFilter implements Filter {
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";

    public static final String TEXT_HTML = "text/html";
    public static final int STATUS_OK = 200;

    private Map<String, KiRouter<Route>> routers = new HashMap<String, KiRouter<Route>>();

    public void add(String method, Route route) {
        KiRouter<Route> router = routers.get(method);
        if (router == null) {
            router = new KiRouter<Route>();
            router.setParamVerifier(new StrictUrlParameterVerifier());
            routers.put(method, router);
        }
        router.add(route);
    }

    public void get(Route route) {
        add(METHOD_GET, route);
    }

    public void post(Route route) {
        add(METHOD_POST, route);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        KiRouter<Route> router = routers.get(req.getMethod());
        if (router != null) {
            String path = req.getServletPath();
            Route matchedRouter = router.exec(path);
            if (matchedRouter != null) {
                matchedRouter.execute(req, resp);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
