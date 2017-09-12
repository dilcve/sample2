package br.com.rf.dropchallenge.beers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import br.com.rf.dropchallenge.model.Beer;

public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return BeerFragment.newInstance(Beer.TYPE_LIGHT);
            case 1:
                return BeerFragment.newInstance(Beer.TYPE_MEDIUM);
            case 2:
                return BeerFragment.newInstance(Beer.TYPE_STRONG);
            case 3:
                return BeerFragment.newInstance(Beer.TYPE_RANDOM);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "LIGHT";
            case 1:
                return "MEDIUM";
            case 2:
                return "STRONG";
            case 3:
                return "RANDOM";
        }
        return null;
    }

}
