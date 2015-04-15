package com.liren91.liren91.home_page;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liren91.liren91.R;
import com.liren91.liren91.utils.MyLog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by smallQ on 15-3-24.
 */
public class HomePageWeb
{
    MyLog log = new MyLog(this);
    public final String HOME_PAGE_URL;
    public static final int PAGE_LIST_NUM = 20;
    private Context mContext;

    public static final String[] FROM = {
            "avatar",
            "job_name",
            "require",
            "company_position",
            "company_name",
            "company_info",
            "layout_welfare",
            "slogan"
    };
    public static final int[] TO = {
            R.id.avatar,
            R.id.job_name,
            R.id.require,
            R.id.company_position,
            R.id.company_name,
            R.id.company_info,
            R.id.layout_welfare,
            R.id.slogan
    };

    public HomePageWeb(Context context, int pageNumber)
    {
        HOME_PAGE_URL = "http://www.91liren.com/toLirenIndex?type=1&pageNum=" + pageNumber;
        this.mContext = context;
    }

    public HomePageWeb(Context context)
    {
        this.mContext = context;
        HOME_PAGE_URL = "http://www.91liren.com/toLirenIndex?type=1&pageNum=";
    }

    //获得第几页一共几项
    public int getCountInPage(int pageNum) throws IOException
    {
        return getElements(HOME_PAGE_URL + pageNum).size();
    }

    //获得第几页第几项
    public HashMap<String, Object> getWhichListOfWhichPage(int pageNum, int listNum) throws IOException
    {
        HashMap<String, Object> map = new HashMap<>();
        Element e = getElements(HOME_PAGE_URL + pageNum).get(listNum);
        map.put("avatar", getAvatar(getAvatarURL(e)));
        map.put("job_name", getJobName(e));
        map.put("require", getRecruitRequireString(e));
        map.put("company_position", getCompanyPosition(e));
        map.put("company_name", getCompanyName(e));
        map.put("company_info", getCompanyInfo(e));
        map.put("layout_welfare", getCompanyWelfare(e));
        map.put("slogan", getCompanySlogan(e));
        map.put("detail_page_url", getDetailPageURL(e));
        return map;
    }

    //获取封装好的20个list
    public ArrayList<HashMap<String, Object>> get20List()
    {
        log.i(this, "get20list即将执行");
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<>();
        try
        {
            for (Element e : getElements(HOME_PAGE_URL))
            {
                HashMap<String, Object> map = new HashMap<>();
                map.put("avatar", getAvatar(getAvatarURL(e)));
//                map.put("avatar",getAvatarURL(e));
                map.put("job_name", getJobName(e));
                map.put("require", getRecruitRequireString(e));
                map.put("company_position", getCompanyPosition(e));
                map.put("company_name", getCompanyName(e));
                map.put("company_info", getCompanyInfo(e));
                map.put("layout_welfare", getCompanyWelfare(e));
                map.put("slogan", getCompanySlogan(e));
//                map.put("data_position_id", getDataPositionid(e));
                map.put("detail_page_url", getDetailPageURL(e));
                listItem.add(map);
            }
        }
        catch (Exception e)
        {
            log.e(this, "get20list出错啦");
            e.printStackTrace();
        }
        return listItem;
    }

    //获得整个网页DOM
    private Document getFullLiren91HomePageByJsoup(String url) throws IOException
    {
        return Jsoup.connect(url).get();
    }

    //获得20个list
    private Elements getElements(String url) throws IOException
    {
        return getFullLiren91HomePageByJsoup(url).select(".list");
    }

    //获得list编号
    public int getDataPositionid(Element element)
    {
        return Integer.valueOf(element.select(".list_info").attr("data-positionid"));
    }

    //获得list指向的页面
    public String getDetailPageURL(Element element)
    {
        return element.select(".list_info>a").first().attr("href");
    }

    //获得头像地址
    private String getAvatarURL(Element element)
    {
        log.v(this, element.select(".list_info>a>img").first().attr("src"));
        return element.select(".list_info>a>img").first().attr("src");
    }

