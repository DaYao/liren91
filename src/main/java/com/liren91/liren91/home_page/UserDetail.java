package com.liren91.liren91.home_page;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import com.liren91.liren91.Liren91Application;
import com.liren91.liren91.R;
import com.liren91.liren91.utils.MyLog;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class UserDetail extends ActionBarActivity
{
    MyLog log = new MyLog(this);
     ExpandableListView mCompanyInfo;
    TextView mJobInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_detail_activity);
        mCompanyInfo = (ExpandableListView) findViewById(R.id.detail_expandable);
        mJobInfo = (TextView) findViewById(R.id.detail_job_info);
        Intent intent = getIntent();
        String url = intent.getStringExtra("detail_rul");
        mCompanyInfo.setGroupIndicator(null);
        mCompanyInfo.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener()
        {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
            {
                if(parent.isGroupExpanded(groupPosition))
                    ((ImageView) v.findViewById(R.id.detail_expandable_arrow)).setImageResource(R.drawable.down);
                else
                    ((ImageView) v.findViewById(R.id.detail_expandable_arrow)).setImageResource(R.drawable.up);
                return false;
            }
        });
//        mCompanyInfo.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener()
//        {
//            @Override
//            public void onGroupCollapse(int groupPosition)
//            {
//
//                log.i(this, "collapse");
//              ((ImageView) mCompanyInfo.getChildAt(groupPosition).findViewById(R.id.detail_expandable_arrow)).setImageResource(R.drawable.up);
//
//            }
//        });
//        mCompanyInfo.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener()
//        {
//            @Override
//            public void onGroupExpand(int groupPosition)
//            {
//                log.i(this, "expand");
//                ((ImageView) mCompanyInfo.getChildAt(groupPosition).findViewById(R.id.detail_expandable_arrow)).setImageResource(R.drawable.down);
//
//            }
//        });
        new GetUserDetail().execute(url);

    }

    private class GetUserDetail extends AsyncTask<String, Void, String[]>
    {
        @Override
        protected String[] doInBackground(String... params)
        {
            //0是lable，1是detail
//            String result0="",result1="";
            String[] result = new String[2];
            if (((Liren91Application) getApplication()).hasConnected(UserDetail.this))
                try
                {
                    result[0] = Jsoup.connect(params[0]).get().select("#content > div.contBody > div.morePosition > div.posFoot > div.bottom.mt_10.hidden").first().html().replace("<br>", "\r");
                    result[1] = Jsoup.connect(params[0]).get().select(".positionInfo > dl:nth-child(2) > dd:nth-child(2)").first().html().replace("<br>", "\r");
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            return result;
        }

        @Override
        protected void onPostExecute(String[] strings)
        {
            super.onPostExecute(strings);
            mJobInfo.setText(strings[1]);
            ArrayList<HashMap<String, String>> list = new ArrayList<>();
            HashMap<String, String> map = new HashMap<>();
            map.put("lable", "公司详情");
            list.add(map);
            ArrayList<HashMap<String, String>> list2 = new ArrayList<>();
            ArrayList<ArrayList<HashMap<String, String>>> list3 = new ArrayList<>();
            HashMap<String, String> map2 = new HashMap<>();
            map2.put("detail", strings[0]);
            list2.add(map2);
            list3.add(list2);
            ExpandableListAdapter adapter = new SimpleExpandableListAdapter(
                    UserDetail.this,
                    list,
                    R.layout.user_detail_company_info_group,
                    new String[]{"lable"},
                    new int[]{R.id.detail_company_info_lable},
                    list3,
                    R.layout.user_detail_company_info_child,
                    new String[]{"detail"},
                    new int[]{R.id.detail_company_info_detail}
            );
            mCompanyInfo.setAdapter(adapter);


        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        return false;
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_user_detail, menu);
//        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return false;
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings)
//        {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
    }
}
