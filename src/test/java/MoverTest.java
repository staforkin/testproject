import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by stafo on 14.01.2017.
 */
public class MoverTest extends CamelTestSupport {

    @Override
    public void setUp() throws Exception {
        deleteDirectory("target/tests");
        super.setUp();
    }

    @Override
    public String isMockEndpoints() {
        // override this method and return the pattern for which endpoints to mock.
        return "file:target/tests/*";
    }

    @Test
    public void testMover() throws Exception{
        // notice we have automatic mocked all endpoints and the name of the endpoints is "mock:uri"
        MockEndpoint camel = getMockEndpoint("mock:file:target/tests/camel");
        camel.expectedMessageCount(1);

        MockEndpoint other = getMockEndpoint("mock:file:target/tests/output");
        other.expectedMessageCount(1);

        template.sendBodyAndHeader("file:target/tests/input", "Hello Camel", Exchange.FILE_NAME, "camel.txt");
        template.sendBodyAndHeader("file:target/tests/input", "Hello World", Exchange.FILE_NAME, "world.txt");

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("file:target/tests/input?preMove=inprogress&move=.done&moveFailed=.error")
                        .choice()
                        .when(body(String.class).contains("Camel")).to("file:target/tests/camel")
                        .otherwise().to("file:target/tests/output");
            }
        };
    }
}
