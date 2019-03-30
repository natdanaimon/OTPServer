/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.mm.util;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

public class LineNotify {

    public static void sendMessage(String message) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {

            HttpClientContext localContext = HttpClientContext.create();
            HttpPost httpget = new HttpPost("https://notify-api.line.me/api/notify");
            String token = PropertiesConfig.getConfig("line.token");
            httpget.addHeader("Authorization", "Bearer " + token);
            httpget.addHeader("Content-Type", "application/x-www-form-urlencoded");
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("message", message));
            httpget.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

            // Pass local context as a parameter
            CloseableHttpResponse response = httpclient.execute(httpget, localContext);
        } catch (UnsupportedEncodingException ex) {

        } catch (IOException ex) {

        } finally {
            try {
                httpclient.close();
            } catch (IOException ex) {

            }
        }
    }



}
