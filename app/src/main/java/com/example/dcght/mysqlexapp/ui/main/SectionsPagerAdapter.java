package com.example.dcght.mysqlexapp.ui.main;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.dcght.mysqlexapp.ExFragment.codeFragment;
import com.example.dcght.mysqlexapp.ExFragment.forumfragment;
import com.example.dcght.mysqlexapp.ExFragment.runFragment;
import com.example.dcght.mysqlexapp.ExFragment.taskFragment;
import com.example.dcght.mysqlexapp.Excersice;
import com.example.dcght.mysqlexapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2,R.string.tab_text_3,R.string.tab_text_4};
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final Context mContext;
    Excersice ex;
    public SectionsPagerAdapter(Context context, FragmentManager fm, Excersice ex) {
        super(fm);
        mContext = context;
        this.ex=ex;
        mFragmentList.add(0,taskFragment.newInstance(ex));
        codeFragment codeFr=codeFragment.newInstance(ex);
        runFragment runFr=runFragment.newInstance(ex);
        runFr.codeFragmentDelegate=codeFr;
        mFragmentList.add(1,codeFr);
        mFragmentList.add(2,runFr);
        mFragmentList.add(3, forumfragment.newInstance(ex));

        //mFragmentList.add(1,taskFragment.newInstance(ex));
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);

    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {

        return 4;
    }
}