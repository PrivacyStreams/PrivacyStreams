package io.github.privacystreams.communication.emailinfo;


public interface EmailAccountNameListener {
    void onSuccess(String name);
    void onFail();
}
