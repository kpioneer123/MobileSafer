package com.zhy.http.okhttp.cookie;



import java.util.ArrayList;
import java.util.List;


import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;


public  class SimpleCookieJar implements CookieJar
{
    private static  List<Cookie> allCookies = new ArrayList<Cookie>();


	private static List<Cookie> cookies;

	public static List<Cookie> getCookies() {
		return cookies != null ? cookies : new ArrayList<Cookie>();
	}

	public static void setCookies(List<Cookie> cookies) {
		SimpleCookieJar.cookies = cookies;
	}

    public synchronized void saveFromResponse(HttpUrl url, List<Cookie> cookies)
    {
        allCookies.addAll(cookies);

        setCookies(cookies);
    }


    @Override
    public synchronized List<Cookie> loadForRequest(HttpUrl url)
    {
        List<Cookie> result = new ArrayList<Cookie>();
        for (Cookie cookie : allCookies)
        {
            if (cookie.matches(url))
            {
                result.add(cookie);
            }
        }
        return result;
    }

    public static List<Cookie>   getCookieList(HttpUrl url)
    {
        List<Cookie> result = new ArrayList<Cookie>();
        for (Cookie cookie : allCookies)
        {
            if (cookie.matches(url))
            {
                result.add(cookie);
            }
        }
        return result;
    }
}
