import RestHttp.RequestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ContextTest {

    RequestContext requestContext;

    @BeforeEach
    public void setup(){
        String contextTest = "GET /messages HTTP/1.1\r\n" +
                "Host: localhost:8080\r\n" +
                "User-Agent: insomnia/2020.4.2\r\n" +
                "Accept: */*\r\n" +
                "Content-Length: 0\r\n";

        requestContext = new RequestContext(contextTest);

    }
    @Test
    public void testGetMethod_shouldBeGET(){
        assertTrue(requestContext.getMethod().equals("GET"), "The Method should be GET");
    }
    @Test
    public void testGetPath_shouldBeMessage(){
        assertTrue(requestContext.getPath().equals("/messages"), "The Path should be /messages");
    }
    @Test
    public void testGetVersion_shouldBeHttp(){
        assertTrue(requestContext.getVersion().equals("HTTP/1.1"), "The Version should be HTTP/1.1");
    }
    @Test
    public void testGetHost_shouldBeLocalhost(){
        assertTrue(requestContext.getHost().equals("localhost:8080"), "The Host should be localhost:8080");
    }
    @Test
    public void testGetContentlength_shouldBeZero(){
        assertTrue(requestContext.getContentLength().equals("0"), "The Contentlength should be 0");
    }

}
