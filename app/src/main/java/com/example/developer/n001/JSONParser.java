package com.example.developer.n001;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by developer on 11/1/2015.
 */
public class JSONParser {

    public JSONObject makeHtttpRequest(String URL,List<NameValuePair>pairs)
    {
        try{
            HttpParams httpParams=new BasicHttpParams();
            int timeOutConnection=10000;
            HttpConnectionParams.setConnectionTimeout(httpParams,timeOutConnection);
            int timeOutSocket=10000;
            HttpConnectionParams.setConnectionTimeout(httpParams,timeOutSocket);

            DefaultHttpClient defaultHttpClient=new DefaultHttpClient(httpParams);
            HttpPost httpPost=new HttpPost(URL);
            if(pairs!=null)
            {
                httpPost.setEntity(new UrlEncodedFormEntity(pairs));

            }
            HttpResponse httpResponse=defaultHttpClient.execute(httpPost);
            HttpEntity httpEntity=httpResponse.getEntity();
            if (httpEntity!=null)
            {
                String ret= EntityUtils.toString(httpEntity);
                return new JSONObject(ret.substring(ret.indexOf("{"),ret.lastIndexOf("}")+1));
            }



        }
        catch (Exception e){


        }


        return null;
    }


}
