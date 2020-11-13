package RestHttp;

import java.util.ArrayList;
import java.util.List;

public class RequestContext {
    private String method;
    private String path;
    private String version;
    private String host;
    private List<String> headers = new ArrayList<>();
    private String contentLength;
    //private String payload;

    public RequestContext(String context){
        //splitting the content into terms
        String[] allTerms = context.split("\r\n");
        String[] oneTerm = allTerms[0].split(" ");
        String method = oneTerm[0];
        String path = oneTerm[1];
        String version = oneTerm[2];
        String host = allTerms[1].split(" ")[1];
        String contleng = allTerms[4].split(" ")[1];


        for (int i = 2; i < allTerms.length; i++) {
            String header = allTerms[i];
            this.headers.add(header + "\r\n");
        }

        this.method = method;
        this.path = path;
        this.version = version;
        this.host = host;
        this.contentLength =contleng;
    }

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

    public List<String> getHeaders() {
        return headers;
    }

    public String getContentLength() {
        return contentLength;
    }

    /*public String getPayload() {
        return payload;
    }*/
}

