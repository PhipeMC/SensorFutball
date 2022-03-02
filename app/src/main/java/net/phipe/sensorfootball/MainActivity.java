package net.phipe.sensorfootball;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    MyBall draw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        draw = new MyBall(this);
        setContentView(draw);
    }

    class MyBall extends View implements SensorEventListener {

        int width, height;
        int size = 20;
        int border = 6;
        int P1 = 0, P2 = 0;
        float eX, eY, eZ;
        String X,Y,Z;
        Paint pincel = new Paint();

        public MyBall(Context context) {
            super(context);
            SensorManager smAdmin = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
            Sensor snsRotacion = smAdmin.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            smAdmin.registerListener(this, snsRotacion, SensorManager.SENSOR_DELAY_FASTEST);
            Display screen = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            width = screen.getWidth();
            height = screen.getHeight();
        }

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            eX -= sensorEvent.values[0];
            X = Float.toString(eX);
            if(eX < (size + border)){
                eX = (size + border);
            }else if(eX > (width - (size + border))){
                eX = width - (size + border);
            }

            eY += sensorEvent.values[1];
            Y = Float.toString(eY);
            if(eY < (size + border)){
                eY = (size + border);
                if(eX > (width/5)*2 && eX < (width/5)*3){
                    P1++;
                    eX = width/2;
                    eY = height/2;
                }
            }else if(eY > (height - size)){
                eY = height - size;
                if(eX > (width/5)*2 && eX < (width/5)*3){
                    P2++;
                    eX = width/2;
                    eY = height/2;
                }
            }

            eZ = sensorEvent.values[2];
            Z = String.format("%.2f",eZ);
            invalidate();
            setBackgroundResource(R.drawable.cancha);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }

        @Override
        protected void onDraw(Canvas canvas) {
            pincel.setColor(Color.RED);
            canvas.drawCircle(eX, eY, eZ+size, pincel);
            pincel.setColor(Color.WHITE);
            pincel.setTextSize(70);
            canvas.drawText(String.format("Score: %d:%d",P1,P2),10,80,pincel);
        }
    }
}