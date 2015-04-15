package com.liren91.liren91;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.liren91.liren91.activity_page.ActivitiesPageFragment;
import com.liren91.liren91.home_page.HomePageFragment;
import com.liren91.liren91.recruit_page.RecruitPageFragment;
import com.liren91.liren91.utils.ImageDecorater;
import com.liren91.liren91.utils.MyLog;

import java.util.Locale;


public class MainActivity extends ActionBarActivity implements
        ActionBar.TabListener,
        HomePageFragment.OnFragmentInteractionListener,
        View.OnClickListener
{
    MyLog log = new MyLog(this);

    @Override
    public void onFragmentInteraction(String id)
    {

    }

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    ViewAnimator mViewAnimator;
    Button mLoginButton;
    Button mLogoutButton;
    ImageView mUserAvatar;
    TextView mUserName;

    public static final int LOGIN_COCE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setHomeButtonEnabled(false);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
        {
            @Override
            public void onPageSelected(int position)
            {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++)
        {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

        mViewAnimator = (ViewAnimator) findViewById(R.id.view_animator);
        mLoginButton = (Button) findViewById(R.id.login_button);
        mLogoutButton = (Button) findViewById(R.id.logout_button);
        mUserName = (TextView) findViewById(R.id.user_name);
        mUserAvatar = (ImageView) findViewById(R.id.login_avatar);

        mLoginButton.setOnClickListener(this);
        mLogoutButton.setOnClickListener(this);
        Log.i("MainActivity", "onCreate finish");
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        boolean isLogin = ((Liren91Application) getApplication()).isLogin();
        if (isLogin)
        {
            SharedPreferences setting = getSharedPreferences("LirenCookie", 0);
            log.v(this, setting.getString("userName", "null"));
            Bitmap source = BitmapFactory.decodeResource(getResources(), R.drawable.avatar);
            Bitmap target = ImageDecorater.createCiecleImage(source);
            mUserAvatar.setImageBitmap(target);
            mUserName.setText(setting.getString("userName", ""));
            mViewAnimator.setDisplayedChild(1);
        }
        else
        {
            Bitmap source = BitmapFactory.decodeResource(getResources(), R.drawable.default_login_avatar);
            Bitmap target = ImageDecorater.createCiecleImage(source);
            ((ImageView) findViewById(R.id.default_avatar)).setImageBitmap(target);
            mViewAnimator.setDisplayedChild(0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case LOGIN_COCE:
                if (data != null)
                    if (data.getBooleanExtra("isLoginOk", false))
                    {
                        log.v(this, data.getStringExtra("userName"));
                        mUserName.setText(data.getStringExtra("userName"));
                        mViewAnimator.setDisplayedChild(1);
                    }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        if (id == R.id.about)
        {
            startActivity(new Intent(this, About.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
    {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition(), true);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
    {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction)
    {
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.login_button:
                startActivityForResult(new Intent(this, LoginActivity.class), LOGIN_COCE);
//                overridePendingTransition(R.anim.abc_fade_in,R.anim.abc_fade_out);
                break;
            case R.id.logout_button:
                mViewAnimator.setDisplayedChild(0);
                SharedPreferences setting = getSharedPreferences("LirenCookie", 0);
                SharedPreferences.Editor editor = setting.edit();
                editor.putBoolean("isLogin", false);
                editor.commit();
                sendBroadcast(new Intent("com.liren91.liren91.LOGOUT"));
                break;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter
    {

        public SectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return FragmentFactory.newInstance(position);
        }

        @Override
        public int getCount()
        {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            Locale l = Locale.getDefault();
            switch (position)
            {
                case 0:
                    return "首页";
                case 1:
                    return "招聘";
//                case 2:
//                    return "内推";
                case 2:
                    return "活动";
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class FragmentFactory
    {
        public static Fragment newInstance(int position)
        {
            switch (position)
            {
                case 0:
                    return new HomePageFragment();
                case 1:
                    return new RecruitPageFragment();
//                case 2:
//                    return new InnerRecommedPageFragment();
                case 2:
                    return new ActivitiesPageFragment();
                default:
                    Fragment fragment = new DummySectionFragment();
                    Bundle args = new Bundle();
                    args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position);
                    fragment.setArguments(args);
                    Log.i("FragmentFactory", "其他三个页面ok");
                    return fragment;
            }
        }
    }

    public static class DummySectionFragment extends Fragment
    {

        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
            View rootView = inflater.inflate(R.layout.section_dummy_fragment, container, false);
            ((TextView) rootView.findViewById(android.R.id.text1)).setText("123");
            return rootView;
        }
    }

}
