package com.liren91.liren91.home_page;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.liren91.liren91.Liren91Application;
import com.liren91.liren91.R;
import com.liren91.liren91.utils.MyLog;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A fragment representing a list of Items.
 * <p/>
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class HomePageFragment extends android.support.v4.app.Fragment implements
        AdapterView.OnItemClickListener,
        AbsListView.OnScrollListener,
        SwipeRefreshLayout.OnRefreshListener
{
    MyLog log = new MyLog(this);
    //    HomePageSimpleAdapter simpleAdapter;
//    private android.os.Handler handler = new Handler()
//    {
//        @Override
//        public void handleMessage(Message msg)
//        {
//            super.handleMessage(msg);
//            setListAdapter(simpleAdapter);
//            log.i(this, "适配器绑定完成");
//        }
//    };

    {
        log.i(this, "这个类开始使用");
    }

    ListView mListView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    View mView = null;
    SimpleAdapter mSimpleAdapter;
    GetDataHelper mGetDataHelper;

    private OnFragmentInteractionListener mListener;

    ArrayList<HashMap<String, Object>> mListViewItems = new ArrayList<>();//维护一个动态增删数据的ListView集合

    boolean mIsMoreListRun = false;
    boolean mIsFreshListFromNetRun = false;//若正在运行，则等待它完成后再从队列中取数据
    boolean mIsRefreshRun = false;


    public HomePageFragment()
    {
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            mListener = (OnFragmentInteractionListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        log.i(this, "onCreate");
        super.onCreate(savedInstanceState);
        mGetDataHelper = new GetDataHelper(getActivity());
        mGetDataHelper.restoreNlist(mListViewItems);

        mSimpleAdapter = new SimpleAdapter(
                getActivity(),
                mListViewItems,
                R.layout.home_page_item,
                HomePageWeb.FROM,
                HomePageWeb.TO
        );
        mSimpleAdapter.setViewBinder(new HomePageViewBinder(getActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (mView != null)
        {
            return mView;
        }

        View view = inflater.inflate(R.layout.home_page_fragment, container, false);
        mListView = (ListView) view.findViewById(R.id.home_page_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.home_page_refresh);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.swipe_color_1, R.color.swipe_color_2,
                R.color.swipe_color_3, R.color.swipe_color_4);
        mListView.setAdapter(mSimpleAdapter);
        mListView.setOnItemClickListener(HomePageFragment.this);
        mListView.setOnScrollListener(HomePageFragment.this);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        if (!mListViewItems.isEmpty()) mSimpleAdapter.notifyDataSetChanged();

        onRefresh();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        log.i(this, "onViewCreated begin");
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onPause()
    {
        log.i(this,"onPause");
        super.onPause();
        mView = getView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        log.i(this,"onResume");
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        log.i(this, "onDestoryView");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        log.i(this, "onDestory");
        mGetDataHelper.saveNlist(mListViewItems);
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        String s = (String) view.findViewById(R.id.job_name).getTag();
        log.v(this, s);
        Intent intent=new Intent(getActivity(),UserDetail.class);
        intent.putExtra("detail_rul",s);
        startActivity(intent);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
        if (view.getCount() > 0 && mIsMoreListRun == false && view.getLastVisiblePosition() == (view.getCount() - 1) && !mSwipeRefreshLayout.isRefreshing())
        {
            log.i(this, "滑到底啦");

            mSwipeRefreshLayout.setRefreshing(true);
            if (((Liren91Application) getActivity().getApplication()).hasConnected(getActivity()))
                new MoreList().execute(view.getCount());
            else
                mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh()
    {
        if (mIsRefreshRun == false)
            if (((Liren91Application) getActivity().getApplication()).hasConnected(getActivity()))
            {
                log.i(this, "onRefresh");
                new RefreshList().execute();
            }
    }


    private class RefreshList extends AsyncTask<String, String, ArrayList<HashMap<String, Object>>>
    {
        public RefreshList()
        {
            mIsRefreshRun = true;
        }


        @Override
        protected ArrayList<HashMap<String, Object>> doInBackground(String... params)
        {
            log.i(this, "RefreshList begin");
            return mGetDataHelper.getNewN();
        }

        @Override
        protected void onPostExecute(final ArrayList<HashMap<String, Object>> list)
        {
            super.onPostExecute(list);
            mListViewItems.clear();

            mListViewItems.addAll(list);
            mSimpleAdapter.notifyDataSetChanged();
            mGetDataHelper.clearListPool();
            mSwipeRefreshLayout.setRefreshing(false);
            if (getActivity() != null)
                Toast.makeText(getActivity(), "首页已刷新", Toast.LENGTH_SHORT).show();
            mIsRefreshRun = false;
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    mIsFreshListFromNetRun = true;
                    mGetDataHelper.freshListFromNet(0 + GetDataHelper.N);
                    mIsFreshListFromNetRun = false;
                }
            }).start();
        }
    }

    private class MoreList extends AsyncTask<Integer, Integer, ArrayList<HashMap<String, Object>>>
    {
        Thread myThread;

        @Override
        protected ArrayList<HashMap<String, Object>> doInBackground(final Integer... params)
        {
            mIsMoreListRun = true;
            while (mIsFreshListFromNetRun == false) break;
            log.i(this, "moreList在运行");
            myThread = null;
            myThread = new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    mIsFreshListFromNetRun = true;
                    mGetDataHelper.freshListFromNet(params[0] + GetDataHelper.N);
                    mIsFreshListFromNetRun = false;
                }
            });
            return mGetDataHelper.getList();
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, Object>> hashMaps)
        {
            super.onPostExecute(hashMaps);
            mListViewItems.addAll(hashMaps);
            mSimpleAdapter.notifyDataSetChanged();
            mIsMoreListRun = false;
            mSwipeRefreshLayout.setRefreshing(false);
            myThread.start();
        }
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(String id);
    }

}
