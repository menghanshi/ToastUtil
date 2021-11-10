package com.myq.toastutils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;


/**
 * Created by zhoulq on 16/8/29.
 *
 * Toast工具类
 */
public class ToastUtil {

    /**
     * 普通Toast
     *
     * @param context
     * @param message
     */
    public static void showToast(final Context context, final String message) {
        if(context == null || TextUtils.isEmpty(message))
            return;
        if (isRunInMainThread()) {
            RoundToast.showBottomTips(context.getApplicationContext(), message);
        } else {
            post(context, new Runnable() {
                @Override
                public void run() {
                    RoundToast.showBottomTips(context.getApplicationContext(), message);
                }
            });
        }
    }

    /**
     * 普通Toast
     * @param context
     * @param message
     * @param offsetY
     */
    public static void showToast(final Context context, final String message, final int offsetY) {
        if(context == null || TextUtils.isEmpty(message))
            return;

        if (isRunInMainThread()) {
            RoundToast.showBottomTips(context.getApplicationContext(), message, offsetY);
        } else {
            post(context, new Runnable() {
                @Override
                public void run() {
                    RoundToast.showBottomTips(context.getApplicationContext(), message, offsetY);
                }
            });
        }
    }

    /**
     * 居中Toast
     *
     * @param context
     * @param message
     */
    public static void showCenterToast(final Context context, final String message) {
        if(context == null || TextUtils.isEmpty(message))
            return;
        if (isRunInMainThread()) {
            RoundToast.showCenterTips(context.getApplicationContext(), message);
        } else {
            post(context, new Runnable() {
                @Override
                public void run() {
                    RoundToast.showBottomTips(context.getApplicationContext(), message);
                }
            });
        }
    }

    /**
     * 需要个性化的元素包括：文案，文案颜色，提示条背景颜色
     *
     * @param context 上下文，建议使用Application Context
     * @param text    文案
     */
    public static void showHeaderToast(final Context context, final CharSequence text) {
        if(context == null || TextUtils.isEmpty(text))
            return;
        if (isRunInMainThread()) {
            HeaderToast.makeToast(context.getApplicationContext(), text).show();
        } else {
            post(context, new Runnable() {
                @Override
                public void run() {
                    HeaderToast.makeToast(context.getApplicationContext(), text).show();
                }
            });
        }
    }

    /**
     * 需要个性化的元素包括：文案，文案颜色，提示条背景颜色，展示时长，是否展示关闭图标
     *
     * @param context    上下文, 建议使用Application Context
     * @param text       文案
     * @param duration   展示时间，单位毫秒
     * @param isShowIcon 是否展示关闭图标
     */
    public static void showHeaderToast(final Context context, final CharSequence text, final long duration, final boolean isShowIcon) {
        if(context == null || TextUtils.isEmpty(text))
            return;
        if (isRunInMainThread()) {
            HeaderToast.makeToast(context.getApplicationContext(), text, duration, isShowIcon).show();
        } else {
            post(context, new Runnable() {
                @Override
                public void run() {
                    HeaderToast.makeToast(context.getApplicationContext(), text, duration, isShowIcon).show();
                }
            });
        }
    }

    /**
     * 需要个性化的元素包括：文案，文案颜色，提示条背景颜色
     *
     * @param context              上下文, 建议使用Application Context
     * @param text                 文案
     * @param textColorResId       文案颜色resource id
     * @param backgroundColorResId 背景颜色resource id
     */
    public static void showHeaderToast(final Context context, final CharSequence text, final int textColorResId, final int backgroundColorResId) {
        if(context == null || TextUtils.isEmpty(text))
            return;
        if (isRunInMainThread()) {
            HeaderToast.makeToast(context.getApplicationContext(), text, textColorResId, backgroundColorResId).show();
        } else {
            post(context, new Runnable() {
                @Override
                public void run() {
                    HeaderToast.makeToast(context.getApplicationContext(), text, textColorResId, backgroundColorResId).show();
                }
            });
        }
    }

    private static boolean isRunInMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /**
     * 在主线程执行runnable
     */
    private static boolean post(Context context, Runnable runnable) {
        return getHandler(context).post(runnable);
    }

    /**
     * 获取主线程的handler
     */
    private static Handler getHandler(Context context) {
        //获得主线程的looper
        Looper mainLooper = context.getMainLooper();
        //获取主线程的handler
        return new Handler(mainLooper);
    }
}
