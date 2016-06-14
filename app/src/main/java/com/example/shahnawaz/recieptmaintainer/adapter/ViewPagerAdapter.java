package com.example.shahnawaz.recieptmaintainer.adapter;

import android.os.Bundle;
import android.support.v4.app.BundleCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Shahnawaz on 6/14/2016.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private PagerFragment mDebit = new PagerFragment(), mCredti = new PagerFragment();

    @Override
    public Fragment getItem(int position) {
        PagerFragment fragment = position == 0 ? mCredti : mDebit;
        Bundle args = new Bundle();
        args.putInt(PagerFragment.KEY_TYPE, position == 0 ? 0 : 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return position == 0 ? "Credit" : "Paid";
    }

    @Override
    public int getCount() {
        return 2;
    }

    public void refresh() {
        mDebit.refresh();
        mCredti.refresh();
    }
}
