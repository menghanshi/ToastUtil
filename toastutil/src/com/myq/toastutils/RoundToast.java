package com.myq.toastutils;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;


/**
 * 自定义布局的圆角toast替代super toast，因为super toast基于WindowManager实现，在高版本的MIUI上需要“悬浮窗”权限。
 *
 * Created by zhoulq on 16/8/9.
 */
public class RoundToast {

    private static Toast toast;

    public static void showCenterTips(Context context, String txt) {
        if(context == null || TextUtils.isEmpty(txt))
            return;
        handleShowTipsEvents(context,txt, Gravity.CENTER, 0 , 0);
    }

    public static void showBottomTips(Context context , String txt) {
        if(context == null || TextUtils.isEmpty(txt))
            return;
        WeakReference<Context> weakReference=new WeakReference<Context>(context);
        float vMargin = weakReference.get().getResources().getDimension(R.dimen.ds71_v2);
        handleShowTipsEvents(weakReference.get(),txt, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0 , (int) vMargin);
    }

    public static void showBottomTips(Context context, String txt, int offsetY) {
        if(context == null || TextUtils.isEmpty(txt))
            return;
        float vMargin = context.getResources().getDimension(R.dimen.ds71_v2);
        handleShowTipsEvents(context, txt, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, (int) vMargin + offsetY);
    }

    /**
     * 自定义ui toast
     */
    private static void handleShowTipsEvents(Context context, String txt, int gravity, int hMargin, int vMargin) {
        if (toast != null) {
            toast.cancel();
        }
        toast = genToast(context, txt, gravity, hMargin, vMargin);
        toast.show();
    }

    private static Toast genToast(Context context, String txt, int gravity, int hMargin, int vMargin) {
        WeakReference<Context> weakReference=new WeakReference<Context>(context);
        LayoutInflater inflater = LayoutInflater.from(weakReference.get());
        View view = inflater.inflate(R.layout.vp_toast_text, null);
        TextView contentTxt = (TextView) view.findViewById(R.id.toast_content);
        if (!TextUtils.isEmpty(txt)) {
            contentTxt.setText(txt);
        }


        Toast toast = new Toast(weakReference.get());
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(gravity, hMargin, vMargin);
        return toast;
    }
}
