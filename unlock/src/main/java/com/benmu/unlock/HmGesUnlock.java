package com.benmu.unlock;

import android.content.Intent;

import com.alibaba.weex.plugin.annotation.WeexModule;
import com.benmu.unlock.activity.CreateGestureActivity;
import com.benmu.unlock.activity.GestureLoginActivity;
import com.benmu.unlock.constant.Constant;
import com.benmu.unlock.utils.ACache;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

/**
 * Created by liangshuai on 2018/5/24.
 * WeexFrameworkWrapper
 * com.benmu.unlock
 */

@WeexModule(name = "hmModule", lazyLoad = true)
public class HmGesUnlock extends WXModule {
    private ACache aCache;

    @JSMethod
    public void addGesturePage(final JSCallback callback) {
        Intent intent = new Intent(new Intent(mWXSDKInstance.getContext(), CreateGestureActivity.class));
        intent.putExtra(Constant.CREATE_GESTURE_CALLBACK, callback);
        mWXSDKInstance.getContext().startActivity(intent);
    }

    @JSMethod
    public void checkGesturePage(final JSCallback callback) {
        Intent intent = new Intent(new Intent(mWXSDKInstance.getContext(), GestureLoginActivity.class));
        intent.putExtra(Constant.CHECK_GESTURE_CALLBACK, callback);
        mWXSDKInstance.getContext().startActivity(intent);
    }

    @JSMethod
    public void removeGesturePassword(final JSCallback callback) {
        aCache = ACache.get(mWXSDKInstance.getContext());
        boolean removeSuccess = aCache.remove(Constant.GESTURE_PASSWORD);
        callback.invoke(removeSuccess ? "success" : "failed");
    }
}
