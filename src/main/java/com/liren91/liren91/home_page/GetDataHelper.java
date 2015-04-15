package com.liren91.liren91.home_page;

import android.content.Context;

import com.liren91.liren91.home_page.HomePageWeb;
import com.liren91.liren91.utils.MyLog;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by smallQ on 15-4-3.
 */

//这个类用来从缓存优先获取数据
public class GetDataHelper
{

    MyLog log = new MyLog(this);

    BlockingQueue<HashMap<String, Object>> mListQueue = new ArrayBlockingQueue<>(N);
    static int mPageNumber = 0;
    static int mPosition = 0;
    static int mListNumber = 0;
    public static final int N = 6;
    HomePageWeb mHomePageWeb;
    Context mContext;

    public GetDataHelper(Context context)
    {
        this.mContext = context;
        mHomePageWeb = new HomePageWeb(mContext);
    }

    //保存N个list供下次启动软件时加载
    public void saveNlist(ArrayList<HashMap<String, Object>> list)
    {
        log.i(this, "begin save list");
        ArrayList<HashMap<String, Object>> subList = new ArrayList<>();
        int size = list.size();
        if (size < N)
        {
            subList.addAll(list.subList(0, size));
        }
        else
        {
            subList.addAll(list.subList(0, N));
        }

        OutputStream os = null;

        try
        {
            mContext.deleteFile("five_list");
            os = mContext.openFileOutput("five_list", Context.MODE_PRIVATE);
            log.i(this, "file created");
        }
        catch (FileNotFoundException e)
        {
            log.e(this, "file not create");
//            e.printStackTrace();
            return;
        }

        ObjectOutputStream oos = null;

        try
        {
            oos = new ObjectOutputStream(os);
            oos.writeObject(subList);
            oos.close();
            log.i(this, "file write ok");
        }
        catch (IOException e)
        {
            log.e(this, "file write failure");
            e.printStackTrace();
        }
    }

    //从存储器恢复N个list
    public void restoreNlist(ArrayList<HashMap<String, Object>> list)
    {
        log.i(this, "begin restore list");
        InputStream is = null;
        try
        {
            is = mContext.openFileInput("five_list");
            log.i(this, "file exesit");
        }
        catch (FileNotFoundException e)
        {
            log.e(this, "file not found");
//            e.printStackTrace();
            return;
        }

        ObjectInputStream ois = null;
        try
        {
            ois = new ObjectInputStream(is);

            log.i(this, "file read ok");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        try
        {
            Object object = ois.readObject();
            ois.close();
            log.i(this, "object read ok");
            ArrayList<HashMap<String, Object>> getedList = ((ArrayList<HashMap<String, Object>>) object);
            log.i(this, "从文件恢复为对象ok");
            if (getedList != null) list.addAll(getedList);
        }
        catch (ClassNotFoundException | IOException e)
        {
            e.printStackTrace();
        }
        catch (NullPointerException e)
        {
            log.e(this, "object read failure");
            e.printStackTrace();
        }
    }

    //清空mListPool
    public void clearListPool()
    {
        mListQueue.clear();
        mPageNumber = 0;
        mPosition = 0;
        mListNumber = 0;
    }

    //从网络获得最新的N条
    public ArrayList<HashMap<String, Object>> getNewN()
    {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < N; i++)
        {
            try
            {
                list.add(mHomePageWeb.getWhichListOfWhichPage((0 + i) / 20 + 1, (0 + i) % 20));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (NullPointerException e)
            {
                e.printStackTrace();
            }
        }
        return list;
    }

    //从网络获得N条list添加到mListQueue中
    public synchronized void freshListFromNet(int position)
    {
        for (int i = 0; i < N; i++)
        {
            try
            {
                mListQueue.add(mHomePageWeb.getWhichListOfWhichPage((position + i) / 20 + 1, (position + i) % 20));
                log.i(this, "第" + ((position + i) / 20 + 1) + "页," + "第" + (position + i) % 20 + "项");
            }
            catch (IllegalStateException e)
            {
                log.i(this, "队列满" + mListQueue.size());
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (NullPointerException e)
            {
                log.e(this, "没有更多的list了");
                e.printStackTrace();
            }
        }
        log.v(this, "传入的position为：" + position);
    }

    //从mListPool获取N条list
    public ArrayList<HashMap<String, Object>> getList()
    {
        ArrayList<HashMap<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < N; i++)
        {
            try
            {
                list.add(mListQueue.take());
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        return list;
    }

}
