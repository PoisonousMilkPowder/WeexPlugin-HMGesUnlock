package com.benmu.unlock.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;


import com.benmu.unlock.R;
import com.benmu.unlock.constant.Constant;
import com.benmu.unlock.utils.ACache;
import com.benmu.unlock.utils.LockPatternUtil;
import com.benmu.unlock.view.LockPatternView;
import com.taobao.weex.bridge.JSCallback;

import java.util.List;

public class GestureLoginActivity extends AppCompatActivity {
    private static final String TAG = "GestureLoginActivity";

    LockPatternView lockPatternView;
    TextView messageTv;

    private static final long DELAYTIME = 600l;
    private byte[] gesturePassword;
    private ACache aCache;

    JSCallback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_login);

        Intent intent = getIntent();
        callback = (JSCallback) intent.getSerializableExtra(Constant.CHECK_GESTURE_CALLBACK);

        init();
    }
    @Override
    public void onBackPressed() {
        //禁止返回键退出本页面
    }


    private void init() {
        lockPatternView = (LockPatternView) findViewById(R.id.lockPatternView_login);
        messageTv = (TextView) findViewById(R.id.messageTv);

        aCache = ACache.get(GestureLoginActivity.this);
        //得到当前用户的手势密码
        gesturePassword = aCache.getAsBinary(Constant.GESTURE_PASSWORD);


        lockPatternView.setOnPatternListener(patternListener);
        updateStatus(Status.DEFAULT);
    }

    private LockPatternView.OnPatternListener patternListener = new LockPatternView.OnPatternListener() {

        @Override
        public void onPatternStart() {
            lockPatternView.removePostClearPatternRunnable();
        }

        @Override
        public void onPatternComplete(List<LockPatternView.Cell> pattern) {
            if(pattern != null){
                if(LockPatternUtil.checkPattern(pattern, gesturePassword)) {
                    updateStatus(Status.CORRECT);
                } else {
                    updateStatus(Status.ERROR);
                }
            }
        }
    };

    /**
     * 更新状态
     * @param status
     */
    private void updateStatus(Status status) {
        messageTv.setText(status.strId);
        messageTv.setTextColor(getResources().getColor(status.colorId));
        switch (status) {
            case DEFAULT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case ERROR:
                lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
                lockPatternView.postClearPatternRunnable(DELAYTIME);
                break;
            case CORRECT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                loginGestureSuccess();
                break;
        }
    }

    /**
     * 手势登录成功（去首页）
     */
    private void loginGestureSuccess() {
        callback.invoke("success");
        finish();
    }

    /**
     * 忘记手势密码（去账号登录界面）
     */
    public void forgetGesturePasswrod(View view) {
        aCache.remove(Constant.GESTURE_PASSWORD);
        finish();
    }

    private enum Status {
        //默认的状态
        DEFAULT(R.string.gesture_default, R.color.white),
        //密码输入错误
        ERROR(R.string.gesture_error, R.color.red_f4333c),
        //密码输入正确
        CORRECT(R.string.gesture_correct, R.color.white);

        private Status(int strId, int colorId) {
            this.strId = strId;
            this.colorId = colorId;
        }
        private int strId;
        private int colorId;
    }
}
