import kirouter.servlet.*;
import org.junit.Test;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class KiRouterFilterTest {

    @Test
    public void singleParameter() throws ServletException, IOException {
        final String stringWithAllStrictCharacters = "abcdefghijklmnopqrstuvxyzABCDEFGHIJKLMNOPQRSTUVXYZ01234567890-.";
        final List list = new ArrayList();
        Filter filter = new KiRouterFilter() {{
            get(new Route("/one-name/:name") {
                public void run() {
                    list.add(params);
                }
            });
        }
        };
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn(KiRouterFilter.METHOD_GET);
        when(request.getServletPath()).thenReturn("/one-name/" + stringWithAllStrictCharacters);
        HttpServletResponse response = mock(HttpServletResponse.class);
        filter.init(null);
        filter.doFilter(request, response, null);
        filter.destroy();
        assertEquals(Arrays.asList(new HashMap() {{
            put("name", stringWithAllStrictCharacters);
        }}), list);
    }

    @Test
    public void acceptOnlySafeUrlParameters() throws ServletException, IOException {
        final List list = new ArrayList();
        Filter filter = new KiRouterFilter() {{
            get(new Route("/one-name/:name") {
                public void run() {
                    list.add(params);
                }
            });
        }
        };
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn(KiRouterFilter.METHOD_GET);
        when(request.getServletPath()).thenReturn("/one-name/apo!!");
        HttpServletResponse response = mock(HttpServletResponse.class);
        filter.doFilter(request, response, new FilterChain() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {

            }
        });
        assertEquals(Arrays.asList() , list);
    }

}
