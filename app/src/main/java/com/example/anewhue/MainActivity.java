package com.example.anewhue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private SharedMemory mSharedMemory;
    private ToggleButton mToggleButton;
    private CountDownTimer mCountDownTimer;
    private Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToggleButton = findViewById(R.id.startButton);

        mSharedMemory = new SharedMemory(this);

        SeekBar alpha = findViewById(R.id.seek_alpha);
        SeekBar red = findViewById(R.id.seek_red);
        SeekBar green = findViewById(R.id.seek_green);
        SeekBar blue = findViewById(R.id.seek_blue);

        //SPINNER
        mSpinner = findViewById(R.id.filterspinner);
        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this, R.array.filters, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        mSpinner.setOnItemSelectedListener(this);
        //SPINNER END

        //SEEK BAR FILTER
        SeekBar.OnSeekBarChangeListener changeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mSharedMemory.setAlpha(alpha.getProgress());
                mSharedMemory.setRed(red.getProgress());
                mSharedMemory.setGreen(green.getProgress());
                mSharedMemory.setBlue(blue.getProgress());

                if (FilterService.STATE == FilterService.STATE_ACTIVE) {
                    Intent intent =new Intent(MainActivity.this, FilterService.class);
                    startService(intent);
                }

                mToggleButton.setChecked(FilterService.STATE == FilterService.STATE_ACTIVE);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };

        alpha.setOnSeekBarChangeListener(changeListener);
        red.setOnSeekBarChangeListener(changeListener);
        green.setOnSeekBarChangeListener(changeListener);
        blue.setOnSeekBarChangeListener(changeListener);

        //TURN ON FILTER
        mToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(MainActivity.this, FilterService.class);
                if (FilterService.STATE == FilterService.STATE_ACTIVE) {
                    stopService(i);
                } else {
                    startService(i);
                }
                refresh();
            }
        });
        //SEEK BAR FILTER END



        //NAVIGATION
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.filterbtn);
        //Making the navigation bar work
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.testbtn:
                        startActivity(new Intent(getApplicationContext(),TestActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.filterbtn:
                        return true;
                    case R.id.settingsbtn:
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });
        //NAVIGATION END
    }



    //SEEK BAR UPDATE
    private void refresh() {
        if(mCountDownTimer != null)
            mCountDownTimer.cancel();

        mCountDownTimer = new CountDownTimer(100, 100) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                mToggleButton.setChecked(FilterService.STATE == FilterService.STATE_ACTIVE);
            }
        };

        mCountDownTimer.start();
    }
    //SEEK BAR UPDATE END


    //SPINNER ITEM SELECT AND FILTER CHANGE
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        SeekBar alphabar = findViewById(R.id.seek_alpha);
        SeekBar redbar = findViewById(R.id.seek_red);
        SeekBar greenbar = findViewById(R.id.seek_green);
        SeekBar bluebar = findViewById(R.id.seek_blue);

        String choice = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(getApplicationContext(), choice+" Filter Selected", Toast.LENGTH_LONG).show();

        if(adapterView.getItemAtPosition(i).equals("Protonomaly (Red-Weak)")){
            alphabar.setProgress(250);
            redbar.setProgress(255);
            greenbar.setProgress(0);
            bluebar.setProgress(0);
            Toast.makeText(getApplicationContext(), "HI 1", Toast.LENGTH_LONG).show();
        }

        if(adapterView.getItemAtPosition(i).equals("Deutronomaly (Green-Weak)")){
            alphabar.setProgress(250);
            redbar.setProgress(0);
            greenbar.setProgress(255);
            bluebar.setProgress(0);
            Toast.makeText(getApplicationContext(), "HI 2", Toast.LENGTH_LONG).show();
        }

        if(adapterView.getItemAtPosition(i).equals("Tritanomaly (Blue-Weak)")){
            alphabar.setProgress(250);
            redbar.setProgress(0);
            greenbar.setProgress(0);
            bluebar.setProgress(255);
            Toast.makeText(getApplicationContext(), "HI 3", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    //SPINNER ITEM SELECT END
}