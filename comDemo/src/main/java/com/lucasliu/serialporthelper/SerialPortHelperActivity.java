package com.lucasliu.serialporthelper;

import android.app.Instrumentation;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;


import android.os.SystemClock;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.jeson.utils.FuncUtil;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

import android_serialport_api.Constants;
import android_serialport_api.SerialPortFinder;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import tp.xmaihh.serialport.SerialHelper;
import tp.xmaihh.serialport.bean.ComBean;
import tp.xmaihh.serialport.utils.ByteUtil;

import static android_serialport_api.Constants.SCAN;
import static com.jeson.utils.FuncUtil.close_QrPower;
import static com.jeson.utils.FuncUtil.close_Scan;
import static com.jeson.utils.FuncUtil.open_QrPower;
import static com.jeson.utils.FuncUtil.open_Scan;
import static com.jeson.utils.FuncUtil.readFile;
import static com.jeson.utils.FuncUtil.read_QrPower_State;
import static com.jeson.utils.FuncUtil.read_Scan_State;



/**
    * @Company    :
    * @Author     :  Lucas     联系WX:780203920
    * @Date       :2024/3/3 0003 17:25
    * @Description :SerialPortHelperActivity.java
    */
public class SerialPortHelperActivity extends AppCompatActivity {

    private RecyclerView recy;
    private Spinner      spSerial;
    private EditText         edInput;
    private Button           btSend;
    private RadioGroup       radioGroup;
    private RadioButton      radioButton1;
    private RadioButton      radioButton2;


    private SerialPortFinder serialPortFinder;
    private SerialHelper     serialHelper;




    private Spinner          spBote;
    private Button           btOpen;
    private Button           btScan;
    private LogListAdapter   logListAdapter;

    private Button           bt_openQrPower;
    private Button           bt_closeQrPower;
    private Button           bt_getState_QrPower;
    private Button           bt_openScanLight;
    private Button           bt_closeScanLight;
    private Button           bt_getState_ScanLight;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seria_port_helper);

        recy = (RecyclerView) findViewById(R.id.recy);
        spSerial = (Spinner) findViewById(R.id.sp_serial);
        edInput = (EditText) findViewById(R.id.ed_input);
        btSend = (Button) findViewById(R.id.bt_send);
        spBote = (Spinner) findViewById(R.id.sp_bote);
        btOpen = (Button) findViewById(R.id.bt_open);
        btScan = (Button) findViewById(R.id.bt_scan);

        bt_openQrPower = (Button) findViewById(R.id.bt_openQrPower);
        bt_closeQrPower = (Button) findViewById(R.id.bt_closeQrPower);
        bt_getState_QrPower = (Button) findViewById(R.id.bt_getState_QrPower);

        bt_openScanLight = (Button) findViewById(R.id.bt_openScanLight);
        bt_closeScanLight = (Button) findViewById(R.id.bt_closeScanLight);
        bt_getState_ScanLight = (Button) findViewById(R.id.bt_getState_ScanLight);




        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioButton1 = (RadioButton) findViewById(R.id.radioButton1);
        radioButton2 = (RadioButton) findViewById(R.id.radioButton2);



        logListAdapter = new LogListAdapter(null);

        recy.setLayoutManager(new LinearLayoutManager(this));
        recy.setAdapter(logListAdapter);
        recy.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        iniview();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        serialHelper.close();
    }

    private void iniview() {



        serialPortFinder = new SerialPortFinder();
        //初始化SerialHelper对象，初始化设定串口名称和波特率
        serialHelper = new SerialHelper(Constants.sPort,Constants.iBaudRate) {
            @Override
            protected void onDataReceived(final ComBean comBean) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (TextUtils.equals(FuncUtil.ByteArrToHex(comBean.bRec),Constants.SUCCESSCALLBACK)) {
                            showTips(getString(R.string.callbacksuccess));

                        } else if (TextUtils.equals(FuncUtil.ByteArrToHex(comBean.bRec),Constants.ERRORCALLBACK)) {
                            showTips(getString(R.string.callbackerror));
                        } else {
                            //Toast.makeText(getBaseContext(), FuncUtil.ByteArrToHex(comBean.bRec), Toast.LENGTH_SHORT).show();
                            showTips(comBean.sRecTime+":   "+new String(comBean.bRec));


                            FuncUtil.getVibrator(getApplicationContext(),500l);

                            FuncUtil.getVoice(getApplicationContext(),1.0f,1.0f);


                        }

                        Log.e("读取IO", "run=="+FuncUtil.ByteArrToHex(comBean.bRec));
                    }
                });
            }


        };




        boolean hasPorts =true;
         String[] ports = serialPortFinder.getAllDevicesPath();
        final String[] botes = new String[]{"0", "50", "75", "110", "134", "150", "200", "300", "600", "1200", "1800", "2400", "4800", "9600", "19200", "38400", "57600", "115200", "230400", "460800", "500000", "576000", "921600", "1000000", "1152000", "1500000", "2000000", "2500000", "3000000", "3500000", "4000000"};
        Log.e("串口数量", "iniview: "+ports.length);
        if (ports.length<=0) {
            hasPorts =false;
            String[] newArr = new String[ports.length + 1];
            System.arraycopy(ports, 0, newArr, 0, ports.length);
            newArr[ports.length] = Constants.sPort;

            ports=new String[]{Constants.sPort};
        }
        Log.e("串口数量", "iniview: "+ports.length);


        SpAdapter spAdapter = new SpAdapter(this);
        spAdapter.setDatas(ports);
        spSerial.setAdapter(spAdapter);
        spSerial.setSelection(ports.length-1);
         //串口号
        String[] finalPorts = ports;
        spSerial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                serialHelper.close();
                serialHelper.setPort(finalPorts[position]);

