package com.example.mldemoapp;

public class CounterWrapper {
    int counter;
    public void counterInit(){
        counter = 0;
    }

    public int getCounter(){
        return counter;
    }

    public void increaseCounter(){
        counter++;
    }
}
