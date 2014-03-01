import kirouter.servlet.*;
import org.junit.Test;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class KiRouterFilterTest {

    @Test
    public void singleParameter() throws ServletException, IOException {
        final Map ret = new HashMap();
        Filter filter = new KiRouterFilter() {{
            get(new Route("/one-name/:name") {
                public void execute(HttpServletRequest request, HttpServletResponse response) {
                    ret.clear();
                    ret.putAll(params);
                }
            });
        }
        };
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn(KiRouterFilter.METHOD_GET);
        when(request.getServletPath()).thenReturn("/one-name/apo");
        filter.doFilter(request, null, null);
        assertEquals(new HashMap(){{ put("name","apo");}}, ret);
    }

}
