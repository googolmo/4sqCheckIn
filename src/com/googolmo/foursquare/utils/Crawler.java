package com.googolmo.foursquare.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * User: googolmo
 * Date: 12-6-17
 * Time: 下午1:05
 */
public class Crawler {

    /**
     * 抓取一个URL
     *
     * @param url
     * @param connectTimeout
     * @param readTimeout
     * @return
     * @throws IOException
     */
    public static InputStream crawlUrl(String url, int connectTimeout, int readTimeout) throws IOException {
        HttpURLConnection con = null;
        InputStream in = null;

        URL u = new URL(url);
        con = (HttpURLConnection) u.openConnection();
        con.setConnectTimeout(connectTimeout);
        con.setReadTimeout(readTimeout);
        con.setRequestMethod("GET");
        con.setDoInput(true);
        con.connect();
        in = con.getInputStream();

        return in;

    }

    /**
     * 抓取一个URL
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static InputStream crawlUrl(String url) throws IOException {
        return crawlUrl(url, 15000, 10000);
    }


}
