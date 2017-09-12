package com.duyp.androidutils.navigator;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

/* Copyright 2017 Patrick Löwenstein
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. */
public class ChildFragmentNavigator extends ActivityNavigator implements FragmentNavigator {

    private final Fragment fragment;

    public ChildFragmentNavigator(Fragment fragment) {
        super(fragment.getActivity());
        this.fragment = fragment;
    }

    @Override
    public void replaceChildFragment(@IdRes int containerId, @NonNull Fragment fragment, View... transitionViews) {
        replaceFragmentInternal(this.fragment.getChildFragmentManager(), containerId, fragment, null, null, false, null);
    }

    @Override
    public final void replaceChildFragment(@IdRes int containerId, @NonNull Fragment fragment, Bundle args, View... transitionViews) {
        replaceFragmentInternal(this.fragment.getChildFragmentManager(), containerId, fragment, null, args, false, null);
    }

    @Override
    public final void replaceChildFragment(@IdRes int containerId, @NonNull Fragment fragment, @NonNull String fragmentTag, Bundle args, View... transitionViews) {
        replaceFragmentInternal(this.fragment.getChildFragmentManager(), containerId, fragment, fragmentTag, args, false, null);
    }

    @Override
    public final void replaceChildFragmentAndAddToBackStack(@IdRes int containerId, @NonNull Fragment fragment, Bundle args, String backstackTag, View... transitionViews) {
        replaceFragmentInternal(this.fragment.getChildFragmentManager(), containerId, fragment, null, args, true, backstackTag);
    }

    @Override
    public final void replaceChildFragmentAndAddToBackStack(@IdRes int containerId, @NonNull Fragment fragment, @NonNull String fragmentTag, Bundle args, String backstackTag, View... transitionViews) {
        replaceFragmentInternal(this.fragment.getChildFragmentManager(), containerId, fragment, fragmentTag, args, true, backstackTag);
    }

    @Override
    @Nullable
    public <T extends Fragment> T findChildFragmentByTag(@NonNull String tag) {
        // noinspection unchecked
        return (T) this.fragment.getChildFragmentManager().findFragmentByTag(tag);
    }

    @Override
    @Nullable
    public <T extends Fragment> T findChildFragmentById(@IdRes int containerId) {
        // noinspection unchecked
        return (T) this.fragment.getChildFragmentManager().findFragmentById(containerId);
    }
}
