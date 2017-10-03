package com.duyp.androidutils.image.glide.loader;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.stream.StreamModelLoader;

import java.io.IOException;
import java.io.InputStream;

public class GlideNetworkDisablingLoader<T> implements StreamModelLoader<T> {

    @Override
    public DataFetcher<InputStream> getResourceFetcher(T model, int width, int height) {
        return new DataFetcher<InputStream>() {
            @Override
            public InputStream loadData(Priority priority) throws Exception {
                throw new IOException("Forced Glide network failure");
            }

            @Override
            public void cleanup() {}

            @Override
            public String getId() {
                return model.toString();
            }

            @Override
            public void cancel() {}
        };
    }
}