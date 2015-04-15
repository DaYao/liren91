package com.liren91.liren91.activity_page;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.liren91.liren91.Liren91Application;
import com.liren91.liren91.R;
import com.liren91.liren91.home_page.HomePageFragment;
import com.liren91.liren91.utils.MyLog;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivitiesPageFragment extends android.support.v4.app.Fragment implements SwipeRefreshLayout.OnRefreshListener
{
    MyLog log = new MyLog(this);
    SwipeRefreshLayout mSwipeRefreshLayout;
    View mView = null;
    LinearLayout mLinearLayout;
    TextView mDefaultText;
    ViewAnimator mViewAnimator;
    TextView mDateTextView;
    ImageView mImageView;
    TextView mActivityName;
    TextView mHostName;
    TextView mPosition;
    TextView mSawCount;
    TextView mJoinCount;

    public ActivitiesPageFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        if (mView != null)
        {
            return mView;
        }

        View view = inflater.inflate(R.layout.activities_fragment, container, false);
        mViewAnimator = (ViewAnimator) view.findViewById(R.id.activities_view_animator);
        mLinearLayout = (LinearLayout) view.findViewById(R.id.activities_layout);
        mDateTextView = ((TextView) view.findViewById(R.id.textView16));
        mImageView = (ImageView) view.findViewById(R.id.imageView);
        mActivityName = ((TextView) view.findViewById(R.id.textView17));
        mHostName = ((TextView) view.findViewById(R.id.textView19));
        mPosition = ((TextView) view.findViewById(R.id.textView20));
        mSawCount = ((TextView) view.findViewById(R.id.textView22));
        mJoinCount = ((TextView) view.findViewById(R.id.textView24));

        mDefaultText = (TextView) view.findViewById(R.id.activities_default_text);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activities_refresh);
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.swipe_color_1, R.color.swipe_color_2,
                R.color.swipe_color_3, R.color.swipe_color_4);

        mSwipeRefreshLayout.setOnRefreshListener(this);

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (mView == null)
        {
            onRefresh();
//            mSwipeRefreshLayout.setRefreshing(true);
    }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mView = getView();
    }

    @Override
    public void onRefresh()
    {
        if (((Liren91Application) getActivity().getApplication()).hasConnected(getActivity()))
        {
            new RefreshActivities().execute();
        }
    }

    private class RefreshActivities extends AsyncTask<Void, Void, Boolean>
    {
        String date, activityN, hostN, position, sawC, joinC, url;
        Bitmap bitmap;

        @Override
        protected Boolean doInBackground(Void... params)
        {
            final ActivitiesPageWeb web = new ActivitiesPageWeb();
            if (web.document == null)
            {
                log.w(this, "document=null");
                return false;
            }
            date = web.getDate();
            bitmap = web.getCoverImage();
            url = web.getDetailPageUrl();
            activityN = web.getActivityName();
            hostN = web.getHostName();
            position = web.getPosition();
            sawC = web.getSawCount();
            joinC = web.getJoinCount();

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean)
        {
            super.onPostExecute(aBoolean);
            if (aBoolean)
            {
                mDateTextView.setText(date);
                mImageView.setImageBitmap(bitmap);
                mImageView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    }
                });
                mActivityName.setText(activityN);
                mHostName.setText(hostN);
                mPosition.setText(position);
                mSawCount.setText(sawC);
                mJoinCount.setText(joinC);
                mViewAnimator.setDisplayedChild(1);
            }
            else
            {
                mViewAnimator.setDisplayedChild(0);
            }
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
