package com.duyp.androidutils.realm;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Pair;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmModel;
import io.realm.RealmResults;

/**
 * Created by duypham on 9/18/17.
 * Mix version of android {@link LiveData} for {@link RealmResults}
 */

public class LiveRealmResults<T extends RealmModel> extends LiveData<LiveRealmResults.LiveDataPair> {

    private RealmResults<T> mRealmResults;

    private final OrderedRealmCollectionChangeListener<RealmResults<T>> listener = (realmResults, changeSet) -> {
        this.updateValue(new LiveDataPair(realmResults, changeSet));
    };

    public LiveRealmResults(@NonNull RealmResults<T> realmResults) {
        updateValue(new LiveDataPair(realmResults, null));
    }

    public RealmResults<T> getData() {
        return mRealmResults;
    }

    @Override
    protected void onActive() {
        super.onActive();
        mRealmResults.addChangeListener(listener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        mRealmResults.removeChangeListener(listener);
    }


    protected void updateValue(LiveDataPair value) {
        try {
            this.setValue(value);
        } catch (Exception e) {
            // if we can't set value (since current thread is a background thread), we must call postValue() instead
            // java.lang.IllegalStateException: Cannot invoke setValue on a background thread
            // or NullPointerException if we are testing (can't get current looper when assert is main thread)
            this.postValue(value);
        }
    }

    @Override
    protected void setValue(LiveDataPair value) {
        super.setValue(value);
        mRealmResults = value.getData();
    }

    @Override
    protected void postValue(LiveDataPair value) {
        super.postValue(value);
        mRealmResults = value.getData();
    }

    /**
     * Convert {@link RealmResults} to Live data {@link LiveRealmResults}
     * @param realmResults input realm results
     * @param <T> type of model
     * @return live data version of given realm results
     */
    public static <T extends RealmModel> LiveRealmResults<T> asLiveData(RealmResults<T> realmResults){
        return new LiveRealmResults<T>(realmResults);
    }

    public class LiveDataPair {
        private RealmResults<T> data;
        private OrderedCollectionChangeSet changeSet;

        public LiveDataPair(RealmResults<T> data, OrderedCollectionChangeSet set) {
            this.data = data;
            this.changeSet = set;
        }

        public RealmResults<T> getData() {
            return data;
        }

        public OrderedCollectionChangeSet getChangeSet() {
            return changeSet;
        }
    }
}