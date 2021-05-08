package com.cookandroid.bluetooth_2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    TextView mTvVaccum;
    TextView mTvSpd;
    ImageView mIvBluetooth;
    Button mBtnConnect, mBtnBreak;
    Button mBtnLeft;
    Button mBtnRight;
    Button mBtnOne;
    Button mBtnTwo;
    Button mBtnThr;
    Switch mSwVaccum;

    RadioGroup mRdoGruoup;
    RadioButton mRdoD, mRdoN, mRdoP;

    BluetoothAdapter mBluetoothAdapter;
    Set<BluetoothDevice> mPairedDevices;
    List<String> mListPairedDevices;

    ConnectedBluetoothThread mThreadConnectedBluetooth;
    BluetoothDevice mBluetoothDevice;
    BluetoothSocket mBluetoothSocket;


    final static int BT_MESSAGE_READ = 2;
    final static int BT_CONNECTING_STATUS = 3;
    final static UUID BT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvVaccum = (TextView)findViewById(R.id.tvVaccum);
        mTvSpd = (TextView)findViewById(R.id.tvSpd);
        mIvBluetooth = (ImageView) findViewById(R.id.ivBluetooth);
        mBtnConnect = (Button)findViewById(R.id.btnConnect);
        mBtnLeft = (Button)findViewById(R.id.btnLeft);
        mBtnRight = (Button)findViewById(R.id.btnRight);
        mBtnOne = (Button)findViewById(R.id.btnOne);
        mBtnTwo = (Button)findViewById(R.id.btnTwo);
        mBtnThr = (Button)findViewById(R.id.btnThr);
        mBtnBreak = (Button)findViewById(R.id.btnBreak);
        mSwVaccum = (Switch)findViewById(R.id.swVaccum);
        mRdoGruoup = (RadioGroup)findViewById(R.id.rdoGroup);
        mRdoD = (RadioButton)findViewById(R.id.rdoD);
        mRdoN = (RadioButton)findViewById(R.id.rdoN);
        mRdoP = (RadioButton)findViewById(R.id.rdoP);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        mIvBluetooth.setImageResource(R.drawable.blacktooth);


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

        mBtnBreak.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int status = event.getAction();
                if(status == MotionEvent.ACTION_DOWN){
                    if(mThreadConnectedBluetooth != null) {
                        mThreadConnectedBluetooth.write("s");
                    }
                }
                if(status == MotionEvent.ACTION_UP){
                    if(mThreadConnectedBluetooth != null) {
                        mThreadConnectedBluetooth.write("sr");
                    }
                }
                return false;
            }
        });

        mRdoGruoup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rdoD){
                    if(mThreadConnectedBluetooth != null) {
                        mThreadConnectedBluetooth.write("D");
                    }
                } else if(checkedId == R.id.rdoN){
                    if(mThreadConnectedBluetooth != null) {
                        mThreadConnectedBluetooth.write("N");
                    }
                } else if(checkedId == R.id.rdoP){
                    if(mThreadConnectedBluetooth != null) {
                        mThreadConnectedBluetooth.write("P");
                    }
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
                builder.setTitle("장치 선택");

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
                Toast.makeText(getApplicationContext(), "페어링된 장치가 없습니다.", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "블루투스가 비활성화 되어 있습니다.", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(), "블루투스 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
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
                Toast.makeText(getApplicationContext(), "소켓 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
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
                Toast.makeText(getApplicationContext(), "데이터 전송 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }
        }
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "소켓 해제 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }
}