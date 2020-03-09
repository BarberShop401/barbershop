package com.vik.barbershop;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import android.media.audiofx.PresetReverb;

public class AudioRecorder extends Thread {




    private boolean stopped = false;

    /**
     * Give the thread high priority so that it's not canceled unexpectedly, and start it
     */
    public AudioRecorder() {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

        start();
    }


    @Override
    public void run() {
        Log.i("Audio", "Running Audio Thread");
        AudioRecord recorder = null;
        AudioTrack track = null;
        short[][] buffers = new short[256][160];
        int ix = 0;

        /*
         * Initialize buffer to hold continuously recorded audio data, start recording, and start
         * playback.
         */
        try {
            int N = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
            recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, N * 10);
            track = new AudioTrack(AudioManager.STREAM_MUSIC, 8000,
                    AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, N * 5, AudioTrack.MODE_STREAM);

            recorder.startRecording();

            track.play();

            //doesn't do anything
//            PresetReverb reverb = new PresetReverb(0, track.getAudioSessionId());
//            reverb.setPreset(PresetReverb.PRESET_LARGEHALL);



            /*
             * Loops until something outside of this thread stops it.
             * Reads the data from the recorder and writes it to the audio track for playback.
             */
            long count = 0;
            while (!stopped) {
//                Log.i("Map", "Writing new data to buffer");

                short[] buffer = buffers[ix++ % buffers.length];


                N = recorder.read(buffer, 0, buffer.length);


                for (int i = 0; i < buffer.length; i++) {
//                    buffer[i] *= 20;
                }

                track.write(buffer, 0, buffer.length);

                count++;
                if (count % 200 == 0) {
                    Log.i("ljw", Arrays.toString(buffer));
                }
            }
        } catch (Throwable x) {
            Log.w("Audio", "Error reading voice audio", x);
        }
        /*
         * Frees the thread's resources after the loop completes so that it can be run again
         */ finally {
            recorder.stop();
            recorder.release();
            track.stop();
            track.release();
        }
    }

    /**
     * Called from outside of the thread in order to stop the recording/playback loop
     */
    private void close() {
        stopped = true;
    }

}