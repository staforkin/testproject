import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.RoutesDefinition;

import java.io.InputStream;

/**
 * Created by stafo on 13.01.2017.
 */

public class App {
    public static void main(String[] args) throws Exception {
        Logic l = new Logic();
        l.main_method();
    }
}

class Logic{
    public Logic(){}
    public void main_method() throws Exception {
        System.out.print("init CamelContext...");
        CamelContext context = new DefaultCamelContext();
        System.out.print("success\n");
        System.out.print("init config_routes.xml...");
        InputStream is = getClass().getResourceAsStream("config_routes.xml");
        System.out.print("success\n");
        System.out.print("init RoutesDefinition...");
        RoutesDefinition routes = context.loadRoutesDefinition(is);
        System.out.print("success\n");
        context.addRouteDefinitions(routes.getRoutes());
        System.out.print("start...");
        context.start();
        System.out.print("success\n");
        Thread.sleep(4000);
        System.out.print("stop...");
        context.stop();
        System.out.print("success\n");
    }
}
