package com.networknt.oauth.code.handler;

import com.networknt.config.Config;
import com.networknt.status.Status;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ConnectException;

import static com.networknt.client.oauth.TokenHelper.encodeCredentials;


/**
* Generated by swagger-codegen
*/
public class Oauth2CodeGetHandlerTest {
    @ClassRule
    public static TestServer server = TestServer.getInstance();

    static final Logger logger = LoggerFactory.getLogger(Oauth2CodeGetHandlerTest.class);

    @Test(expected = ConnectException.class)
    public void testAuthorizationCode() throws Exception {
        String url = "http://localhost:6881/oauth2/code?response_type=code&client_id=59f347a0-c92d-11e6-9d9d-cec0c932ce01&redirect_uri=http://localhost:8080/authorization";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        // add authentication header
        httpGet.setHeader("Authorization", "Basic " + encodeCredentials("admin", "123456"));
        CloseableHttpResponse response = client.execute(httpGet);
        int statusCode = response.getStatusLine().getStatusCode();
        String body  = IOUtils.toString(response.getEntity().getContent(), "utf8");
        Assert.assertEquals(statusCode, 302);

        // at this moment, an exception will help as it is redirected to localhost:8080 and it is not up.
    }

    @Test
    public void testCodeWithoutResponseType() throws Exception {
        String url = "http://localhost:6881/oauth2/code?client_id=59f347a0-c92d-11e6-9d9d-cec0c932ce01&redirect_uri=http://localhost:8888/authorization";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Authorization", "Basic " + encodeCredentials("admin", "123456"));
        try {
            CloseableHttpResponse response = client.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, statusCode);
            if(statusCode == 400) {
                Status status = Config.getInstance().getMapper().readValue(response.getEntity().getContent(), Status.class);
                Assert.assertNotNull(status);
                Assert.assertEquals("ERR11000", status.getCode());
                Assert.assertEquals("VALIDATOR_REQUEST_PARAMETER_QUERY_MISSING", status.getMessage()); // response_type missing
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCodeWithoutClientId() throws Exception {
        String url = "http://localhost:6881/oauth2/code?response_type=code&redirect_uri=http://localhost:8888/authorization";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Authorization", "Basic " + encodeCredentials("admin", "123456"));
        try {
            CloseableHttpResponse response = client.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            Assert.assertEquals(400, statusCode);
            if(statusCode == 400) {
                Status status = Config.getInstance().getMapper().readValue(response.getEntity().getContent(), Status.class);
                Assert.assertNotNull(status);
                Assert.assertEquals("ERR11000", status.getCode());
                Assert.assertEquals("VALIDATOR_REQUEST_PARAMETER_QUERY_MISSING", status.getMessage()); // client_id missing
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCodeWrongPassword() throws Exception {
        String url = "http://localhost:6881/oauth2/code?response_type=code&client_id=59f347a0-c92d-11e6-9d9d-cec0c932ce01&redirect_uri=http://localhost:8888/authorization";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Authorization", "Basic " + encodeCredentials("admin", "admin"));
        try {
            CloseableHttpResponse response = client.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            String body  = IOUtils.toString(response.getEntity().getContent(), "utf8");
            Assert.assertEquals(401, statusCode);
            if(statusCode == 401) {
                Status status = Config.getInstance().getMapper().readValue(body, Status.class);
                Assert.assertNotNull(status);
                Assert.assertEquals("ERR12016", status.getCode());
                Assert.assertEquals("INCORRECT_PASSWORD", status.getMessage()); // client_id missing
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCodeInvalidResponseType() throws Exception {
        String url = "http://localhost:6881/oauth2/code?response_type=wrong&client_id=59f347a0-c92d-11e6-9d9d-cec0c932ce01&redirect_uri=http://localhost:8888/authorization";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Authorization", "Basic " + encodeCredentials("admin", "123456"));
        try {
            CloseableHttpResponse response = client.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            //String body  = IOUtils.toString(response.getEntity().getContent(), "utf8");
            Assert.assertEquals(400, statusCode);
            if(statusCode == 400) {
                Status status = Config.getInstance().getMapper().readValue(response.getEntity().getContent(), Status.class);
                Assert.assertNotNull(status);
                Assert.assertEquals("ERR11002", status.getCode());
                Assert.assertEquals("VALIDATOR_REQUEST_PARAMETER_ENUM_INVALID", status.getMessage()); // response type wrong
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCodeClientNotFound() throws Exception {
        String url = "http://localhost:6881/oauth2/code?response_type=code&client_id=fake&redirect_uri=http://localhost:8888/authorization";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Authorization", "Basic " + encodeCredentials("admin", "123456"));
        try {
            CloseableHttpResponse response = client.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            String body  = IOUtils.toString(response.getEntity().getContent(), "utf8");
            Assert.assertEquals(404, statusCode);
            if(statusCode == 404) {
                Status status = Config.getInstance().getMapper().readValue(body, Status.class);
                Assert.assertNotNull(status);
                Assert.assertEquals("ERR12014", status.getCode());
                Assert.assertEquals("CLIENT_NOT_FOUND", status.getMessage()); // client not found
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(expected = ConnectException.class)
    public void testAuthorizationCodePKCE() throws Exception {
        String url = "http://localhost:6881/oauth2/code?response_type=code&client_id=59f347a0-c92d-11e6-9d9d-cec0c932ce01&code_challenge=GIDiZShhVObyvaTrpkPM8VPmtMkj_qnBWlDwE7uz90s&code_challenge_method=S256&redirect_uri=http://localhost:8080/authorization";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        // add authentication header
        httpGet.setHeader("Authorization", "Basic " + encodeCredentials("admin", "123456"));
        CloseableHttpResponse response = client.execute(httpGet);
        int statusCode = response.getStatusLine().getStatusCode();
        String body  = IOUtils.toString(response.getEntity().getContent(), "utf8");
        Assert.assertEquals(statusCode, 302);

        // at this moment, an exception will help as it is redirected to localhost:8080 and it is not up.
    }

    @Test
    public void testCodePKCECodeChallengeMethodInvalid() throws Exception {
        String url = "http://localhost:6881/oauth2/code?response_type=code&client_id=59f347a0-c92d-11e6-9d9d-cec0c932ce01&code_challenge=GIDiZShhVObyvaTrpkPM8VPmtMkj_qnBWlDwE7uz90s&code_challenge_method=ABC&redirect_uri=http://localhost:8888/authorization";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Authorization", "Basic " + encodeCredentials("admin", "123456"));
        try {
            CloseableHttpResponse response = client.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            String body  = IOUtils.toString(response.getEntity().getContent(), "utf8");
            Assert.assertEquals(400, statusCode);
            if(statusCode == 400) {
                Status status = Config.getInstance().getMapper().readValue(body, Status.class);
                Assert.assertNotNull(status);
                Assert.assertEquals("ERR12033", status.getCode());
                Assert.assertEquals("INVALID_CODE_CHALLENGE_METHOD", status.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testCodePKCECodeChallengeTooShort() throws Exception {
        String url = "http://localhost:6881/oauth2/code?response_type=code&client_id=59f347a0-c92d-11e6-9d9d-cec0c932ce01&code_challenge=xzmujl&code_challenge_method=S256&redirect_uri=http://localhost:8888/authorization";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Authorization", "Basic " + encodeCredentials("admin", "123456"));
        try {
            CloseableHttpResponse response = client.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            String body  = IOUtils.toString(response.getEntity().getContent(), "utf8");
            Assert.assertEquals(400, statusCode);
            if(statusCode == 400) {
                Status status = Config.getInstance().getMapper().readValue(body, Status.class);
                Assert.assertNotNull(status);
                Assert.assertEquals("ERR12034", status.getCode());
                Assert.assertEquals("CODE_CHALLENGE_TOO_SHORT", status.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCodePKCECodeChallengeTooLong() throws Exception {
        String url = "http://localhost:6881/oauth2/code?response_type=code&client_id=59f347a0-c92d-11e6-9d9d-cec0c932ce01&code_challenge=xzmujlxzmujl-tX1OgdSrtB3oVFp4G3VHVvGbv81i8Nd-A62qgcmo0VDvOq_EaYJiSaM4fsx6oEqhHZfzhTcmcU4WjUAxzmujl-tX1OgdSrtB3oVFp4G3VHVvGbv81i8Nd-A62qgcmo0VDvOq_EaYJiSaM4fsx6oEqhHZfzhTcmcU4WjUA&code_challenge_method=S256&redirect_uri=http://localhost:8888/authorization";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Authorization", "Basic " + encodeCredentials("admin", "123456"));
        try {
            CloseableHttpResponse response = client.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            String body  = IOUtils.toString(response.getEntity().getContent(), "utf8");
            Assert.assertEquals(400, statusCode);
            if(statusCode == 400) {
                Status status = Config.getInstance().getMapper().readValue(body, Status.class);
                Assert.assertNotNull(status);
                Assert.assertEquals("ERR12035", status.getCode());
                Assert.assertEquals("CODE_CHALLENGE_TOO_LONG", status.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCodePKCECodeChallengeInvalidFormat() throws Exception {
        String url = "http://localhost:6881/oauth2/code?response_type=code&client_id=59f347a0-c92d-11e6-9d9d-cec0c932ce01&code_challenge=G$IiZShhVObyvaTrpkPM8VPmtMkj_qnBWlDwE7uz90s&code_challenge_method=S256&redirect_uri=http://localhost:8888/authorization";
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Authorization", "Basic " + encodeCredentials("admin", "123456"));
        try {
            CloseableHttpResponse response = client.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            String body  = IOUtils.toString(response.getEntity().getContent(), "utf8");
            Assert.assertEquals(400, statusCode);
            if(statusCode == 400) {
                Status status = Config.getInstance().getMapper().readValue(body, Status.class);
                Assert.assertNotNull(status);
                Assert.assertEquals("ERR12036", status.getCode());
                Assert.assertEquals("INVALID_CODE_CHALLENGE_FORMAT", status.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
