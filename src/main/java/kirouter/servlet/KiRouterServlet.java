package kirouter.servlet;

import kirouter.KiRouter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class KiRouterServlet extends HttpServlet {
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";

    public KiRouterServlet() {
        configure();
    }

    public void configure() {

    }

    private Map<String, KiRouter<Route>> routers = new HashMap<String, KiRouter<Route>>();

    public void add(String method, Route route) {
        KiRouter<Route> router = routers.get(method);
        if(router == null) {
            router = new KiRouter<Route>();
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
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        KiRouter<Route> router = routers.get(req.getMethod());
        if(router != null) {
            Route matchedRouter = router.exec(req.getContextPath());
            if(matchedRouter != null) {
                matchedRouter.execute(req, resp);
                return;
            }
        }
        super.service(req, resp);
    }
}
