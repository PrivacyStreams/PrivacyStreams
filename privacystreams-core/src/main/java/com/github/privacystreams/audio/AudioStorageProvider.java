package com.github.privacystreams.audio;

import android.Manifest;
import android.database.Cursor;
import android.provider.MediaStore;

import com.github.privacystreams.core.providers.MStreamProvider;

import java.io.File;

/**
 * Provide a stream of images stored in local sd card.
 */

class AudioStorageProvider extends MStreamProvider {

    AudioStorageProvider(){
        this.addRequiredPermissions(Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @Override
    protected void provide() {
        this.getAudioInfo();
        this.finish();

    }

    private void getAudioInfo(){

        final String track_id = MediaStore.Audio.Media._ID;
        final String track_no = MediaStore.Audio.Media.TRACK;
        final String track_name = MediaStore.Audio.Media.TITLE;
        final String artist = MediaStore.Audio.Media.ARTIST;
        final String duration = MediaStore.Audio.Media.DURATION;
        final String album = MediaStore.Audio.Media.ALBUM;
        final String composer = MediaStore.Audio.Media.COMPOSER;
        final String year = MediaStore.Audio.Media.YEAR;
        final String path = MediaStore.Audio.Media.DATA;
        final String date_added = MediaStore.Audio.Media.DATE_ADDED;

        Cursor c = this.getContext().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{track_id, track_no, artist, track_name, album, duration, path, year, composer, date_added},
                null, null, null
        );

        if (c != null && c.moveToFirst()) {
            do {
                // Get the field values
                Long dateAdded = c.getLong(c.getColumnIndex(date_added));
                String fileName = c.getString(c.getColumnIndex(path));
                AudioData audioData = AudioData.newLocalAudio(new File(fileName));
                Audio audioItem = new Audio(dateAdded, audioData);
                this.output(audioItem);
            } while (c.moveToNext());

            c.close();
        }
    }

}

