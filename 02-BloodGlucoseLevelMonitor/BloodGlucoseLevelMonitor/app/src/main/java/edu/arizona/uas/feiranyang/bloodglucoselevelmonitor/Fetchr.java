package edu.arizona.uas.feiranyang.bloodglucoselevelmonitor;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class Fetchr {

    public byte[] getUrlBytes(String urlSpec, String entry) throws IOException{

        try {
            upload(entry);
        }catch (Exception e){
            System.out.println("something wrong");
            throw e;
        }
        return new byte[0];
    }

    public String getUrlString(String urlSpec,String entry) throws IOException{
        return new String(getUrlBytes(urlSpec,entry));
    }

    public void upload(String entry)throws IOException{
        URL url = new URL("http://u.arizona.edu/~lxu/cscv381/local_glucose.php");

        Map<String,Object> params = new LinkedHashMap<>();
        // set username, password, data parameter
        params.put("username", "feiranyang");
        params.put("password", "a5502");
        params.put("data", entry);

        StringBuilder postData = new StringBuilder();

        //establish connection with params
        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
    }
}
