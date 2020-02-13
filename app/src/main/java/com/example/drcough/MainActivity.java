package com.example.drcough;

import android.Manifest;
import android.os.Bundle;

import com.deskode.recorddialog.RecordDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.musicg.fingerprint.FingerprintManager;
import com.musicg.fingerprint.FingerprintSimilarity;
import com.musicg.fingerprint.FingerprintSimilarityComputer;
import com.musicg.wave.Wave;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import javazoom.jl.converter.Converter;

public class MainActivity extends AppCompatActivity {

    RecordDialog recordDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //resources permsissions
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        recordDialog = RecordDialog.newInstance(getResources().getString(R.string.recod_audio));
                        recordDialog.setTitle(getResources().getString(R.string.recorder));
                        recordDialog.setMessage(getResources().getString(R.string.press_recod_audio));
                        recordDialog.show(MainActivity.this.getFragmentManager(),"TAG");

                        recordDialog.setPositiveButton(getResources().getString(R.string.save_btn), new RecordDialog.ClickListener() {
                            @Override
                            public void OnClickListener(String path) {

                                processCough(path);

                                //Toast.makeText(MainActivity.this,"Save audio: " + path, Toast.LENGTH_LONG).show();
                            }
                        });



                /**
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                 **/
            }
        });
    }


    private void processCough(String recordedPath)
    {
        try
        {
            new Converter().convert("White Wedding.mp3", "White Wedding.wav");
            //new Converter().convert("Poison.mp3", "Poison.wav");
            // Fingerprint from WAV
            byte[] firstFingerPrint = new FingerprintManager().extractFingerprint(new Wave("White Wedding.wav"));
            byte[] secondFingerPrint = new FingerprintManager().extractFingerprint(new Wave("Poison.wav"));
            // Compare fingerprints
            FingerprintSimilarity fingerprintSimilarity = new FingerprintSimilarityComputer(firstFingerPrint, secondFingerPrint).getFingerprintsSimilarity();
            System.out.println("Similarity score = " + fingerprintSimilarity.getScore());
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
