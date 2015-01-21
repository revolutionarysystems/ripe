package uk.co.revsys.ripe.client;

import com.jayway.jsonpath.JsonPath;
import java.io.IOException;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.io.IOUtils;
import uk.co.revsys.utils.http.HttpClient;
import uk.co.revsys.utils.http.HttpClientImpl;
import uk.co.revsys.utils.http.HttpRequest;
import uk.co.revsys.utils.http.HttpResponse;

public class RipeClient {

    private final HttpClient httpClient;
    private final String baseUrl;

    public RipeClient() {
        this(new HttpClientImpl());
    }

    public RipeClient(HttpClient httpClient) {
        this(httpClient, "http://rest.db.ripe.net");
    }

    public RipeClient(HttpClient httpClient, String baseUrl) {
        this.httpClient = httpClient;
        this.baseUrl = baseUrl;
    }

    public RipeSearchResult search(String ipAddress) throws IOException {
        HttpRequest request = HttpRequest.GET(baseUrl + "/search.json");
        MultiValueMap parameters = new MultiValueMap();
        parameters.put("query-string", ipAddress);
        parameters.put("flag", "no-irt");
        parameters.put("flag", "no-referenced");
        parameters.put("flag", "resource");
        request.setParameters(parameters);
        HttpResponse response = httpClient.invoke(request);
        String json = IOUtils.toString(response.getInputStream());
        RipeSearchResult result = new RipeSearchResult();
        result.setNetworkName(JsonPath.read(json, "$.objects.object[0].attributes.attribute[?(@.name==netname)][0].value").toString());
        result.setNetworkDescription(JsonPath.read(json, "$.objects.object[0].attributes.attribute[?(@.name==descr)][0].value").toString());
        return result;
    }

}
