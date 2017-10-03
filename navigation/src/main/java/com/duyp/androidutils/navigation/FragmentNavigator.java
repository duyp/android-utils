package com.duyp.androidutils.navigation;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

/* Copyright 2016 Patrick Löwenstein
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
public interface FragmentNavigator extends Navigator {

    void replaceChildFragment(@IdRes int containerId, @NonNull Fragment fragment, View... transitionViews);
    void replaceChildFragment(@IdRes int containerId, @NonNull Fragment fragment, Bundle args, View... transitionViews);
    void replaceChildFragment(@IdRes int containerId, @NonNull Fragment fragment, @NonNull String fragmentTag, Bundle args, View... transitionViews);
    void replaceChildFragmentAndAddToBackStack(@IdRes int containerId, @NonNull Fragment fragment, Bundle args, String backstackTag, View... transitionViews);
    void replaceChildFragmentAndAddToBackStack(@IdRes int containerId, @NonNull Fragment fragment, @NonNull String fragmentTag, Bundle args, String backstackTag, View... transitionViews);

    @Nullable
    <T extends Fragment> T findChildFragmentByTag(@NonNull String tag);

    @Nullable
    <T extends Fragment> T findChildFragmentById(@IdRes int containerId);
}