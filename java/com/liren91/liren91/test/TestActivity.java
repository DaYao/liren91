package com.liren91.liren91.test;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.liren91.liren91.R;
import com.liren91.liren91.utils.MyLog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TestActivity extends ActionBarActivity
        implements View.OnClickListener
{
    MyLog log = new MyLog(this);
    Button button1;
    Button button2;
    Button button3;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_activity);
//        button1 = (Button) findViewById(R.id.button1);
//        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        textView = (TextView) findViewById(R.id.textView);

//        button1.setOnClickListener(this);
//        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.button1:
                new SignOn().execute();
                break;
            case R.id.button2:
                new Recruit().execute();
                break;
        }
    }

    String cookie;

    private class SignOn extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            String result = "";
            String requestUrl = "http://www.91liren.com/user/login";

            try
            {
                URL url = new URL(requestUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoInput(true);
                PrintWriter pw = new PrintWriter(connection.getOutputStream());
                String param1 = "loginName=ggg%40163.com&password=hhh";
                String param2 = "loginName=hellogaopeng%40163.com&password=1234qwer";
                pw.write(param1);
                pw.flush();
                pw.close();
                log.v(this, connection.getResponseCode() + "");
                if (connection.getResponseCode() == 200)
                {
                    InputStream is = connection.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    int len = 0;
                    byte[] buff = new byte[1024];
                    while ((len = is.read(buff)) != -1)
                    {
                        bos.write(buff, 0, len);
                    }
                    is.close();
                    bos.close();
                    result = connection.getHeaderField("Set-Cookie");
                    cookie = result;
//                    result = bos.toString("utf-8");
                }
                connection.disconnect();
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            textView.setText(s);
        }
    }

    private class Recruit extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            String result = "";
            String zhaopinUrl = "http://www.91liren.com/toPositionPublishPage";
            String baidu = "http://www.baidu.com";
            try
            {
                URL url = new URL(zhaopinUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Cookie", cookie);
//                connection.setRequestMethod("POST");
//                connection.setDoInput(true);
//                connection.setDoInput(true);
//                PrintWriter pw = new PrintWriter(connection.getOutputStream());
//                String param1 = "loginName=ggg%40163.com&password=hhh";
//                String param2 = "loginName=hellogaopeng%40163.com&password=1234qwer";
//                pw.write(param1);
//                pw.flush();
//                pw.close();
                log.v(this, "recruit: " + connection.getResponseCode() + "");
                if (connection.getResponseCode() == 200)
                {
                    InputStream is = connection.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    int len = 0;
                    byte[] buff = new byte[1024];
                    while ((len = is.read(buff)) != -1)
                    {
                        bos.write(buff, 0, len);
                    }
                    is.close();
                    bos.close();

                    result = bos.toString("utf-8");
                }
                connection.disconnect();
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return result;

        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            textView.setText(s);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }
}
