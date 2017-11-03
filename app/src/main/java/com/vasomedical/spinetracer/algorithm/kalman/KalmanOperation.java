package com.vasomedical.spinetracer.algorithm.kalman;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.Random;

import jama.Matrix;
import jkalman.JKalman;

/**
 * Created by dehualai on 10/30/17.
 */

public class KalmanOperation {


    public ArrayList<Entry> process(ArrayList<Entry> input){

        ArrayList<Entry> output = new ArrayList<Entry>();

        try {
            JKalman kalman = new JKalman(4, 2);

        //    Random rand = new Random(System.currentTimeMillis() % 3000);
        //    double x = 0;
        //    double y = 0;
            // constant velocity
         //   double dx = rand.nextDouble();
         //   double dy = rand.nextDouble();

            double x = input.get(0).getX();
            double y = input.get(0).getY();

            // init
            Matrix s = new Matrix(4, 1); // state [x, y, dx, dy, dxy]
            Matrix c = new Matrix(4, 1); // corrected state [x, y, dx, dy, dxy]

            Matrix m = new Matrix(2, 1); // measurement [x]
            m.set(0, 0, x);
            m.set(1, 0, y);

            // transitions for x, y, dx, dy
            double[][] tr = {
                    {0.1, 0, 0.1, 0},
                    {0, 0.1, 0, 0.1},
                    {0, 0, 0.1, 0},
                    {0, 0, 0, 0.1} };
            kalman.setTransition_matrix(new Matrix(tr));

            // 1s somewhere?
            kalman.setError_cov_post(kalman.getError_cov_post().identity());


            for(int i = 1; i < input.size(); i++){
                x = input.get(i).getX();
                y = input.get(i).getY();

                m.set(0,0,x);
                m.set(1,0,y);


                c = kalman.Correct(m);
                Log.i("System", "" + i + ";" +  m.get(0, 0) + ";" + m.get(1, 0) + ";" + x + ";" + y + ";"
                        + s.get(0, 0) + ";" + s.get(1, 0) + ";" + s.get(2, 0) + ";" + s.get(3, 0) + ";"
                        + c.get(0, 0) + ";" + c.get(1, 0) + ";" + c.get(2, 0) + ";" + c.get(3, 0) + ";");

                output.add(new Entry((float) c.get(0, 0), (float) c.get(1, 0)));
            }

        } catch (Exception ex) {
            Log.i("System", ex.getMessage());
        }

        return output;
    }

}
