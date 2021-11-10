package com.myq.toastutils;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 顶部展示的Toast。
 *
 * 基于系统Toast实现，通过反射调用Toast的方法来实现自定义View，加入点击事件，修改展示时间等。并且兼容高版本系统及MIUI，不需要引入额外的权限。
 *
 * Created by zhoulq on 16/8/29.
 */
public class HeaderToast {
    private Toast mToast;
    private Context mContext;
    private View mView;
    private RelativeLayout mToastLayout;
    private TextView mTipTextView;
    private ImageView mCloseImageView;
    private Object mTN;
    private Method mShowMethod;
    private Method mHideMethod;

    private boolean mIsShowIcon = false;    // 是否有关闭的ICON
    private long mDuration = 1500;  // 展示的时长
    private CharSequence mText = "";    // 提示文字
    private int mBackgroundResId = 0;
    private int mTextColorResId = 0;

    private boolean isShowing = false;   // 是否正在展示

    private Handler mHandler = new Handler();

    public HeaderToast(Context context) {
        mContext = context;
        if(null == mToast){
            mToast = new Toast(mContext);
        }

        LayoutInflater inflate = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflate.inflate(R.layout.vp_view_header_toast, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mView.setLayoutParams(lp);
        mToastLayout = (RelativeLayout) mView.findViewById(R.id.toastLayout);
        mTipTextView = (TextView) mView.findViewById(R.id.messageTextView);
        mCloseImageView = (ImageView) mView.findViewById(R.id.closeImageView);
        mCloseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHandler.post(hideRunnable);
            }
        });
    }

    public static HeaderToast makeToast(Context context, CharSequence text){
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        HeaderToast headerToast = new HeaderToast(context);
        headerToast.mToast = toast;
        headerToast.mText = text;
        return headerToast;
    }

    public static HeaderToast makeToast(Context context, CharSequence text, long duration, boolean isShowIcon){
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        HeaderToast headerToast = new HeaderToast(context);
        headerToast.mToast = toast;
        headerToast.mText = text;
        headerToast.mDuration = duration;
        headerToast.mIsShowIcon = isShowIcon;
        return headerToast;
    }

    public static HeaderToast makeToast(Context context, CharSequence text, int textColorResId, int backgroundColorResId){
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        HeaderToast headerToast = new HeaderToast(context);
        headerToast.mToast = toast;
        headerToast.mText = text;
        headerToast.mTextColorResId = textColorResId;
        headerToast.mBackgroundResId = backgroundColorResId;
        return headerToast;
    }

    public void show(){
        if(isShowing){
            return;
        }

        if(mBackgroundResId != 0){
            mToastLayout.setBackgroundResource(mBackgroundResId);   // 设置背景颜色
        }

        if(!TextUtils.isEmpty(mText)){
            mTipTextView.setText(mText);                            // 设置展示提示文案
        }

        if(mTextColorResId != 0){
            mTipTextView.setTextColor(mContext.getResources().getColor(mTextColorResId));   // 设置文字颜色
        }
        if(mIsShowIcon){                                        // 判断是否展示ICON
            mCloseImageView.setVisibility(View.VISIBLE);
        }else {
            mCloseImageView.setVisibility(View.GONE);
        }
        mToast.setView(mView);

        initTN();
        try {
            mShowMethod.invoke(mTN);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }

        isShowing = true;
        mHandler.postDelayed(hideRunnable, mDuration);
    }

    private Runnable hideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    public void hide(){
        if(!isShowing){
            return;
        }

        try {
            mHideMethod.invoke(mTN);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }

        isShowing = false;
    }

    private void initTN() {
        try {
            Field tnField = mToast.getClass().getDeclaredField("mTN");
            tnField.setAccessible(true);
            mTN = tnField.get(mToast);
            mShowMethod = mTN.getClass().getMethod("show");
            mHideMethod = mTN.getClass().getMethod("hide");

            Field tnParamsField = mTN.getClass().getDeclaredField("mParams");
            tnParamsField.setAccessible(true);
            WindowManager.LayoutParams mLayoutParams = (WindowManager.LayoutParams) tnParamsField.get(mTN);
            mLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;  // 设置MATCH_PARENT使宽度占满屏幕
            mLayoutParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

            /**设置动画*/
            mLayoutParams.windowAnimations = android.R.style.Animation_Dialog; // 使用了系统的滑动动画效果，还可以使用Dialog等

            /**调用tn.show()之前一定要先设置mNextView*/
            Field tnNextViewField = mTN.getClass().getDeclaredField("mNextView");
            tnNextViewField.setAccessible(true);
            tnNextViewField.set(mTN, mToast.getView());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setGravity(Gravity.LEFT | Gravity.TOP,0 ,0);
    }

    public void setGravity(int gravity, int xOffset, int yOffset){
        mToast.setGravity(gravity, xOffset, yOffset);
    }

    public void setDuration(long duration){
        mDuration = duration;
    }

    public void showIcon(boolean isShowIcon){
        mIsShowIcon = isShowIcon;
    }

    public void setBackgroundColor(int resId){
        mBackgroundResId = resId;
    }
    public void setTextColor(int resId){
       mTextColorResId = resId;
    }
}
