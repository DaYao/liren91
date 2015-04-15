package com.liren91.liren91.deprecated.inner_recommend_page;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.liren91.liren91.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class InnerRecommedPageFragment extends android.support.v4.app.Fragment
{
View mView;
    LinearLayout mLinearLayout2;
    LinearLayout mLinearLayout1;
    LinearLayout mLinearLayout0;

    public InnerRecommedPageFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.inner_recommend_item, container, false);
        mLinearLayout2=(LinearLayout)view.findViewById(R.id.lin2);
        mLinearLayout1=(LinearLayout)view.findViewById(R.id.lin1);
        mLinearLayout0=(LinearLayout)view.findViewById(R.id.lin0);
        final Animation animation= AnimationUtils.loadAnimation(getActivity(),R.anim.inner_recommend_item_tween);

        mLinearLayout2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mLinearLayout0.startAnimation(animation);
                ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(-300,300);
                mLinearLayout0.setLayoutParams(params);
            }
        });


        return view;
    }


}
