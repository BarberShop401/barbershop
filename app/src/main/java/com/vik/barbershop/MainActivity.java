package com.vik.barbershop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import java.io.File;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.PitchShifter;
import be.tarsos.dsp.effects.DelayEffect;
import be.tarsos.dsp.io.android.AndroidAudioPlayer;
import be.tarsos.dsp.io.android.AndroidFFMPEGLocator;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    AudioRecorder audioRecorder;
    int REQUEST_MICROPHONE = 69;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float lastX, lastY, lastZ;
    private float deltaXMax = 0;
    private float deltaYMax = 0;
    private float deltaZMax = 0;
    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check for RECORD_AUDIO permission, ask for it if we don't have it, then start recording
        requestRecordAudioPermission();

        //init sensor manager
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            Log.i("ljw", "success! we have an accelerometer");
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Log.i("ljw", "fail! we dont have an accelerometer!");
        }


        AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050,1024,0);
        PitchDetectionHandler pdh = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult result, AudioEvent e) {
                final float pitchInHz = result.getPitch();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView text = findViewById(R.id.pitchView);
                        text.setText("" + pitchInHz);
                    }
                });
            }
        };
//        AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
//        AudioProcessor p = new PitchShifter(2222, 4444, 1024, 500);
        AudioProcessor p = new DelayEffect(0.5, 0.5, 44100);
        dispatcher.addAudioProcessor(p);
        new Thread(dispatcher,"Audio Dispatcher").start();
    }


    //onResume() register the accelerometer for listening the events
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // get the change of the x,y,z values of the accelerometer
        deltaX = Math.abs(lastX - event.values[0]);
        deltaY = Math.abs(lastY - event.values[1]);
        deltaZ = Math.abs(lastZ - event.values[2]);

        // if the change is below 2, it is just plain noise
        if (deltaX < 2) deltaX = 0;
        if (deltaY < 2) deltaY = 0;
        if (deltaZ < 2) deltaZ = 0;

//        Log.i("ljw", "x/y/z: " + deltaX + "/" + deltaY + "/" + deltaZ);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //necessary for interface implementation but not being used currently
    }

//    onPause() unregister the accelerometer for stop listening the events
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }


    public void requestRecordAudioPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_MICROPHONE);
        } else {
            //if we already have recording permission, start it
            audioRecorder = new AudioRecorder();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == REQUEST_MICROPHONE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED) {
                    Log.i("audio", "RECORD_AUDIO permission granted");

                    //start recorder
                    audioRecorder = new AudioRecorder();
                }
            } else {
                Log.i("audio", "RECORD_AUDIO permission denied");
            }
        }

    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    //https://0110.be/tags/TarsosDSP

    public void playSavedAudioWithTarsos() {
        new AndroidFFMPEGLocator(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                File externalStorage = Environment.getExternalStorageDirectory();
                File mp3 = new File(externalStorage.getAbsolutePath() , "/audio.mp3");
                AudioDispatcher adp;
                adp = AudioDispatcherFactory.fromPipe(mp3.getAbsolutePath(),44100,5000,2500);
                adp.addAudioProcessor(new AndroidAudioPlayer(adp.getFormat(),5000, AudioManager.STREAM_MUSIC));
                adp.run();
            }
        }).start();
    }


}
