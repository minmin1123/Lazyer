package com.application.lazyer;

import android.app.Activity;
import android.app.Notification;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;

public class MainActivity extends Activity {

    private ImageView mIVLight1;//灯1图标

    private ImageView mIVLight2;//灯2图标

    private RadioGroup mRGLightOn1;//灯1组

    private RadioGroup mRGLightOn2;//灯2组

    final int RCV_MSG=0x123;//收到服务器发送信息的msg.what

    final int SND_MSG=0x234;//发送信息到服务器的msg.what

    Handler handler=new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what){
                case RCV_MSG:
                    Bundle bundle=msg.getData();
                    String str=bundle.getString("data");
                    Toast.makeText(MainActivity.this,str,Toast.LENGTH_SHORT).show();
                    break;
                case SND_MSG:
                    TCPSendData(msg.getData());

        }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRcvThread();
        initView();
    }

    public void initRcvThread(){
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                //如果收到数据则发送msg到handler
                final String host = "192.168.1.102";
                final int port = 8086;
                try{
                    Socket socket = new Socket(host, port);
                    socket.setSoTimeout(8000);
                    InputStream inputStream = socket.getInputStream();
                    byte[] buf = new byte[1024];
                    while(true){
                        int len = inputStream.read(buf);
                        String receData = new String(buf, 0, len, Charset.forName("UTF-8"));
                        if(!receData.equals("")){
                            Message msg=new Message();
                            msg.what=RCV_MSG;
                            Bundle bundle=new Bundle();
                            bundle.putString("data", receData);
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        }
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void initView(){
        mIVLight1 = (ImageView) findViewById(R.id.imgViewLight1);
        mIVLight2 = (ImageView) findViewById(R.id.imgViewLight2);
        mRGLightOn1 = (RadioGroup) findViewById(R.id.radioGroup1);
        mRGLightOn2 = (RadioGroup) findViewById(R.id.radioGroup2);


        mRGLightOn1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch(i){
                    case R.id.LightOn1:
                        mIVLight1.setBackgroundResource(R.drawable.lighton);
                        try {
                            sendJSONmsg(radioGroup.getId(),true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.LightOff1:
                        mIVLight1.setBackgroundResource(R.drawable.lightoff);
                        try {
                            sendJSONmsg(radioGroup.getId(),false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });
        mRGLightOn2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch(i){
                    case R.id.LightOn2:
                        mIVLight2.setBackgroundResource(R.drawable.lighton);
                        try {
                            sendJSONmsg(radioGroup.getId(),true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case R.id.LightOff2:
                        mIVLight2.setBackgroundResource(R.drawable.lightoff);
                        try {
                            sendJSONmsg(radioGroup.getId(),false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });
    }

    public String RtJSON(int gropId,boolean isOn) throws JSONException {
        JSONObject jsout=new JSONObject();
        jsout.put("gropId", gropId);
        jsout.put("isOn",isOn);
        return jsout.toString();
    }

    public void sendJSONmsg(int gropId,boolean isOn) throws JSONException {
        Message msg=new Message();
        msg.what=SND_MSG;
        Bundle data=new Bundle();
        data.putString("json",RtJSON(gropId,isOn));
        msg.setData(data);
        handler.sendMessage(msg);
    }


    public void TCPSendData(final Bundle data){
        final String host = "192.168.1.102";
        final int port = 8086;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Socket socket = null;
                try {
                    // 1、创建连接
                    socket = new Socket(host, port);
                    if (socket.isConnected()) {
                    }
                    // 2、设置读流的超时时间
                    socket.setSoTimeout(8000);
                    // 3、获取输出流
                    OutputStream outputStream = socket.getOutputStream();
                    // 4、发送信息
                    String json=data.getString("json");
                    byte[] sendData = json.getBytes(Charset.forName("UTF-8"));
                    outputStream.write(sendData, 0, sendData.length);
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (socket != null) {
                        try {
                            socket.close();
                            socket = null;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }


}