package com.cip.ferrari.admin.core.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http util to send data
 * 
 * @author xuxueli
 * @version 2015-11-28 15:30:59
 */
public class HttpUtil {

	private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

	// response param
	public static final String status = "status";
	public static final String msg = "msg";
	// response status enum
	public static final String SUCCESS = "SUCCESS";
	public static final String FAIL = "FAIL";

	/**
	 * http post request
	 * 
	 * @param reqURL
	 * @param params
	 * @return [0]=responseMsg, [1]=exceptionMsg
	 */
	public static String[] post(String reqURL, Map<String, String> params) {
		String responseMsg = null;
		String exceptionMsg = null;

		// do post
		HttpPost httpPost = null;
		CloseableHttpClient httpClient = null;
		try {
			httpPost = new HttpPost(reqURL);
			httpClient = HttpClients.createDefault();
			if (params != null && !params.isEmpty()) {
				List<NameValuePair> formParams = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> entry : params.entrySet()) {
					formParams.add(new BasicNameValuePair(entry.getKey(), entry
							.getValue()));
				}
				httpPost.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8"));
			}
			RequestConfig requestConfig = RequestConfig.custom()
					.setSocketTimeout(5000).setConnectTimeout(5000).build();
			httpPost.setConfig(requestConfig);

			HttpResponse response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				if (null != entity) {
					responseMsg = EntityUtils.toString(entity, "UTF-8");
					EntityUtils.consume(entity);
				}
			} else {
				exceptionMsg = "http请求返回错误,code:"
						+ response.getStatusLine().getStatusCode() + ",reason:"
						+ response.getStatusLine().getReasonPhrase();
			}

		} catch (Exception e) {
			logger.error("send http post exception,requrl:" + reqURL
					+ ",params:" + params, e);
			StringWriter out = new StringWriter();
			e.printStackTrace(new PrintWriter(out));
			exceptionMsg = out.toString();
		} finally {
			if (httpPost != null) {
				httpPost.releaseConnection();
			}
			if (httpClient != null) {
				try {
					httpClient.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}

		String[] result = new String[2];
		result[0] = responseMsg;
		result[1] = exceptionMsg;
		return result;
	}
}