//                serialHelper.setPort(Constants.sPort);
                btOpen.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SpAdapter spAdapter2 = new SpAdapter(this);
        spAdapter2.setDatas(botes);
        spBote.setAdapter(spAdapter2);
        spBote.setSelection(17);
         //波特率
        spBote.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                serialHelper.close();
                serialHelper.setBaudRate(botes[position]);
//                serialHelper.setBaudRate("115200");
                btOpen.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
         //扫描命令
        btScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (serialHelper.isOpen()) {
                    try {
                        sendScan();
//                        Log.e("读取IO", "shex2== "+Long.parseLong(shex2));
                    } catch (Exception e) {
                        showTips(getString(R.string.please));
                    }
                } else {
                    showTips( getString(R.string.pleases));
                }




            }
        });
        //打开串口
        boolean finalHasPorts = hasPorts;
        btOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FuncUtil.getVibrator(getApplicationContext(),500l);
                if (finalHasPorts) {
                    try {
                        serialHelper.open();
                        btOpen.setEnabled(false);
                        showTips(getString(R.string.portssuccess));
                    }catch(Throwable t)
                    {

                        showTips(getString(R.string.portserror));
                    }
                } else {
                    showTips( getString(R.string.hasports));
                }

            }
        });
         //发送按钮
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //text
                if (radioGroup.getCheckedRadioButtonId() == R.id.radioButton1) {
                    if (edInput.getText().toString().length() > 0) {
                        if (serialHelper.isOpen()) {
                            serialHelper.sendHex(Constants.ACTIVATION);
                            SystemClock.sleep(20);
                            serialHelper.sendTxt(edInput.getText().toString());

                        } else {
                            showTips( getString(R.string.pleases));

                        }
                    } else {
                        showTips( getString(R.string.cmmd));

                    }
                } else {//hex
                    if (edInput.getText().toString().length() > 0) {
                        if (serialHelper.isOpen()) {
                            try {
                                serialHelper.sendHex(Constants.ACTIVATION);
                                SystemClock.sleep(20);
                                serialHelper.sendHex(edInput.getText().toString());
                            } catch (Exception e) {
                                showTips(getString(R.string.please));

                            }

                        } else {
                            showTips(getString(R.string.pleases));

                        }
                    } else {
                        showTips(getString(R.string.cmmd));

                    }
                }


            }
        });


        //打开二维码电源
        bt_openQrPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //自定义按键
                //String key = "/sys/class/misc/wiite_con_ctrl/???????????";
                //打开二维码电源
                open_QrPower();
                showTips(getString(R.string.qrpower)+"   "+getString(R.string.openn));
            }
        });
        //关闭二维码电源
        bt_closeQrPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //自定义按键
                //String key = "/sys/class/misc/wiite_con_ctrl/???????????";
                //关闭二维码电源
                close_QrPower();
                showTips(getString(R.string.qrpower)+"   "+getString(R.string.closen));
            }
        });
        //读取二维码电源状态
        bt_getState_QrPower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //自定义按键
                //String key = "/sys/class/misc/wiite_con_ctrl/???????????";
                try {
                    //读取二维码电源状态
                    String s_power = read_QrPower_State();
                    s_power=s_power.equals("0")?getString(R.string.closen):getString(R.string.openn);
                    showTips(getString(R.string.qrpower)+"   "+getString(R.string.getState)+":   "+s_power);
                } catch (Throwable e) {
                    showTips("没有读取权限");
                }


            }
        });
        //打开扫码灯
        bt_openScanLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //自定义按键
                //String key = "/sys/class/misc/wiite_con_ctrl/???????????";
                //打开扫码灯
                open_Scan();
                showTips(getString(R.string.scanLight)+"   "+getString(R.string.openn));

            }
        });
        //关闭扫码灯
        bt_closeScanLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //自定义按键
                //String key = "/sys/class/misc/wiite_con_ctrl/???????????";
                //关闭扫码灯
                close_Scan();
                showTips(getString(R.string.scanLight)+"   "+getString(R.string.closen));
            }
        });
        //读取扫码灯状态
        bt_getState_ScanLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //自定义按键
                //String key = "/sys/class/misc/wiite_con_ctrl/???????????";
                try {
                    //读取扫码灯状态
                    String s_scan = read_Scan_State();
                    s_scan=s_scan.equals("0")?getString(R.string.closen):getString(R.string.openn);
                    Log.e("读取IO", "扫码灯=="+s_scan);
                    showTips(getString(R.string.scanLight) + "   " + getString(R.string.getState) + ":   " + s_scan);
                } catch (Throwable e) {

                    showTips("没有读取权限");
                }


            }


        });




    }


    private void showTips(String tips) {
        Toast.makeText(getBaseContext(),tips , Toast.LENGTH_SHORT).show();
        logListAdapter.addData(tips);
        recy.smoothScrollToPosition(logListAdapter.getData().size());
    }



     //=================
