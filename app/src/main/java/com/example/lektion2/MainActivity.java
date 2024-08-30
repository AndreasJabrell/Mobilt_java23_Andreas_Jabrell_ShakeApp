package com.example.lektion2;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private long lastLogTime = 0;

    ImageView iv;
    TextView tv;
    TextView tv2;
    ProgressBar pb1;
    TextView tv3;
    SeekBar pb2;

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
        Log.i("Andreas", "onPause:");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null && sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        Log.i("Andreas", "onResume:");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("Andreas", "onRestart:");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView);

        iv = findViewById(R.id.imageView);
        tv2 = findViewById(R.id.textView5);
        pb1 = findViewById(R.id.progressBar);
        pb1.setMax(200);
        tv3 = findViewById(R.id.textView6);
        pb2 = findViewById(R.id.seekBar);
        pb2.setMax(200);
        iv.setImageResource(R.drawable.cowboy_cover);

        tv.setTextColor(getResources().getColor(R.color.purple));

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (sensorManager != null) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }


        //Typeface typeface = ResourcesCompat.getFont(this, R.font.codystar_light);
        //tv.setTypeface(typeface);

        Button b = findViewById(R.id.button3);
        Intent A2 = new Intent(MainActivity.this, MainActivity2.class);


        b.setOnClickListener((e) -> {
            startActivity(A2);
            Log.i("Andreas", "knappen funkar");
            tv.setText(getResources().getStringArray(R.array.system)[1]);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;
        });


    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long currentTime = System.currentTimeMillis();

            if (currentTime - lastLogTime >= 1000) {
                Log.i("AccelerometerData", "X: " + x + ", Y: " + y + ", Z: " + z);
                lastLogTime = currentTime; // Uppdatera senaste loggtid
                tv2.setText("X axis " + (String.valueOf(x)));
                tv3.setText("Y axis " + (String.valueOf(y)));
                pb1.setProgress((int) Math.round(x) * 10 + 100);
                pb2.setProgress((int) Math.round(y) * 10 + 100);
                tv.setText("Z axis "  + (String.valueOf(z)));
            }

            iv.setAlpha(z/20);


                try {
                    if (x < 4 && x > -4) {
                        iv.setRotation(x * 5);
                    }
                    if (x < -20 || x > 20) {
                        Toast.makeText(MainActivity.this, "SLUTA SKAKA MIG", Toast.LENGTH_SHORT).show();
                        Log.i("Andreas", "JORDBÄVNING" + x);
                    }
                } catch (Exception e) {
                    Log.e("SensorError", "Error setting rotation", e);
                }




        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
