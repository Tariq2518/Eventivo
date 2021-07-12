package com.raredevz.eventivo.Helper;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.raredevz.eventivo.Manager.Fragments.AllPaymentsFragment;
import com.raredevz.eventivo.Manager.Fragments.ApprovedPaymentsFragment;
import com.raredevz.eventivo.Manager.Fragments.DeclinedPaymentsFragment;
import com.raredevz.eventivo.Manager.Fragments.PendingPaymentsFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new AllPaymentsFragment();
        }
        else if (position == 1)
        {
            fragment = new PendingPaymentsFragment();
        }
        else if (position == 2)
        {
            fragment = new ApprovedPaymentsFragment();
        }
        else if (position == 3)
        {
            fragment = new DeclinedPaymentsFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "All";
        }
        else if (position == 1)
        {
            title = "Pending";
        }
        else if (position == 2)
        {
            title = "Approved";
        }else if (position == 3)
        {
            title = "Declined";
        }
        return title;
    }
}
