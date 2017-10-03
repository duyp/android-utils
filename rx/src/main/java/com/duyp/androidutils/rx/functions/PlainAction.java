package com.duyp.androidutils.rx.functions;

import io.reactivex.functions.Action;

/**
 * Created by duypham on 7/26/17.
 * Like {@link Action} but without Exception
 */

public interface PlainAction extends Action {

    /**
     * Run the action
     */
    @Override
    void run();
}
