package com.github.privacystreams.document;

import com.github.privacystreams.core.Item;
import com.github.privacystreams.utils.annotations.PSItem;
import com.github.privacystreams.utils.annotations.PSItemField;
import com.google.api.services.drive.model.File;

/**
 * drive documents output
 */
@PSItem
public class DriveDocument extends Item{
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

    DriveDocument(File file){
        this.setFieldValue(ID, file.getId());
        this.setFieldValue(TITLE, file.getName());
        this.setFieldValue(CREATED_DATE, file.getCreatedTime().getValue());
        this.setFieldValue(MODIFIED_DATE, file.getModifiedTime().getValue());
        this.setFieldValue(DESCRIPTION, file.getDescription());
        this.setFieldValue(FILE_SIZE, file.getSize());
    }

    public static DriveListProvider testDriveList
            (long beginTime, long endTime, int maxResultNum, int resultNum){
        return new DriveListProvider(beginTime, endTime, maxResultNum, resultNum);
    }
}

