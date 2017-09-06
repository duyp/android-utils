package com.duyp.androidutils.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by phamd on 7/4/2017.
 * Base adapter with list data
 */

public abstract class BaseAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    @NonNull
    private List<T> data;

    public BaseAdapter(@NonNull List<T> data) {
        this.data = data;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(getItemLayoutResource(), parent, false);
        return createViewHolder(v);
    }

    public abstract int getItemLayoutResource();

    public abstract VH createViewHolder(View itemView);

    @Override
    public void onBindViewHolder(VH holder, int position) {
        T item = getItem(position);
        if (item != null) {
            onBindViewHolder(holder, getItem(position));
        }
    }

    protected abstract void onBindViewHolder(VH holder, @NonNull T item);

    public T getItem(int adapterPosition) {
        if (adapterPosition >= 0 && adapterPosition < data.size()) {
            return data.get(adapterPosition);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @NonNull
    public List<T> getData() {
        return data;
    }
}