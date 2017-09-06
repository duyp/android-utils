package com.duyp.androidutils.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phamd on 6/14/2017.
 * Base Recycler view adapter with headers and footers
 */

public abstract class BaseHeaderFooterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 11;
    private static final int TYPE_FOOTER = 12;
    protected static final int TYPE_NON_FOOTER_HEADER = -1;

    private List<View> headers = new ArrayList<>();
    private List<View> footers = new ArrayList<>();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isHeaderOrFooter(viewType)) {
            return HeaderFooterViewHolder.createInstance(parent);
        } else {
            return createHolder(parent, viewType);
        }
    }

    protected abstract RecyclerView.ViewHolder createHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = holder.getItemViewType();
        if (type == TYPE_HEADER) {
            View v = headers.get(position);
            //add our view to a header view and display it
            bindHeaderFooter((HeaderFooterViewHolder) holder, v);
        } else if (type == TYPE_FOOTER) {
            View v = footers.get(position - headers.size() - getItemCountExceptHeaderFooter());
            //add oru view to a footer view and display it
            bindHeaderFooter((HeaderFooterViewHolder) holder, v);
        }
    }

    private void bindHeaderFooter(HeaderFooterViewHolder vh, View view) {
        //empty out our FrameLayout and replace with our header/footer
        if (view != null) {
            vh.base.removeAllViews();
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                    , ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER_HORIZONTAL;
            if (view.getLayoutParams() != null) {
                params.height = view.getLayoutParams().height;
                params.width = view.getLayoutParams().width;
            }
            vh.base.addView(view, params);
        }
    }

    @Override
    public int getItemCount() {
        return headers.size() + footers.size() + getItemCountExceptHeaderFooter();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < headers.size()) {
            return TYPE_HEADER;
        } else if (position >= headers.size() + getItemCountExceptHeaderFooter()) {
            return TYPE_FOOTER;
        }
        return TYPE_NON_FOOTER_HEADER;
    }

    /**
     * @return number items of adapter data except header and footer
     */
    public abstract int getItemCountExceptHeaderFooter();

    public int getRealItemPosition(int adapterPosition) {
        return adapterPosition - headers.size();
    }

    protected boolean isHeaderOrFooter(int itemViewType) {
        return itemViewType == TYPE_HEADER || itemViewType == TYPE_FOOTER;
    }

    //add a header to the adapter
    public void addHeader(View header) {
        if (header != null && !headers.contains(header)) {
            headers.add(header);
            //animate
            notifyItemInserted(headers.size() - 1);
        }
    }

    /**
     * remove a header from the adapter
     */
    public void removeHeader(View header) {
        if (header != null && headers.contains(header)) {
            //animate
            notifyItemRemoved(headers.indexOf(header));
            headers.remove(header);
            if (header.getParent() != null) {
                ((ViewGroup) header.getParent()).removeView(header);
            }
        }
    }

    /**
     * add a footer to the adapter
     * @param footer view to add
     */
    public void addFooter(View footer) {
        if (footer != null && !footers.contains(footer)) {
            footers.add(footer);
            //animate
            notifyItemInserted(headers.size() + getItemCountExceptHeaderFooter() + footers.size() - 1);
        }
    }

    /**
     * remove a footer from the adapter
     * @param footer view to remove
     */
    public void removeFooter(View footer) {
        if (footer != null && footers.contains(footer)) {
            //animate
            notifyItemRemoved(headers.size() + getItemCountExceptHeaderFooter() + footers.indexOf(footer));
            footers.remove(footer);
            if (footer.getParent() != null) {
                ((ViewGroup) footer.getParent()).removeView(footer);
            }
        }
    }

    public List<View> getHeaders() {
        return headers;
    }

    public List<View> getFooters() {
        return footers;
    }

    //our header/footer RecyclerView.ViewHolder is just a FrameLayout
    private static class HeaderFooterViewHolder extends RecyclerView.ViewHolder {

        static HeaderFooterViewHolder createInstance(ViewGroup parent) {
            //create a new framelayout, or inflate from a resource
            FrameLayout frameLayout = new FrameLayout(parent.getContext());
            //make sure it fills the space
            frameLayout.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            );
            return new HeaderFooterViewHolder(frameLayout);
        }

        FrameLayout base;

        HeaderFooterViewHolder(View itemView) {
            super(itemView);
            this.base = (FrameLayout) itemView;
        }
    }
}
