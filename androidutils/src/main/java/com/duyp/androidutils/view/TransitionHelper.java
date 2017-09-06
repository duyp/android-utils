package com.duyp.androidutils.view;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Duy Pham on 24/05/2017.
 * Helper class for creating content transitions used with {@link android.app.ActivityOptions}.
 */
public class TransitionHelper {

    /**
     * Create the transition participants required during a activity transition while
     * avoiding glitches with the system UI.
     *
     * @param activity The activity used as start for the transition.
     * @param includeStatusBar If false, the status bar will not be added as the transition
     *        participant.
     * @return All transition participants.
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static Pair<View, String>[] createSafeTransitionParticipants(@NonNull Activity activity,
                                                                        boolean includeStatusBar,
                                                                        @Nullable Pair... otherParticipants) {
        // Avoid system UI glitches as described here:
        // https://plus.google.com/+AlexLockwood/posts/RPtwZ5nNebb
        View decor = activity.getWindow().getDecorView();
        View statusBar = null;
        if (includeStatusBar) {
            statusBar = decor.findViewById(android.R.id.statusBarBackground);
        }
        View navBar = decor.findViewById(android.R.id.navigationBarBackground);

        // Create pair of transition participants.
        List<Pair> participants = new ArrayList<>(3);
        addNonNullViewToTransitionParticipants(statusBar, participants);
        addNonNullViewToTransitionParticipants(navBar, participants);
        // only add transition participants if there's at least one none-null element
        if (otherParticipants != null && !(otherParticipants.length == 1
                && otherParticipants[0] == null)) {
            participants.addAll(Arrays.asList(otherParticipants));
        }
        // noinspection unchecked
        return participants.toArray(new Pair[participants.size()]);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private static void addNonNullViewToTransitionParticipants(View view, List<Pair> participants) {
        if (view == null) {
            return;
        }
        participants.add(new Pair<>(view, view.getTransitionName()));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    public static Pair[] getPairsFromViews(boolean includeNestedChildren, View... views) {
        if (views != null) {
            Log.d("transition", "Creating pairs for " + views.length + " views (include nested children = " + includeNestedChildren + ")");
            List<Pair> pairs = new ArrayList<>();
            for (View v : views) {
                if (!includeNestedChildren) {
                    if (v != null && v.getTransitionName() != null && !(v.getVisibility() == View.GONE)) {
                        Log.d("transition", v.getTransitionName());
                        // noinspection unchecked
                        pairs.add(new Pair(v, v.getTransitionName()));
                    }
                } else {
                    List<View> children = getAllChildren(v);
                    for (View child : children) {
                        if (child != null && child.getTransitionName() != null
                                && !(child.getVisibility() == View.GONE)) {
                            Log.d("transition", child.getTransitionName());
                            // noinspection unchecked
                            pairs.add(new Pair(child, child.getTransitionName()));
                        }
                    }
                }
            }
            if (pairs.size() > 0) {
                return pairs.toArray(new Pair[pairs.size()]);
            }
            return null;
        }
        return null;
    }

    /**
     * Get all view hierarchy of input view
     * @param v input view
     * @return list of nested view
     */
    private static List<View> getAllChildren(View v) {
        List<View> visited = new ArrayList<>();
        List<View> unvisited = new ArrayList<>();
        unvisited.add(v);

        while (!unvisited.isEmpty()) {
            View child = unvisited.remove(0);
            visited.add(child);
            if (!(child instanceof ViewGroup)) continue;
            ViewGroup group = (ViewGroup) child;
            final int childCount = group.getChildCount();
            for (int i=0; i<childCount; i++) unvisited.add(group.getChildAt(i));
        }

        return visited;
    }
}
