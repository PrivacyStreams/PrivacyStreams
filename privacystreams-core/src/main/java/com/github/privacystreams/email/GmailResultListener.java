package com.github.privacystreams.email;

import java.util.List;

/**
 * Created by lenovo on 2017/6/4.
 */

interface GmailResultListener {
    void onSuccess();
    void onFail();
    void setList(List<String> list);
    boolean isEmpty();
}
