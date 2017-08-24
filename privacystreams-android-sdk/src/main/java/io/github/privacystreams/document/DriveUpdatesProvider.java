package io.github.privacystreams.document;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.events.ChangeListener;

//TODO: to be completed
public class DriveUpdatesProvider {
    GoogleApiClient mGoogleApiClient;
    ChangeListener changeListener;
    public DriveUpdatesProvider(){

    }
//@Override
    public void provide(){
//    super.provide();
//    mGoogleApiClient = new GoogleApiClient.Builder(getContext())
//            .enableAutoManage(this, )
//            .addApi(Drive.API)
//            .addScope(Drive.SCOPE_FILE)
//            .setAccountName(accountName)
//            .build();
//        DriveFolder folder = Drive.DriveApi.getRootFolder(mGoogleApiClient).getDriveId().asDriveFolder();
//        folder.addChangeListener(mGoogleApiClient, changeListener);
    }

//    @Override
//    private java.util.List<String> getDataFromApi() throws Exception{
//
//    }
}