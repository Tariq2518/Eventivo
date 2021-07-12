package com.raredevz.eventivo.Helper;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.raredevz.eventivo.Admin.Fragments.FragmentManagerAccounts;
import com.raredevz.eventivo.Admin.Fragments.FragmentUserAccounts;

public class ViewPagerAdapterAdminAccounts extends FragmentPagerAdapter {

    public ViewPagerAdapterAdminAccounts(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0)
        {
            fragment = new FragmentUserAccounts();
        }else if (position == 1){
            fragment = new FragmentManagerAccounts();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0)
        {
            title = "User Accounts";
        }
        else if (position == 1)
        {
            title = "Manager Accounts";
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