    //下载头像
    private byte[] getAvatar(String avatarURL)
    {
        byte[] bitmapArray = null;
        try
        {
            URL url = new URL(avatarURL);
            HttpURLConnection connection = ((HttpURLConnection) url.openConnection());
            connection.setConnectTimeout(5000);
            connection.setDoInput(true);
            InputStream in = connection.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int num = 0;
            while ((num = in.read(buff)) != -1)
            {
                baos.write(buff, 0, num);
            }
            bitmapArray = baos.toByteArray();
            in.close();
            connection.disconnect();

            log.i(this, "头像下载");
            return bitmapArray;
        }
        catch (Exception e)
        {
            log.e(this, "下载头像出错啦");
            e.printStackTrace();
        }
        return null;
    }

    //获得职位名称和详细页面的地址
    private String[] getJobName(Element element)
    {
        log.v(this, element.select(".list_info>h2>a").text());
        return new String[]{
                element.select(".list_info>h2>a").text(),
                getDetailPageURL(element)
        };
    }

    //获得招聘要求和待遇
    private SpannableStringBuilder getRecruitRequire(Element element)
    {
        String text = element.select(".list_info>ul>li").first().text().trim();
        log.v(this, text);
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.parseColor("#e74c3c"));
        int start, end;
        start = text.indexOf("|") + 1;
        end = text.indexOf("|", start);
        log.v(this, "start: " + start + " end: " + end);
        builder.setSpan(redSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return builder;
    }

    private String getRecruitRequireString(Element element)
    {
        return element.select(".list_info>ul>li").first().text().trim();
    }

    //获得公司地点
    private String getCompanyPosition(Element element)
    {
        log.v(this, element.select(".list_info>ul>li").last().text().trim());
        return element.select(".list_info>ul>li").last().text().trim();
    }

    //获得点赞链接

    //获得分享链接

    //获得公司名称
    private String getCompanyName(Element element)
    {
        log.v(this, element.select(".more>ul>li:nth-child(1)>p").text().trim());
        return element.select(".more>ul>li:nth-child(1)>p").text().trim();
    }

    //获得公司行业和人数规模
    private String getCompanyInfo(Element element)
    {
        log.v(this, element.select(".more>ul>li:nth-child(2)>p").text().trim());
        return element.select(".more>ul>li:nth-child(2)>p").text().trim();
    }

    //获得公司提供的福利
    private String[] getCompanyWelfare(Element element)
    {
        Elements welfareElements = element.select(".more>ul>li:nth-child(3)>span");
        int length = welfareElements.size();
        String[] welfareItems = new String[length];
        int i = 0;
        for (Element e : welfareElements)
        {
            welfareItems[i] = e.text();
            i++;
            log.v(this, e.text());
        }
        return welfareItems;
    }

    //制作包装好福利的视图
    private ArrayList<LinearLayout> getWelfareLayout(String[] welfare)
    {
        int length = welfare.length;
        ArrayList<LinearLayout> layouts = new ArrayList<>(length);
        for (int i = 0; i < length; i++)
        {
            log.i(this, "即将制作layout视图");
            LinearLayout layout = new LinearLayout(mContext);
            layout.setBackgroundColor(Color.parseColor("#cccccc"));
            layout.setPaddingRelative(1, 1, 1, 1);
            LinearLayout.LayoutParams ml = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ml.setMargins(0, 0, 4, 2);
            layout.setLayoutParams(ml);

            log.i(this, "即将吧textView都装入layout");
            TextView textView = new TextView(mContext);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setBackgroundColor(Color.WHITE);
            textView.setPaddingRelative(4, 0, 4, 0);
            textView.setText(welfare[i]);
            layout.addView(textView);

            layouts.add(layout);
        }
        log.i(this, "视图完成了");
        return layouts;
    }

    //获得公司的企业口号
    private String getCompanySlogan(Element element)
    {
        log.v(this, element.select(".more>ul>li:nth-child(4)>p").text());
        return element.select(".more>ul>li:nth-child(4)>p").text();
    }

    //获得申请按钮

    //获得收藏按钮
}
