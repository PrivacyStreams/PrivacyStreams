package io.github.privacystreams.utils;

import android.content.Context;
import androidx.core.content.FileProvider;

/**
 * A file provider for exposing file to uri.
 */

public class PSFileProvider extends FileProvider {
    /**
     * Get the provider of the external file path.
     *
     * @param context context.
     * @return provider.
     */
    public static String getProviderName(Context context) {
        return context.getPackageName() + ".provider";
    }
}
