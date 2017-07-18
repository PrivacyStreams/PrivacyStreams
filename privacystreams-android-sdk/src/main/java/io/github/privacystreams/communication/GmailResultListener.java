package io.github.privacystreams.communication;

import com.google.api.services.gmail.Gmail;

interface GmailResultListener {
    void onSuccess(Gmail service);
    void onFail();
}
