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
                public void execute(HttpServletRequest request, HttpServletResponse response) {
                    list.add(params);
                }
            });
        }
        };
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn(KiRouterFilter.METHOD_GET);
        when(request.getServletPath()).thenReturn("/one-name/" + stringWithAllStrictCharacters);
        filter.init(null);
        filter.doFilter(request, null, null);
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
                public void execute(HttpServletRequest request, HttpServletResponse response) {
                    list.add(params);
                }
            });
        }
        };
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getMethod()).thenReturn(KiRouterFilter.METHOD_GET);
        when(request.getServletPath()).thenReturn("/one-name/apo!!");
        filter.doFilter(request, null, new FilterChain() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {

            }
        });
        assertEquals(Arrays.asList() , list);
    }

}
