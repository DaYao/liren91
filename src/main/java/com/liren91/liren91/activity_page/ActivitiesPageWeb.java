package com.liren91.liren91.activity_page;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by smallQ on 15-4-13.
 */
public class ActivitiesPageWeb
{
    private static final String ACTIVITIES_PAGE_URL = "http://www.91liren.com/activity/toIndex";
    public Document document;

    public ActivitiesPageWeb()
    {
        this.document = getDocument();
    }

    Document getDocument()
    {
        try
        {
            return Jsoup.connect(ACTIVITIES_PAGE_URL).get();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    String getDate()
    {
        if(document!=null)
            return document.select(".pic_titletxt").text();
        else
            return "";
    }

    String getCoverImageUrl()
    {
        if (document != null)
            return document.select(".piclist > ul:nth-child(1) > li:nth-child(1) > a:nth-child(1) > img:nth-child(1)").attr("src");
        else
            return "";
    }

    Bitmap getCoverImage()
    {
        try
        {
            URL url = new URL(getCoverImageUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            return BitmapFactory.decodeStream(connection.getInputStream());

        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    String getDetailPageUrl()
    {
        if (document != null)
            return document.select(".piclist > ul:nth-child(1) > li:nth-child(1) > a:nth-child(1)").attr("href");
        else
            return "";
    }

    String getActivityName()
    {
        if (document != null)
            return document.select("a.name").text();
        else
            return "";
    }

    String getHostName()
    {
        if (document != null)
            return document.select("span.name").text();
        else
            return "";
    }

    String getPosition()
    {
        if (document != null)
            return document.select(".piclistm > p:nth-child(3) > span:nth-child(1)").text();
        else
            return "";
    }

    String getSawCount()
    {
        if (document != null)
            return document.select(".piclistbl").text();
        else
            return "";
    }

    String getJoinCount()
    {
        if (document != null)
            return document.select(".piclistbr").text();
        else
            return "";
    }

    String getTotalCount()
    {
        if (document != null)
            return document.select(".piclistbr > span:nth-child(1)").text();
        else
            return "";
    }

}
