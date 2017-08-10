package com.vasomedical.spinetracer.model;

/**
 * Created by dehualai on 4/6/17.
 */

public class MedianFilter {

    // must be odd number : 3,5,7,9.......2n+1
    int WIN_SIZE = 3;
    int HALF_WIN_SIZE = (WIN_SIZE-1)/2;
    int N;


    private Float[] filter(Float[] inputSignal){
        Float[] result = new Float[N];
        for(int i = HALF_WIN_SIZE ; i < inputSignal.length - HALF_WIN_SIZE; i ++){
            float sum = 0 ;
            for(int j =- HALF_WIN_SIZE; j <= HALF_WIN_SIZE; j++){
                sum += inputSignal[i + j];
            }
            result[i-HALF_WIN_SIZE] = sum / WIN_SIZE;
        }
        return result;

    }




    public Float[] applyFilter(Float[] originalSignal){
        N = originalSignal.length;
        // create extension
        Float[] extension = new Float[ N + 2*HALF_WIN_SIZE];
        for(int i = HALF_WIN_SIZE; i < N + HALF_WIN_SIZE; i++){
            extension[i] = originalSignal[i-HALF_WIN_SIZE];
        }

        for(int i = 0 ; i < HALF_WIN_SIZE; i++){
            extension[i] = originalSignal[1+i];
            extension[N + HALF_WIN_SIZE + i ] = originalSignal[ N-1-i];
        }

        return filter(extension);

    }

}
