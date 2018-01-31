package tech.iosd.mieda;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;

public class AudioActivity extends AppCompatActivity {

    MediaRecorder recorder;
    String filePath;
    int AUDIO_PERM_REQ_CODE = 324;

    TextView tvRecStatus;
    Button btnStartRec, btnStopRec;

    String permissions[] = { Manifest.permission.RECORD_AUDIO };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        tvRecStatus = findViewById(R.id.tvRecStatus);
        btnStartRec = findViewById(R.id.btnStartRec);
        btnStopRec = findViewById(R.id.btnStopRec);

        ActivityCompat.requestPermissions(
                AudioActivity.this,
                permissions,
                AUDIO_PERM_REQ_CODE
        );

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == AUDIO_PERM_REQ_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.RECORD_AUDIO)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        audioRecord();
                    } else {
                        Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

    }

    private void audioRecord() {

        btnStartRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecording();
            }
        });
        btnStopRec.setEnabled(false);
        btnStopRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording();
            }
        });
    }

    void startRecording(){
        try {
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            filePath = getExternalCacheDir().getAbsolutePath() + "/" + nameOfFile() + ".3gp";
            recorder.setOutputFile(filePath);
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        tvRecStatus.setText("Recording in progress... Press Stop button to stop");
        btnStartRec.setEnabled(false);
        btnStopRec.setEnabled(true);
    }

    void stopRecording(){
        recorder.stop();
        recorder.release();
        recorder = null;

        tvRecStatus.setText("Recording stopped... File saved");
        btnStartRec.setEnabled(true);
        btnStopRec.setEnabled(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
    }

    String nameOfFile(){
        String str = "Audio Recording -- ";
        Calendar cal = Calendar.getInstance();
        str += String.valueOf(cal.get(Calendar.YEAR));
        str += "-";
        str += String.valueOf(cal.get(Calendar.MONTH)+1);
        str += "-";
        str += String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        str += "  ";
        str += String.valueOf(cal.get(Calendar.HOUR));
        str += ":";
        str += String.valueOf(cal.get(Calendar.MINUTE));
        str += ":";
        str += String.valueOf(cal.get(Calendar.SECOND));
        return str;
    }

}
