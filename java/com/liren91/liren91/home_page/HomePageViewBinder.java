package com.liren91.liren91.home_page;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.liren91.liren91.R;

/**
 * Created by smallQ on 15-4-4.
 * 这个类顾名思义
 */
public class HomePageViewBinder implements SimpleAdapter.ViewBinder
{

    Activity activity;

    public HomePageViewBinder(Activity activity)
    {
        this.activity = activity;
    }


    @Override
    public boolean setViewValue(View view, Object data, String textRepresentation)
    {
        int id = view.getId();
        switch (id)
        {
            case R.id.avatar:
                byte[] bytes = (byte[]) data;
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ((ImageView) view).setImageBitmap(bitmap);
                break;
            case R.id.job_name:
                ((TextView) view).setText(((String[]) data)[0]);
                view.setTag(((String[]) data)[1]);//设置一个tag存储链接给OnItemClick()用
                break;
            case R.id.require:
                String text = (String) data;
                SpannableStringBuilder builder = new SpannableStringBuilder(text);
                ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.parseColor("#e74c3c"));
                int start, end;
                start = text.indexOf("|") + 1;
                end = text.indexOf("|", start);
                builder.setSpan(redSpan, start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                ((TextView) view).setText(builder);
                break;
            case R.id.layout_welfare:
                String[] welfares = (String[]) data;
                LinearLayout container = (LinearLayout) view;
//                float size=((TextView)(activity.findViewById(R.id.textView5))).getTextSize();
                container.removeAllViews();
                int n = welfares.length;
                for (int i = 0; i < n; i++)
                {
                    LinearLayout layout = new LinearLayout(activity);
                    layout.setBackgroundColor(Color.parseColor("#cccccc"));
                    layout.setPaddingRelative(1, 1, 1, 1);
                    LinearLayout.LayoutParams ml = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    ml.setMargins(0, 0, 4, 2);
                    layout.setLayoutParams(ml);

                    TextView textView = new TextView(activity);
//                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    textView.setBackgroundColor(Color.WHITE);
                    textView.setPaddingRelative(4, 0, 4, 0);
                    textView.setText(welfares[i]);
//                    textView.setTextSize(size);
                    layout.addView(textView);
                    container.addView(layout);
                }
                break;
            default:
                ((TextView) view).setText((String) data);
        }
        return true;
    }
}
