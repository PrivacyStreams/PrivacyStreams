package io.github.privacystreams.communication.email;

import com.google.api.services.gmail.Gmail;

interface GmailResultListener {
    void onSuccess(Gmail service);

    void onFail();
}
