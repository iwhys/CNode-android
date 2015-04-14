package com.zbar.lib;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zbar.lib.camera.CameraManager;
import com.zbar.lib.decode.CaptureHandler;
import com.zbar.lib.decode.InactivityTimer;

import java.io.IOException;

/**
 * 作者: 陈涛(1076559197@qq.com)
 *
 * 时间: 2014年5月9日 下午12:25:31
 *
 * 版本: V_1.0.0
 *
 * 描述: 扫描界面
 */
public class CaptureView implements Callback, View.OnClickListener {

    private ActionBarActivity activity;
    private View view;
    private OnCaptureListener onCaptureListener;
    private CaptureHandler handler;
    private boolean hasSurface;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.50f;
    private boolean vibrate;
    private int x = 0;
    private int y = 0;
    private int cropWidth = 0;
    private int cropHeight = 0;
    private RelativeLayout mContainer, mCropLayout;
    private boolean isNeedCapture = false;

    public CaptureView(ActionBarActivity activity){
        this.activity = activity;
        init();
    }

    public View getView(){
        return view;
    }

    public void setOnCaptureListener(OnCaptureListener onCaptureListener){
        this.onCaptureListener = onCaptureListener;
    }

    public boolean isNeedCapture() {
        return isNeedCapture;
    }

    public void setNeedCapture(boolean isNeedCapture) {
        this.isNeedCapture = isNeedCapture;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getCropWidth() {
        return cropWidth;
    }

    public void setCropWidth(int cropWidth) {
        this.cropWidth = cropWidth;
    }

    public int getCropHeight() {
        return cropHeight;
    }

    public void setCropHeight(int cropHeight) {
        this.cropHeight = cropHeight;
    }

    /**
     * 设置扫描区域尺寸
     * @param width 宽高相等
     */
    public void setCropLayoutSize(int width, int height){
        mCropLayout.getLayoutParams().width = width;
        mCropLayout.getLayoutParams().height = height;
    }

    /** Called when the activity is first created. */
    private void init() {
        view = View.inflate(activity, R.layout.inc_scanner, null);
        // 初始化 CameraManager
        CameraManager.init(activity);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(activity);

        mContainer = (RelativeLayout) view.findViewById(R.id.capture_containter);
        mCropLayout = (RelativeLayout) view.findViewById(R.id.capture_crop_layout);

        view.findViewById(R.id.button_back).setOnClickListener(this);
        view.findViewById(R.id.button_light).setOnClickListener(this);

        ImageView mQrLineView = (ImageView) view.findViewById(R.id.capture_scan_line);
        TranslateAnimation mAnimation = new TranslateAnimation(TranslateAnimation.ABSOLUTE, 0f, TranslateAnimation.ABSOLUTE, 0f,
                TranslateAnimation.RELATIVE_TO_PARENT, 0f, TranslateAnimation.RELATIVE_TO_PARENT, 0.66f);
        mAnimation.setDuration(1500);
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(Animation.REVERSE);
        mAnimation.setInterpolator(new LinearInterpolator());
        mQrLineView.setAnimation(mAnimation);
    }

    boolean flag = true;

    protected void light(View v) {
        if (flag) {
            flag = false;
            // 开闪光灯
            v.setBackgroundResource(R.drawable.icon_flashlight_on);
            CameraManager.get().openLight();
        } else {
            flag = true;
            // 关闪光灯
            v.setBackgroundResource(R.drawable.icon_flashlight_off);
            CameraManager.get().offLight();
        }

    }

    @SuppressWarnings("deprecation")
    public void onResume() {
        SurfaceView surfaceView = (SurfaceView) view.findViewById(R.id.capture_preview);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        playBeep = true;
        AudioManager audioService = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    public void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    public void onDestroy() {
        inactivityTimer.shutdown();
    }

    public void handleDecode(String result) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        if (onCaptureListener != null){
            onCaptureListener.onCapture(result);
        } else {
            throw new RuntimeException("缺少扫码结果处理接口");
        }
    }

    /**
     * 扫描后调用此方法可重新启动扫描功能
     */
    public void restartPreview(){
        // 连续扫描，不发送此消息扫描一次结束后就不能再次扫描
        handler.sendEmptyMessage(R.id.restart_preview);
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);

            Point point = CameraManager.get().getCameraResolution();
            int w = point.y;
            int h = point.x;

            int x = mCropLayout.getLeft() * w / mContainer.getWidth();
            int y = mCropLayout.getTop() * h / mContainer.getHeight();

            int cropWidth = mCropLayout.getWidth() * w / mContainer.getWidth();
            int cropHeight = mCropLayout.getHeight() * h / mContainer.getHeight();

            setX(x);
            setY(y);
            setCropWidth(cropWidth);
            setCropHeight(cropHeight);
            // 设置是否需要截图
            setNeedCapture(true);


        } catch (IOException | RuntimeException ioe) {
            return;
        }
        if (handler == null) {
            handler = new CaptureHandler(CaptureView.this);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public Handler getHandler() {
        return handler;
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = activity.getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    @Override
    public void onClick(View v) {
        final int id = v.getId();
        if (id == R.id.button_back){
            activity.finish();
        } else if (id == R.id.button_light){
            light(v);
        }
    }

    /**
     * 捕获成功的接口
     */
    public interface OnCaptureListener{
        public void onCapture(String result);
    }
}