package io.github.privacystreams.audio;

import android.Manifest;
import android.media.MediaRecorder;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.util.Log;

import java.util.LinkedList;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.core.exceptions.PSException;
import io.github.privacystreams.core.PStreamProvider;
import io.github.privacystreams.utils.Globals;
import io.github.privacystreams.utils.StorageUtils;
import io.github.privacystreams.utils.TimeUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Record audio for a duration from microphone
 */

class AudioRecorder extends PStreamProvider {
    private final Long duration;
    private static final int HW_SUPPORTED_SAMPLE_RATE = 44100;
    public static boolean isFinished = false;
    public static boolean isStarted = false;
    public static List<Short> Data;

    AudioRecorder(long duration) {
        this.duration = duration;
        this.addParameters(duration);
        this.addRequiredPermissions(Manifest.permission.RECORD_AUDIO);
    }

    @Override
    protected void provide() {
        Audio audioItem = null;
        try {
            audioItem = recordAudio(this.getUQI(), this.duration);
            this.output(audioItem);
        } catch (IOException e) {
            e.printStackTrace();
            this.raiseException(this.getUQI(), PSException.INTERRUPTED("AudioRecorder failed."));
        } catch (RuntimeException e) {
            e.printStackTrace();
            this.raiseException(this.getUQI(), PSException.INTERRUPTED("AudioRecorder failed. Perhaps the audio duration is too short."));
        }
        this.finish();
    }

    private static void runAfterDuration(final long durationInMillis,
                                  final Runnable callback) {
        (new Thread() {
            public void run() {
                try {
                    Thread.sleep(durationInMillis);
                }
                catch(InterruptedException ie) {
                    ie.printStackTrace();
                }

                callback.run();
            }
        }).start();
    }

    static Audio recordAudio(UQI uqi, long duration) throws IOException {
        long startTime = System.currentTimeMillis();

        final int bufferSize = AudioRecord.getMinBufferSize(HW_SUPPORTED_SAMPLE_RATE,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

        if(bufferSize > 0) {
            Log.d("Debug", "The min buffer size is " + bufferSize + " bytes");
            final AudioRecord recorder = createPcm8BitStereoWithRate(HW_SUPPORTED_SAMPLE_RATE, bufferSize);
            boolean recorderStateIsValid = ((recorder != null) &&
                    (recorder.getState() == AudioRecord.STATE_INITIALIZED));

            final List<Short> databuffer = new ArrayList<>();

            if (recorderStateIsValid) {

                recorder.startRecording();
                Log.d("Debug", "Recording has begun");

                readWhileRecording(recorder, bufferSize, databuffer);
                runAfterDuration(duration, new Runnable() {
                    public void run() {
                        recorder.stop();
                        recorder.release();
                        Data = databuffer;

                        isFinished = true;

                        Log.d("Debug", "Recording has completed");
                        Log.d("Debug", "Read in a total of " + (databuffer.size()) + " bytes");
                    }

                });
            } else {
                Log.d("Debug", "Recorder did not initialize correctly. Has the permission been granted?");
            }
        }
        else {
            Log.d("Debug", getMinBufferSizeErrorMessage(bufferSize));
        }


        while (!isFinished){}


        AudioData audioData = AudioData.newTempRecord(Data);

        return new Audio(startTime, audioData);
//

    }

    private static AudioRecord createPcm8BitStereoWithRate(final int sampleRate,
                                                    final int bufferSizeInBytes) {
        try {
            return new AudioRecord(MediaRecorder.AudioSource.MIC,
                    sampleRate,
                    AudioFormat.CHANNEL_IN_MONO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    bufferSizeInBytes);
        } catch (IllegalArgumentException illegalArgument) {
            Log.d("Debug", illegalArgument.getMessage());
        }

        return null;
    }
    private static void readWhileRecording(final AudioRecord recordingDevice,
                                    final int bufferSize, final List<Short> databuffer) {
        (new Thread() {
            public void run() {

                while(recordingDevice.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING) {
                    short[] buffer = new short[bufferSize];

                    int bytesRead = recordingDevice.read(buffer, 0, bufferSize, AudioRecord.READ_BLOCKING);

                    if(bytesRead > 0) {
                        Log.d("Debug", "Read in " + bytesRead + " bytes");
                        int count = 0;
                        for(final short elem : buffer) {
                            if (count<bytesRead){
                                if (elem!=0 && isStarted==false){
                                    isStarted = true;
                                }
                                if (isStarted){
                                    databuffer.add(elem);
                                    count += 1;
                                }


                            } else{
                            break;}
                        }

                    }
                    else {
                        Log.d("Debug", getReadErrorMessage(bytesRead));
                    }

                }


                Log.d("Debug", "Mic is no longer recording");
            }
        }).start();

    }

    private static String getMinBufferSizeErrorMessage(final int errorCode) {
        switch(errorCode) {
            case AudioRecord.ERROR_BAD_VALUE: {
                return "Recording parameters not supported by the hardware, or an invalid parameter was passed";
            }
            case AudioRecord.ERROR: {
                return "Implementation was unable to query the hardware for input properties";
            }
            default: {
                return "Unknown error";
            }
        }
    }

    private static String getReadErrorMessage(final int errorCode) {
        switch(errorCode) {
            case AudioRecord.ERROR_INVALID_OPERATION: {
                return "The object is not properly initialized";
            }
            case AudioRecord.ERROR_BAD_VALUE: {
                return "Parameters dont resolve to valid data and indexes";
            }
            case AudioRecord.ERROR_DEAD_OBJECT: {
                return "The object is not valid anymore, and needs to be re-created";
            }
            default: {
                return "Unknown error";
            }
        }
    }

}
