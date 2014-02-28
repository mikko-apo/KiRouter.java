import kirouter.servlet.*;
import org.junit.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class KiRouterServletTest {

    @Test
    public void singleParameter() throws ServletException, IOException {
        final Map ret = new HashMap();
        HttpServlet servlet = new KiRouterServlet() {
            @Override
            public void configure() {
                get(new Route("/one-name/:name") {
                    @Override
                    public void execute(HttpServletRequest request, HttpServletResponse response) {
                        ret.clear();
                        ret.putAll(params);
                    }
                });
            }
        };
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn(KiRouterServlet.METHOD_GET);
        when(request.getContextPath()).thenReturn("/one-name/apo");
        servlet.service(request,null);
        assertEquals(new HashMap(){{ put("name","apo");}}, ret);
    }

}
