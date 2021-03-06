package com.cookandroid.bluetooth_2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    TextView mTvVaccum, mTvVaccum_2;
    TextView mTvSpd;
    ImageView mIvBluetooth;
    Button mBtnConnect;
    Button mBtnLeft, mBtnRight, mBtnGo, mBtnBack;
    Button mBtnOne, mBtnTwo, mBtnThr;
    Switch mSwVaccum;

    BluetoothAdapter mBluetoothAdapter;
    Set<BluetoothDevice> mPairedDevices;
    List<String> mListPairedDevices;

    ConnectedBluetoothThread mThreadConnectedBluetooth;
    BluetoothDevice mBluetoothDevice;
    BluetoothSocket mBluetoothSocket;

    final static UUID BT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvVaccum = (TextView)findViewById(R.id.tvVaccum);
        mTvVaccum_2 = (TextView)findViewById(R.id.tvVaccum_2);
        mTvSpd = (TextView)findViewById(R.id.tvSpd);
        mIvBluetooth = (ImageView) findViewById(R.id.ivBluetooth);
        mBtnConnect = (Button)findViewById(R.id.btnConnect);
        mBtnLeft = (Button)findViewById(R.id.btnLeft);
        mBtnRight = (Button)findViewById(R.id.btnRight);
        mBtnOne = (Button)findViewById(R.id.btnOne);
        mBtnTwo = (Button)findViewById(R.id.btnTwo);
        mBtnThr = (Button)findViewById(R.id.btnThr);
        mBtnGo = (Button)findViewById(R.id.btnGo);
        mBtnBack = (Button)findViewById(R.id.btnBack);
        mSwVaccum = (Switch)findViewById(R.id.swVaccum);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        mIvBluetooth.setImageResource(R.drawable.blacktooth);

        mBtnConnect.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                listPairedDevices();
            }
        });

        mBtnLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int status = event.getAction();
                if(status == MotionEvent.ACTION_DOWN){
                    if(mThreadConnectedBluetooth != null) {
                        mThreadConnectedBluetooth.write("l");
                    }
                }
                if(status == MotionEvent.ACTION_UP){
                    if(mThreadConnectedBluetooth != null) {
                        mThreadConnectedBluetooth.write("lr");
                    }
                }
                return false;
            }
        });

        mBtnRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int status = event.getAction();
                if(status == MotionEvent.ACTION_DOWN){
                    if(mThreadConnectedBluetooth != null) {
                        mThreadConnectedBluetooth.write("r");
                    }
                }
                if(status == MotionEvent.ACTION_UP){
                    if(mThreadConnectedBluetooth != null) {
                        mThreadConnectedBluetooth.write("rr");
                    }
                }
                return false;
            }
        });

        mBtnGo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int status = event.getAction();
                if(status == MotionEvent.ACTION_DOWN){
                    if(mThreadConnectedBluetooth != null) {
                        mThreadConnectedBluetooth.write("g");
                    }
                }
                if(status == MotionEvent.ACTION_UP){
                    if(mThreadConnectedBluetooth != null) {
                        mThreadConnectedBluetooth.write("gr");
                    }
                }
                return false;
            }
        });

        mBtnBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int status = event.getAction();
                if(status == MotionEvent.ACTION_DOWN){
                    if(mThreadConnectedBluetooth != null) {
                        mThreadConnectedBluetooth.write("b");
                    }
                }
                if(status == MotionEvent.ACTION_UP){
                    if(mThreadConnectedBluetooth != null) {
                        mThreadConnectedBluetooth.write("br");
                    }
                }
                return false;
            }
        });

        mBtnOne.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mThreadConnectedBluetooth != null) {
                    mThreadConnectedBluetooth.write("one");
                    mTvSpd.setText(": 1");
                }
            }
        });

        mBtnTwo.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mThreadConnectedBluetooth != null) {
                    mThreadConnectedBluetooth.write("two");
                    mTvSpd.setText(": 2");
                }
            }
        });
        mBtnThr.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mThreadConnectedBluetooth != null) {
                    mThreadConnectedBluetooth.write("thr");
                    mTvSpd.setText(": 3");
                }
            }
        });



        mSwVaccum.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked) {
                    mTvVaccum.setText("ON");
                    if(mThreadConnectedBluetooth != null) {
                        mThreadConnectedBluetooth.write("o");
                    }
                }
                else {
                    mTvVaccum.setText("OFF");
                    if(mThreadConnectedBluetooth != null) {
                        mThreadConnectedBluetooth.write("f");
                    }
                }
            }
        });
    }


    void listPairedDevices() {
        if (mBluetoothAdapter.isEnabled()) {
            mPairedDevices = mBluetoothAdapter.getBondedDevices();

            if (mPairedDevices.size() > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("?????? ??????");

                mListPairedDevices = new ArrayList<String>();
                for (BluetoothDevice device : mPairedDevices) {
                    mListPairedDevices.add(device.getName());
                    //mListPairedDevices.add(device.getName() + "\n" + device.getAddress());
                }
                final CharSequence[] items = mListPairedDevices.toArray(new CharSequence[mListPairedDevices.size()]);
                mListPairedDevices.toArray(new CharSequence[mListPairedDevices.size()]);

                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        connectSelectedDevice(items[item].toString());
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                Toast.makeText(getApplicationContext(), "???????????? ????????? ????????????.", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "??????????????? ???????????? ?????? ????????????.", Toast.LENGTH_SHORT).show();
        }
    }
    void connectSelectedDevice(String selectedDeviceName) {
        for(BluetoothDevice tempDevice : mPairedDevices) {
            if (selectedDeviceName.equals(tempDevice.getName())) {
                mBluetoothDevice = tempDevice;
                mIvBluetooth.setImageResource(R.drawable.bluetooth);
                break;
            }
        }
        try {
            mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(BT_UUID);
            mBluetoothSocket.connect();
            mThreadConnectedBluetooth = new ConnectedBluetoothThread(mBluetoothSocket);
            mThreadConnectedBluetooth.start();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "???????????? ?????? ??? ????????? ??????????????????.", Toast.LENGTH_LONG).show();
        }
    }

    private class ConnectedBluetoothThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedBluetoothThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "?????? ?????? ??? ????????? ??????????????????.", Toast.LENGTH_LONG).show();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    bytes = mmInStream.available();
                    if (bytes != 0) {
                        SystemClock.sleep(100);
                        bytes = mmInStream.available();
                        bytes = mmInStream.read(buffer, 0, bytes);
                    }
                } catch (IOException e) {
                    break;
                }
            }
        }
        public void write(String str) {
            byte[] bytes = str.getBytes();
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "????????? ?????? ??? ????????? ??????????????????.", Toast.LENGTH_LONG).show();
            }
        }
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "?????? ?????? ??? ????????? ??????????????????.", Toast.LENGTH_LONG).show();
            }
        }
    }
}