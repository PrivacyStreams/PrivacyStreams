package io.github.privacystreams.document;

import com.google.api.services.drive.model.File;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.utils.Logging;
import io.github.privacystreams.utils.annotations.PSItem;
import io.github.privacystreams.utils.annotations.PSItemField;

/**
 * drive documents output
 */

@PSItem
public class DriveDocument extends Item {
    /*
    the string id of the drive document
     */
    @PSItemField(type = String.class)
    public static final String ID = "id";
    /*
    the title of the drive document
     */
    @PSItemField(type = String.class)
    public static final String TITLE = "title";
    /*
    the created time in long of the drive document
     */
    @PSItemField(type = Long.class)
    public static final String CREATED_DATE = "created date";
    /*
    the modified time in long of the drive document
     */
    @PSItemField(type = Long.class)
    public static final String MODIFIED_DATE = "modified date";
    /*
    the description of the drive document
     */
    @PSItemField(type = String.class)
    public static final String DESCRIPTION = "description";
    /*
    the file size of the drive document
     */
    @PSItemField(type = Long.class)
    public static final String FILE_SIZE = "file size";

    DriveDocument(File file) {
        if (file == null) {
            Logging.error("file is null");
        }
        this.setFieldValue(ID, file.getId());
        this.setFieldValue(TITLE, file.getName());
        if (file.getCreatedTime() != null)
            this.setFieldValue(CREATED_DATE, file.getCreatedTime().getValue());
        if (file.getModifiedTime() != null)
            this.setFieldValue(MODIFIED_DATE, file.getModifiedTime().getValue());
        this.setFieldValue(DESCRIPTION, file.getDescription());
        this.setFieldValue(FILE_SIZE, file.getSize());
    }

    public static DriveListProvider asDocumentList
            (long beginTime, long endTime, int maxResultNum, int resultNum) {
        return new DriveListProvider(beginTime, endTime, maxResultNum, resultNum);
    }
}