/*     @Override
     public boolean onKeyUp(int keyCode, KeyEvent event) {
         Log.e("读取IO", "onKeyUp_keyCode=="+keyCode);
         if (keyCode == KeyEvent.KEYCODE_ENTER) {
             // 释放回车键时的处理逻辑
             // ...
             return true; // 返回true表示已经处理了该按键事件
         }
         return super.onKeyUp(keyCode, event);
     }*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("读取IO", "onKeyDown_keyCode=="+keyCode+"KeyEvent=="+event);
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {

            sendScan();


            //中间按键为19 scanCode=103
            Toast.makeText(getBaseContext(), "keyCode=  "+keyCode, Toast.LENGTH_SHORT).show();
            logListAdapter.addData("keyCode=  "+keyCode);
            recy.smoothScrollToPosition(logListAdapter.getData().size());
            return true; // 返回true表示已经处理了该按键事件
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            //右按键为22 scanCode=106
            Toast.makeText(getBaseContext(), "keyCode=  "+keyCode, Toast.LENGTH_SHORT).show();
            logListAdapter.addData("keyCode=  "+keyCode);
            recy.smoothScrollToPosition(logListAdapter.getData().size());
            return true; // 返回true表示已经处理了该按键事件
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            //左按键为82 scanCode=139
            Toast.makeText(getBaseContext(), "keyCode=  "+keyCode, Toast.LENGTH_SHORT).show();
            logListAdapter.addData("keyCode=  "+keyCode);
            recy.smoothScrollToPosition(logListAdapter.getData().size());
            return true; // 返回true表示已经处理了该按键事件
        }


        return super.onKeyDown(keyCode, event);
    }

    private void sendScan() {
        if (serialHelper!=null) {
            //先唤醒扫描头
            serialHelper.sendHex(Constants.ACTIVATION);
            SystemClock.sleep(20);
            serialHelper.sendHex(SCAN);
        }

    }

    /**
     * 执行shell命令
     *
     * @param cmd
     */
    private void execShellCmd(String cmd) {

        try {
            // 申请获取root权限，这一步很重要，不然会没有作用
            Process process = Runtime.getRuntime().exec("su");
            // 获取输出流
            OutputStream outputStream = process.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(
                    outputStream);
            dataOutputStream.writeBytes(cmd);
            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.close();
        } catch (Throwable t) {
            t.printStackTrace();
            Log.e("读取IO", "execShellCmd: ");
        }
    }


    //==================


}
