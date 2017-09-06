package com.duyp.androidutils.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

/**
 * Created by duypham on 7/7/17.
 *
 */

public abstract class BaseFragmentStatePagerAdapter<T extends Fragment> extends FragmentStatePagerAdapter {

    private SparseArray<T> mPageReferences = new SparseArray<>();

    public BaseFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public abstract T createFragment(int position);

    @Override
    public Fragment getItem(int position) {
        T fragment = createFragment(position);
        mPageReferences.append(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        mPageReferences.remove(position);
    }

    public T getFragment(int position) {
        return mPageReferences.get(position);
    }
}