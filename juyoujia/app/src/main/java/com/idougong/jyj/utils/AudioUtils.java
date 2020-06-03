package com.idougong.jyj.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.idougong.jyj.common.net.EnvConstant;


public class AudioUtils {
    private static AudioUtils audioUtils;
    private SpeechSynthesizer mySynthesizer;

    public AudioUtils() {

    }

    public static AudioUtils getInstance() {
        if (audioUtils == null) {
            synchronized (AudioUtils.class) {
                if (audioUtils == null) {
                    audioUtils = new AudioUtils();
                }
            }
        }
        return audioUtils;
    }

    private InitListener myInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
        }
    };

    public void init(Context context) {
        mySynthesizer = SpeechSynthesizer.createSynthesizer(context, myInitListener);
        mySynthesizer.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
        mySynthesizer.setParameter(SpeechConstant.PITCH, "50");
        mySynthesizer.setParameter(SpeechConstant.VOLUME, "50");
    }

    public void speakText(String content) {
        int code = mySynthesizer.startSpeaking(content, new SynthesizerListener() {

            @Override
            public void onSpeakBegin() {

            }

            @Override
            public void onBufferProgress(int i, int i1, int i2, String s) {

            }

            @Override
            public void onSpeakPaused() {

            }

            @Override
            public void onSpeakResumed() {

            }

            @Override
            public void onSpeakProgress(int i, int i1, int i2) {

            }

            @Override
            public void onCompleted(SpeechError speechError) {
                Log.i("AudioUtils", "-----AudioUtils播放完成-----");
            }

            @Override
            public void onEvent(int i, int i1, int i2, Bundle bundle) {

            }
        });
    }
}
