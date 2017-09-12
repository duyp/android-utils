package com.duyp.androidutils.navigator;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Pair;
import android.view.View;

import com.duyp.androidutils.functions.PlainConsumer;
import com.duyp.androidutils.view.TransitionHelper;

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
 * limitations under the License.
 *
 *
 * ------
 *
 * FILE CHANGED 2017 Tailored Media GmbH
 *
 */
public class ActivityNavigator implements Navigator {

    protected final FragmentActivity activity;

    public ActivityNavigator(FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public void finishActivity() {
        activity.finish();
    }

    @Override
    public final void startActivity(@NonNull Intent intent) {
        activity.startActivity(intent);
    }

    @Override
    public final void startActivity(@NonNull String action) {
        activity.startActivity(new Intent(action));
    }

    @Override
    public final void startActivity(@NonNull String action, @NonNull Uri uri) {
        activity.startActivity(new Intent(action, uri));
    }

    @Override
    public final void startActivity(@NonNull Class<? extends Activity> activityClass) {
        startActivityInternal(activityClass, null, null);
    }

    @Override
    public void startActivity(@NonNull Class<? extends Activity> activityClass, @NonNull PlainConsumer<Intent> setArgsAction) {
        startActivityInternal(activityClass, setArgsAction, null);
    }

    @Override
    public void startActivity(@NonNull Class<? extends Activity> activityClass, Bundle args) {
        startActivityInternal(activityClass, intent -> intent.putExtra(EXTRA_ARG, args), null);
    }

    @Override
    public final void startActivity(@NonNull Class<? extends Activity> activityClass, @NonNull Parcelable arg) {
        startActivityInternal(activityClass, intent -> intent.putExtra(EXTRA_ARG, arg), null);
    }

    @Override
    public final void startActivity(@NonNull Class<? extends Activity> activityClass, @NonNull String arg) {
        startActivityInternal(activityClass, intent -> intent.putExtra(EXTRA_ARG, arg), null);
    }

    @Override
    public final void startActivity(@NonNull Class<? extends Activity> activityClass, int arg) {
        startActivityInternal(activityClass, intent -> intent.putExtra(EXTRA_ARG, arg), null);
    }

    @Override
    public final void startActivityForResult(@NonNull Class<? extends Activity> activityClass, int requestCode) {
        startActivityInternal(activityClass, null, requestCode);
    }

    @Override
    public void startActivityForResult(@NonNull Class<? extends Activity> activityClass, @NonNull PlainConsumer<Intent> setArgsAction, int requestCode) {
        startActivityInternal(activityClass, setArgsAction, requestCode);
    }

    @Override
    public final void startActivityForResult(@NonNull Class<? extends Activity> activityClass, @NonNull Parcelable arg, int requestCode) {
        startActivityInternal(activityClass, intent -> intent.putExtra(EXTRA_ARG, arg), requestCode);
    }

    @Override
    public final void startActivityForResult(@NonNull Class<? extends Activity> activityClass, @NonNull String arg, int requestCode) {
        startActivityInternal(activityClass, intent -> intent.putExtra(EXTRA_ARG, arg), requestCode);
    }

    @Override
    public final void startActivityForResult(@NonNull Class<? extends Activity> activityClass, int arg, int requestCode) {
        startActivityInternal(activityClass, intent -> intent.putExtra(EXTRA_ARG, arg), requestCode);
    }

    private void startActivityInternal(Class<? extends Activity> activityClass, PlainConsumer<Intent> setArgsAction, Integer requestCode) {
        Intent intent = new Intent(activity, activityClass);
        if(setArgsAction != null) { setArgsAction.accept(intent); }

        if(requestCode != null) {
            activity.startActivityForResult(intent, requestCode);
        } else {
            activity.startActivity(intent);
        }
    }

    @Override
    public void replaceFragment(@IdRes int containerId, @NonNull Fragment fragment, View... transitionViews) {
        replaceFragmentInternal(activity.getSupportFragmentManager(), containerId, fragment, null, null, false, null, transitionViews);
    }

    @Override
    public final void replaceFragment(@IdRes int containerId,@NonNull Fragment fragment, Bundle args, View... transitionViews) {
        replaceFragmentInternal(activity.getSupportFragmentManager(), containerId, fragment, null, args, false, null, transitionViews);
    }

    @Override
    public final void replaceFragment(@IdRes int containerId, @NonNull Fragment fragment, @NonNull String fragmentTag, Bundle args, View... transitionViews) {
        replaceFragmentInternal(activity.getSupportFragmentManager(), containerId, fragment, fragmentTag, args, false, null, transitionViews);
    }

    @Override
    public final void replaceFragmentAndAddToBackStack(@IdRes int containerId,@NonNull Fragment fragment, Bundle args, String backstackTag, View... transitionViews) {
        replaceFragmentInternal(activity.getSupportFragmentManager(), containerId, fragment, null, args, true, backstackTag, transitionViews);
    }

    @Override
    public final void replaceFragmentAndAddToBackStack(@IdRes int containerId, @NonNull Fragment fragment, @NonNull String fragmentTag, Bundle args, String backstackTag, View... transitionViews) {
        replaceFragmentInternal(activity.getSupportFragmentManager(), containerId, fragment, fragmentTag, args, true, backstackTag, transitionViews);
    }

    @Override
    @Nullable
    public <T extends Fragment> T findFragmentByTag(@NonNull String tag) {
        // noinspection unchecked
        return (T) activity.getSupportFragmentManager().findFragmentByTag(tag);
    }

    @Override
    @Nullable
    public <T extends Fragment> T findFragmentById(@IdRes int containerId) {
        // noinspection unchecked
        return (T)activity.getSupportFragmentManager().findFragmentById(containerId);
    }

    protected final void replaceFragmentInternal(FragmentManager fm, @IdRes int containerId, Fragment fragment, String fragmentTag, Bundle args, boolean addToBackstack,  String backstackTag, View... transitionViews) {
        if(args != null) { fragment.setArguments(args);}
        FragmentTransaction ft = fm.beginTransaction();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            for (View view : transitionViews) {
                if (view != null && view.getTransitionName() != null) {
                    ft.addSharedElement(view, view.getTransitionName());
                }
            }
        }
        ft.replace(containerId, fragment, fragmentTag);
        if(addToBackstack) {
            ft.addToBackStack(backstackTag).commit();
            fm.executePendingTransactions();
        } else {
            ft.commitNow();
        }
    }

    /**
     * Start an activity with shared element transition
     * @param activityClass target activity class
     * @param consumer consumer accepting an intent
     * @param includeNestedChildren find all nested children of any input transition views
     * @param includeStatusBar make transition for status bar
     * @param views list of view used for shared element transition, each view must have transition name
     *              same as destination view on target activity
     */
    public void startActivityWithTransition(@NonNull Class<? extends Activity> activityClass,
                                            @NonNull PlainConsumer<Intent> consumer,
                                            boolean includeNestedChildren, boolean includeStatusBar, View... views) {
        Intent intent = new Intent(activity, activityClass);
        consumer.accept(intent);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP
                && views != null && views.length > 0) {
            try {
                Pair[] pairs = TransitionHelper.getPairsFromViews(includeNestedChildren, views);
                Pair[] fullPairs = TransitionHelper.createSafeTransitionParticipants(activity, includeStatusBar, pairs);
                // noinspection unchecked
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(activity, fullPairs);
                activity.startActivity(intent, options.toBundle());
            } catch (Exception e){
                activity.startActivity(intent);
            }
        } else {
            activity.startActivity(intent);
        }
    }
}
