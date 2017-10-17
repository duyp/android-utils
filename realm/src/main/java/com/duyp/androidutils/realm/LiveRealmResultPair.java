package com.duyp.androidutils.realm;

import io.realm.OrderedCollectionChangeSet;
import io.realm.RealmModel;
import io.realm.RealmResults;

public class LiveRealmResultPair<T extends RealmModel> {
        private RealmResults<T> data;
        private OrderedCollectionChangeSet changeSet;

        public LiveRealmResultPair(RealmResults<T> data, OrderedCollectionChangeSet set) {
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