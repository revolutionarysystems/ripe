
package uk.co.revsys.ripe.client;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import uk.co.revsys.utils.http.HttpClientImpl;

public class RipeClientTest {

    public RipeClientTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSearch() throws Exception {
        String ipAddress = "82.22.146.176";
        RipeClient ripeClient = new RipeClient(new HttpClientImpl(), "http://rest.db.ripe.net");
        RipeSearchResult result = ripeClient.search(ipAddress);
        assertEquals("NTL-BIA-LONDON-ACTON", result.getNetworkName());
        assertEquals("NTL Infrastructure - Reading", result.getNetworkDescription());
    }

}