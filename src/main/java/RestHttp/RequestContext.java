package RestHttp;

public class RequestContext {
    private String method;
    private String path;
    private String version;
    private String host;
    private String headers;
    private String payload;



    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getVersion() {
        return version;
    }

    public String getHost() {
        return host;
    }

    public String getHeaders() {
        return headers;
    }

    public String getPayload() {
        return payload;
    }
}

