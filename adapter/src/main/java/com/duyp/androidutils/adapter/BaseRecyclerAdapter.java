package com.duyp.androidutils.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.List;

/**
 * Created by phamd on 6/16/2017.
 * Base recycler view adapter with list data of single view type
 */

public abstract class BaseRecyclerAdapter<T> extends BaseHeaderFooterAdapter {

    protected static final int TYPE_ITEM = 333;

    @NonNull
    private List<T> mAdapterData;

    protected final LayoutInflater mInflater;

    protected final Context context;

    public BaseRecyclerAdapter(@NonNull Context context, @NonNull List<T> data) {
        mAdapterData = data;
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setNewData(@NonNull List<T> newData) {
        mAdapterData = newData;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //check what type of view our position is
        if (isHeaderOrFooter(holder.getItemViewType())) {
            super.onBindViewHolder(holder, position);
        } else {
            //it's one of our items, display as required
            T item = getItem(position);
            if (item != null) {
                bindHolder(holder, item);
            }
        }
    }

    protected abstract void bindHolder(RecyclerView.ViewHolder viewHolder, @NonNull T item);

    /**
     * @param adapterPosition adapter position
     * @return Data at input position
     */
    @Nullable
    protected T getItem(int adapterPosition) {
        int realPosition = getRealItemPosition(adapterPosition);
        if (realPosition >= 0 && realPosition < mAdapterData.size()) {
            return mAdapterData.get(realPosition);
        }
        return null;
    }

    protected void removeItem(int adapterPosition) {
        int realPosition = getRealItemPosition(adapterPosition);
        if (realPosition >= 0 && realPosition < mAdapterData.size()) {
            mAdapterData.remove(realPosition);
            notifyItemRemoved(adapterPosition);
        }
    }

    protected void removeItem(T item) {
        mAdapterData.remove(item);
        notifyDataSetChanged();
    }

    protected void addItem(T item) {
        addItem(item, true);
    }

    protected void addItem(T item, boolean notify) {
        mAdapterData.add(item);
        if (notify) {
            notifyDataSetChanged();
        }
    }

    public List<T> getData() {
        return mAdapterData;
    }

    @Override
    public int getItemCountExceptHeaderFooter() {
        return mAdapterData.size();
    }

    @Override
    public int getItemViewType(int position) {
        int type = super.getItemViewType(position);
        return type == TYPE_NON_FOOTER_HEADER ? TYPE_ITEM : type;
    }
}
