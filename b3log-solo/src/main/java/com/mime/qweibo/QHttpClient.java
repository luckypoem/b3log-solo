package com.mime.qweibo;

import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.HttpStatus;
//import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
//import org.apache.commons.httpclient.methods.GetMethod;
//import org.apache.commons.httpclient.methods.PostMethod;
//import org.apache.commons.httpclient.methods.multipart.FilePart;
//import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
//import org.apache.commons.httpclient.methods.multipart.Part;
//import org.apache.commons.httpclient.methods.multipart.StringPart;
//import org.apache.commons.httpclient.HttpStatus;
import java.net.URL;
import javax.servlet.http.HttpServletResponse;

public class QHttpClient {

    private static final int CONNECTION_TIMEOUT = 20000;
    private URLFetchService urlFetchService =
            URLFetchServiceFactory.getURLFetchService();

    public QHttpClient() {
    }

    /**
     * Using GET method.
     *
     * @param url
     *            The remote URL.
     * @param queryString
     *            The query string containing parameters
     * @return Response string.
     * @throws Exception
     */
    public String httpGet(String url, String queryString) throws Exception {
        String ret = null;

        if (queryString != null && !queryString.equals("")) {
            url += "?" + queryString;
        }


        final HTTPRequest request = new HTTPRequest(new URL(url));
//		GetMethod httpGet = new GetMethod(url);
//        httpGet.getParams().setParameter("http.socket.timeout",
//                                         new Integer(CONNECTION_TIMEOUT));

        try {
            final HTTPResponse response = urlFetchService.fetch(request);
            if (HttpServletResponse.SC_OK != response.getResponseCode()) {
                System.err.println("HttpGet Method failed: "
                                   + response.getResponseCode());
            }
//            int statusCode = httpClient.executeMethod(httpGet);
//            if (statusCode != HttpStatus.SC_OK) {
//                System.err.println("HttpGet Method failed: "
//                                   + httpGet.getStatusLine());
//            }
            // Read the response body.
//            responseData = httpGet.getResponseBodyAsString();
            ret = new String(response.getContent(), "UTF-8");

        } catch (Exception e) {
            throw new Exception(e);
        } finally {
//            httpGet.releaseConnection();
//            httpClient = null;
        }

        return ret;
    }

    /**
     * Using POST method.
     *
     * @param url
     *            The remote URL.
     * @param queryString
     *            The query string containing parameters
     * @return Response string.
     * @throws Exception
     */
    public String httpPost(String url, String queryString) throws Exception {
        String ret = null;

//        HttpClient httpClient = new HttpClient();
//        PostMethod httpPost = new PostMethod(url);

        final HTTPRequest request = new HTTPRequest(new URL(url),
                                                    HTTPMethod.POST);
//        request.setHeader(new HTTPHeader("Content-Type",
//                                         "application/x-www-form-urlencoded"));
        request.setHeader(new HTTPHeader("Content-Type",
                                         "text/html"));
//        httpPost.addParameter("Content-Type",
//                              "application/x-www-form-urlencoded");
//        httpPost.getParams().setParameter("http.socket.timeout",
//                                          new Integer(CONNECTION_TIMEOUT));
        if (queryString != null && !queryString.equals("")) {
//            httpPost.setRequestEntity(new ByteArrayRequestEntity(queryString.
//                    getBytes()));
            request.setPayload(queryString.getBytes("UTF-8"));
        }

        try {
//            int statusCode = httpClient.executeMethod(httpPost);
            final HTTPResponse response = urlFetchService.fetch(request);
            if (HttpServletResponse.SC_OK != response.getResponseCode()) {
                System.err.println("HttpPost Method failed: "
                                   + response.getResponseCode());
            }
//            if (statusCode != HttpStatus.SC_OK) {
//                System.err.println("HttpPost Method failed: "
//                                   + httpPost.getStatusLine());
//            }
//            ret = httpPost.getResponseBodyAsString();
            ret = new String(response.getContent(), "UTF-8");
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
//			httpPost.releaseConnection();
//			httpClient = null;
        }

        return ret;
    }
    /**
     * Using POST method with multiParts.
     *
     * @param url
     *            The remote URL.
     * @param queryString
     *            The query string containing parameters
     * @param files
     *            The list of image files
     * @return Response string.
     * @throws Exception
     */
//	public String httpPostWithFile(String url, String queryString,
//			List<QParameter> files) throws Exception {
//
//		String responseData = null;
//		url += '?' + queryString;
//		HttpClient httpClient = new HttpClient();
//		PostMethod httpPost = new PostMethod(url);
//		try {
//			List<QParameter> listParams = QHttpUtil
//					.getQueryParameters(queryString);
//			int length = listParams.size() + (files == null ? 0 : files.size());
//			Part[] parts = new Part[length];
//			int i = 0;
//			for (QParameter param : listParams) {
//				parts[i++] = new StringPart(param.mName,
//						QHttpUtil.formParamDecode(param.mValue), "UTF-8");
//			}
//			for (QParameter param : files) {
//				File file = new File(param.mValue);
//				parts[i++] = new FilePart(param.mName, file.getName(), file,
//						QHttpUtil.getContentType(file), "UTF-8");
//			}
//
//			httpPost.setRequestEntity(new MultipartRequestEntity(parts,
//					httpPost.getParams()));
//
//			int statusCode = httpClient.executeMethod(httpPost);
//			if (statusCode != HttpStatus.SC_OK) {
//				System.err.println("HttpPost Method failed: "
//						+ httpPost.getStatusLine());
//			}
//			responseData = httpPost.getResponseBodyAsString();
//		} catch (Exception e) {
//			throw new Exception(e);
//		} finally {
//			httpPost.releaseConnection();
//			httpClient = null;
//		}
//
//		return responseData;
//	}
}
