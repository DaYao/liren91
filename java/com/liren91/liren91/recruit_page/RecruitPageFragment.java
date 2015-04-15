package com.liren91.liren91.recruit_page;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ViewAnimator;

import com.liren91.liren91.Liren91Application;
import com.liren91.liren91.LoginActivity;
import com.liren91.liren91.R;
import com.liren91.liren91.utils.MyLog;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecruitPageFragment extends android.support.v4.app.Fragment
{
    MyLog log = new MyLog(this);
    public static final int RECRUIT = 2;
    Button mButton;
    ViewAnimator mViewAnimator;
    AnimatorReceiver receiver;

    public RecruitPageFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.recruit_fragment, container, false);
        mButton = (Button) v.findViewById(R.id.recruit_page_button);
        mViewAnimator = (ViewAnimator) v.findViewById(R.id.recruit_page_view_animator);

        mButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivityForResult(new Intent(getActivity(), LoginActivity.class), RECRUIT);
            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        IntentFilter filter = new IntentFilter("com.liren91.liren91.LOGOUT");
        receiver=new AnimatorReceiver();
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if (((Liren91Application) getActivity().getApplication()).isLogin())
            mViewAnimator.setDisplayedChild(1);
        else
            mViewAnimator.setDisplayedChild(0);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case RECRUIT:
                if (data != null)
                    if (data.getBooleanExtra("isLoginOk", false))
                    {
                        mViewAnimator.setDisplayedChild(1);
                    }
                break;
        }
    }

    class AnimatorReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            log.i(this,"onReceive");
            mViewAnimator.setDisplayedChild(0);
        }
    }

}
