import kirouter.KiRouter;
import kirouter.Route;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class KiRouterTest {

    abstract class SumRoute extends Route {
        Map params;

        SumRoute(String path) {
            super(path);
        }

        @Override
        public Object process(Map params) {
            try {
                SumRoute clone = (SumRoute) this.clone();
                clone.params = params;
                return clone;
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }

        abstract public String execute(String a, String b);
    }

    @Test
    public void singleParameter() {
        KiRouter router = new KiRouter();
        router.add(new Route("/one-name/:name") {
            @Override
            public Object process(Map params) {
                return params.get("name");
            };
        });
        assertEquals("apo", router.exec("/one-name/apo"));
        assertEquals("mikko", router.exec("/one-name/mikko"));
        assertEquals(null, router.exec("/two-name/apo"));
    }

    @Test
    public void callbackParameter() {
        KiRouter<SumRoute> router = new KiRouter<SumRoute>();
        router.add(new SumRoute("/one-name/:name") {
            @Override
            public String execute(String a, String b) {
                return params.get("name") + a + b;
            }
        });
        assertEquals("apoab", router.exec("/one-name/apo").execute("a","b"));
        assertEquals("mikkoab", router.exec("/one-name/mikko").execute("a","b"));
        assertEquals(null, router.exec("/two-name/apo"));
    }
}
