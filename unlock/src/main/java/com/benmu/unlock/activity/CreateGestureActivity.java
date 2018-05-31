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
import com.benmu.unlock.view.LockPatternIndicator;
import com.benmu.unlock.view.LockPatternView;
import com.taobao.weex.bridge.JSCallback;

import java.util.ArrayList;
import java.util.List;

public class CreateGestureActivity extends AppCompatActivity {

    LockPatternIndicator lockPatternIndicator;
    LockPatternView lockPatternView;
    TextView messageTv;

    private ACache aCache;
    private static final long DELAYTIME = 600L;
    private static final String TAG = "CreateGestureActivity";
    private List<LockPatternView.Cell> mChosenPattern = null;
    JSCallback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_gesture);

        Intent intent = getIntent();
        ArrayList callbackList = (ArrayList) intent.getSerializableExtra(Constant.CREATE_GESTURE_CALLBACK);
        callback = (JSCallback) callbackList.get(0);

        init();
    }

    private void init() {
        lockPatternIndicator = (LockPatternIndicator) findViewById(R.id.lockPatterIndicator);
        lockPatternView = (LockPatternView) findViewById(R.id.lockPatternView_create);
        messageTv = (TextView) findViewById(R.id.messageTv);

        aCache = ACache.get(CreateGestureActivity.this);
        lockPatternView.setOnPatternListener(patternListener);
    }

    /**
     * 手势监听
     */
    private LockPatternView.OnPatternListener patternListener = new LockPatternView.OnPatternListener() {

        @Override
        public void onPatternStart() {
            lockPatternView.removePostClearPatternRunnable();
            lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
        }

        @Override
        public void onPatternComplete(List<LockPatternView.Cell> pattern) {
            if(mChosenPattern == null && pattern.size() >= 4) {
                mChosenPattern = new ArrayList<LockPatternView.Cell>(pattern);
                updateStatus(Status.CORRECT, pattern);
            } else if (mChosenPattern == null && pattern.size() < 4) {
                updateStatus(Status.LESSERROR, pattern);
            } else if (mChosenPattern != null) {
                if (mChosenPattern.equals(pattern)) {
                    updateStatus(Status.CONFIRMCORRECT, pattern);
                } else {
                    updateStatus(Status.CONFIRMERROR, pattern);
                }
            }
        }
    };

    /**
     * 更新状态
     * @param status
     * @param pattern
     */
    private void updateStatus(Status status, List<LockPatternView.Cell> pattern) {
        messageTv.setTextColor(getResources().getColor(status.colorId));
        messageTv.setText(status.strId);
        switch (status) {
            case DEFAULT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case CORRECT:
                updateLockPatternIndicator();
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case LESSERROR:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case CONFIRMERROR:
                lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
                lockPatternView.postClearPatternRunnable(DELAYTIME);
                break;
            case CONFIRMCORRECT:
                saveChosenPattern(pattern);
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                setLockPatternSuccess();
                break;
        }
    }

    /**
     * 更新 Indicator
     */
    private void updateLockPatternIndicator() {
        if (mChosenPattern == null)
            return;
        lockPatternIndicator.setIndicator(mChosenPattern);
    }

    /**
     * 重新设置手势
     */
    public void resetLockPattern(View view) {
        mChosenPattern = null;
        lockPatternIndicator.setDefaultIndicator();
        updateStatus(Status.DEFAULT, null);
        lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
    }

    /**
     * 成功设置了手势密码(跳到首页)
     */
    private void setLockPatternSuccess() {
        callback.invoke("success");
        finish();
    }

    /**
     * 保存手势密码
     */
    private void saveChosenPattern(List<LockPatternView.Cell> cells) {
        byte[] bytes = LockPatternUtil.patternToHash(cells);
        aCache.put(Constant.GESTURE_PASSWORD, bytes);
    }

    private enum Status {
        //默认的状态，刚开始的时候（初始化状态）
        DEFAULT(R.string.create_gesture_default, R.color.white),
        //第一次记录成功
        CORRECT(R.string.create_gesture_correct, R.color.white),
        //连接的点数小于4（二次确认的时候就不再提示连接的点数小于4，而是提示确认错误）
        LESSERROR(R.string.create_gesture_less_error, R.color.red_f4333c),
        //二次确认错误
        CONFIRMERROR(R.string.create_gesture_confirm_error, R.color.red_f4333c),
        //二次确认正确
        CONFIRMCORRECT(R.string.create_gesture_confirm_correct, R.color.white);

        private Status(int strId, int colorId) {
            this.strId = strId;
            this.colorId = colorId;
        }
        private int strId;
        private int colorId;
    }
}
