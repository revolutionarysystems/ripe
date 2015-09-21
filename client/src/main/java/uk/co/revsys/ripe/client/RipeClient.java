package uk.co.revsys.ripe.client;

import com.jayway.jsonpath.JsonPath;
import java.io.IOException;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
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
        parameters.put("flags", "no-irt");
        parameters.put("flags", "no-referenced");
        parameters.put("flags", "resource");
        request.setParameters(parameters);
        HttpResponse response = httpClient.invoke(request);
        if (response.getStatusCode() == 200) {
            String json = IOUtils.toString(response.getInputStream());
            System.out.println("json = " + json);
            RipeSearchResult result = new RipeSearchResult();
            result.setNetworkName(readJsonAttribute(json, "netname"));
            result.setNetworkDescription(readJsonAttribute(json, "descr"));
            return result;
        } else {
            throw new IOException("Ripe Server Error: " + response.getStatusCode());
        }
    }

    private String readJsonAttribute(String json, String attributeName) {
        JSONArray matches = JsonPath.read(json, "$.objects.object[0].attributes.attribute[?(@.name==" + attributeName + ")]");
        if (matches.isEmpty()) {
            return null;
        } else {
            JSONObject match = (JSONObject) matches.get(0);
            return match.get("value").toString();
        }
    }

}
