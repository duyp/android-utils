package com.duyp.androidutils.functions;

import android.support.annotation.NonNull;

import io.reactivex.functions.Predicate;

/**
 * Created by duypham on 7/31/17.
 * Like {@link Predicate but without exception}
 */

public interface PlainPredicate<T> extends Predicate<T> {

    /**
     * Test the given input value and return a boolean.
     * @param t the value
     * @return the boolean result
     */
    @Override
    boolean test(@NonNull T t);
}
