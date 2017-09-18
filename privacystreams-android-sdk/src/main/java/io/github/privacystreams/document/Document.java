package io.github.privacystreams.document;

import java.io.File;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.annotations.PSItemField;


public class Document extends Item {
    @PSItemField(type = String.class)
    public static final String NAME = "name";

    @PSItemField(type = String.class)
    public static final String PATH = "path";

    @PSItemField(type = Long.class)
    public static final String LENGTH = "length";

    @PSItemField(type = Long.class)
    public static final String LAST_MODIFIED = "last_modified";

    @PSItemField(type = Boolean.class)
    public static final String CAN_WRITE = "can_write";

    @PSItemField(type = Boolean.class)
    public static final String IS_HIDDEN = "is_hidden";

    Document(File file){
        this.setFieldValue(NAME,file.getName());
        this.setFieldValue(PATH,file.getAbsolutePath());
        this.setFieldValue(LENGTH,file.length());
        this.setFieldValue(LAST_MODIFIED,file.lastModified());
        this.setFieldValue(CAN_WRITE,file.canWrite());
        this.setFieldValue(IS_HIDDEN,file.isHidden());
    }

    public static PStreamProvider asUpdates(){
        return new LocalDocumentUpdateProvider();
    }
}
