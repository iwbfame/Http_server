package ru.netology;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Request {
    private String requestPath;
    private List<NameValuePair> queryParams;

    public Request(String requestPath) throws URISyntaxException {
        URI uri = new URI(requestPath);
        this.requestPath = uri.getPath();
        this.queryParams = URLEncodedUtils.parse(uri, StandardCharsets.UTF_8);
    }

    public String getPath() {
        return requestPath;
    }

    public String getQueryParam(String name) {
        for (NameValuePair param : queryParams) {
            if (param.getName().equals(name)) {
                return param.getValue();
            }
        }
        return null;
    }

    public List<NameValuePair> getQueryParams() {
        return queryParams;
    }
}
