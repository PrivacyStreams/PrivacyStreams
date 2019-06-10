package io.github.privacystreams.machine_learning;

public class Recognition{
    private final String id;
    private final Float confidence;
    private float[] location;

    public Recognition(
            final String id, final Float confidence, final float[] location) {
        this.id = id;
        this.confidence = confidence;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public Float getConfidence() {
        return confidence;
    }

    public float[] getLocation() {
        return location;
    }

    @Override
    public String toString() {
        String resultString = "";
        if (id != null) {
            resultString += "[" + id + "] ";
        }

        if (confidence != null) {
            resultString += String.format("(%.1f%%) ", confidence * 100.0f);
        }

        if (location != null) {
            resultString += " (" + location[0] + ", " + location[1] + ", " + location[2] + ", " + location[3] + ") ";
        }

        return resultString.trim();
    }
}
