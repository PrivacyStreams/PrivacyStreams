package io.github.privacystreams.machine_learning;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.Arrays;

import io.github.privacystreams.core.Item;
import io.github.privacystreams.core.UQI;
import io.github.privacystreams.utils.Assertions;

class MLKitTextRecognition extends MLProcessor<Object>{
    String inputField;
    FirebaseVisionImage image;
    FirebaseVisionTextRecognizer recognizer = FirebaseVision.getInstance()
            .getOnDeviceTextRecognizer();
    Task<FirebaseVisionText> result;
    FirebaseVisionText actualResult;
    boolean onlyText;
    MLKitTextRecognition(String inputField, boolean onlyText){
        super(Arrays.asList(new String[]{inputField}));
        this.inputField = Assertions.notNull("inputField", inputField);
        this.addParameters(inputField);
        this.onlyText = Assertions.notNull("onlyText", onlyText);
        this.addParameters(onlyText);
    }

    //Expects inputField to be an upright bitmap

    @Override
    protected Object infer(UQI uqi, Item item){
        image = FirebaseVisionImage.fromBitmap((Bitmap)item.getValueByField(inputField));
        result = recognizer.processImage(image)
                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    @Override
                    public void onSuccess(FirebaseVisionText firebaseVisionText) {
                        // Task completed successfully
                        actualResult = firebaseVisionText;
                    }
                })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception
                                System.out.println("Text Recognizer FAILED");
                            }
                        });

        if(onlyText){
            return actualResult.getText();
        }
        else{
            return actualResult;
        }
    }
}
