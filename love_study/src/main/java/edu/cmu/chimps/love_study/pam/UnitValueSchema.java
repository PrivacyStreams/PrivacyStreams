package edu.cmu.chimps.love_study.pam;

public class UnitValueSchema implements Schema {

    public String getSchemaName() {
        return "unit-value";
    }

    private Unit unit;
    private Value value;

    public UnitValueSchema(Unit propertyUnit, Value propertyValue) {
        this.unit = propertyUnit;
        this.value = propertyValue;
    }

    public Unit getPropertyUnit() {
        return unit;
    }

    public Value getPropertyValue() {
        return value;
    }

    public static class Unit implements Property {

        private String unit;

        public Unit(String unit) {
            this.unit = unit;
        }

        @Override
        public String getJsonName() {
            return "unit";
        }

        @Override
        public String getJsonValue() {
            return unit;
        }

    }

    public static class Value implements Property {

        private double value;

        public Value(double value) {
            this.value = value;
        }

        @Override
        public String getJsonName() {
            return "value";
        }

        @Override
        public Double getJsonValue() {
            return this.value;
        }

    }

}
