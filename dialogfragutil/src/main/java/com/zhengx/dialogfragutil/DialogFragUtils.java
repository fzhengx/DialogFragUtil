package com.zhengx.dialogfragutil;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.util.HashMap;

/**
 * name：DialogFragmentUtils
 * class: DialogFragment工具类
 *  1.
 *  DialogFragUtils.DialogStyle thizDialogStyle = new DialogFragUtils.DialogStyle()
 *                 .cancelable(true) //点击是否可取消，默认true
 *                 .cancelableInTouchOutside(false) //点击外部是否可取消,默认true
 *                 .dontInterceptOutsideTouchEvent(true) //不拦截外部未被覆盖的区域的事件（也就是外部未覆盖的按钮等可点击）,默认false;该属性影响cancelableInTouchOutside
 *                 .setAnimationStyleResource(R.style.BottomAnimation) //动画
 *                 .setDimAmount(0.3f)//背景变暗程度,默认0.5
 *                 .setBackground(Color.WHITE)//纯色背景，默认白色
 *                 .setBackgroundCorner(20, 20, 0, 0)//纯色背景圆角,从左上顺时针方向设置
 *                 .setGravity(Gravity.BOTTOM, xOffset, yOffset)//位置，默认居中
 *                 .setWidth(ViewGroup.LayoutParams.WRAP_CONTENT)//宽度,默认80%宽度
 *                 .setHeight(300);//高度px
 *
 *         //设置某个dialogFragment类的样式，也可单独设置某个实例的样式
 *         DialogFragUtils.setUpDialogStyle(SampleDialog.class, thizDialogStyle);
 *  2
 *      show()用来显示(可以针对特定实例设置style)；
 *
 *      changeSize() 修改对话框长宽；
 *
 *      changeLocation（） 修改对话框位置；
 *
 *  注：---依赖lifecycle，仅适用support DialogFragment
 */
public class DialogFragUtils {

    private static final String TAG = DialogFragUtils.class.getSimpleName();

    public static boolean LoggerEnable = true;
    /**
     * 屏幕宽高
     */
    public static int screenWidth, screenHeight;
    /**
     * 对话框样式与Dialogfragment对应关系集合
     */
    private HashMap<String, DialogStyle> styleSet = new HashMap<>();

    private HashMap<String, DialogStyle> hashKeyStyleSet = new HashMap<>();

    private DialogFragUtils() {
    }

    private static DialogFragUtils get() {
        return Singleton.INSTANCE;
    }

    private static final class Singleton {
        private static DialogFragUtils INSTANCE = new DialogFragUtils();
    }

    /**
     * 初始化，注册生命周期监听
     *
     * @param application
     */
    protected static void init(Application application) {
        DisplayMetrics dm = application.getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;

        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                get().handleActivityLifecycle(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    /**
     * 设置dialog的样式，按照类名设置
     */
    public static void setUpDialogStyle(Class<? extends DialogFragment> clazz, DialogStyle dialogStyle) {
        get().styleSet.put(clazz.getName(), dialogStyle);
    }

    /**
     * 设置dialog的样式，按照具体实例tag设置,会覆盖类名设置
     */
    public static void setUpDialogStyle(DialogFragment dialogFragment, DialogStyle dialogStyle) {
        get().hashKeyStyleSet.put(dialogFragment.getTag(), dialogStyle);
    }

    /**
     * 默认tag为class.getName
     */
    public static DialogFragment show(FragmentManager fm, @NonNull DialogFragment dialogFragment) {

        return show(fm, dialogFragment, null, null);
    }

    public static DialogFragment show(FragmentManager fm, @NonNull DialogFragment dialogFragment,
                                      String tag) {

        return show(fm, dialogFragment, null, tag);
    }

    public static DialogFragment show(FragmentManager fm, @NonNull DialogFragment dialogFragment,
                                      DialogStyle style) {

        return show(fm, dialogFragment, style, null);
    }
    /**
     * 显示fragment，避免重复显示
     */
    public static DialogFragment show(FragmentManager fm, @NonNull DialogFragment dialogFragment,
                                       DialogStyle style, String tag) {

        String theTAG = (tag == null || tag.equals("")) ? dialogFragment.getClass().getName() : tag;

        if (!dialogFragment.isAdded() && null == fm.findFragmentByTag(theTAG)) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(dialogFragment, theTAG);
            ft.commitAllowingStateLoss();
            fm.executePendingTransactions();

            if (style != null) DialogFragUtils.setUpDialogStyle(dialogFragment, style);
        } else { //已经加载进容器里去了....
            //do nothing
        }

        return dialogFragment;
    }

    /**
     *根据dialogFragment的tag来 取消对话框
     */
    public static void dismissByTag(FragmentManager fm, @NonNull String tag) {

        DialogFragment dialogFragment = (DialogFragment) fm.findFragmentByTag(tag);
        if (dialogFragment != null) {
            dialogFragment.dismiss();
        }
    }

    /**
     * 在dialog显示状态下修改长宽
     *
     * @param dialogFragment
     * @param width          宽度
     * @param height         高度
     * @return
     */
    public static boolean changeSize(DialogFragment dialogFragment,
                                     int width, int height) {

        if (!dialogFragment.isVisible()) return false;

        Window window = dialogFragment.getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams p = window.getAttributes();

            p.width = width;
            p.height = height;
            window.setAttributes(p);

            updateDialogStyle(dialogFragment, p);
        } else {
            return false;
        }
        return true;
    }

    /**
     * 在dialog显示状态下修改位置
     *
     * @param dialogFragment
     * @param gravity        位置属性
     * @param xOffset        偏移量（根据gravity属性有不同）
     * @param yOffset
     * @return
     */
    public static boolean changeLocation(DialogFragment dialogFragment,
                                         int gravity, int xOffset, int yOffset) {

        if (!dialogFragment.isVisible()) return false;

        Window window = dialogFragment.getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams p = window.getAttributes();

            p.gravity = gravity;
            p.x = xOffset;
            p.y = yOffset;
            window.setAttributes(p);

            updateDialogStyle(dialogFragment, p);
        } else {
            return false;
        }
        return true;
    }

    private void handleActivityLifecycle(Activity activity) {
        if (activity instanceof FragmentActivity) {
            ((FragmentActivity) activity).getSupportFragmentManager()
                    .registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
                        @Override
                        public void onFragmentPreAttached(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull Context context) {
                            super.onFragmentPreAttached(fm, f, context);
                            debug(f, " on fragment pre attached");
                        }

                        @Override
                        public void onFragmentAttached(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull Context context) {
                            super.onFragmentAttached(fm, f, context);
                            debug(f, " on fragment attached");
                        }

                        @Override
                        public void onFragmentPreCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
                            super.onFragmentPreCreated(fm, f, savedInstanceState);
                            debug(f, " on fragment pre created");
                        }

                        @Override
                        public void onFragmentCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
                            super.onFragmentCreated(fm, f, savedInstanceState);
                            debug(f, " on fragment created");
                        }

                        @Override
                        public void onFragmentViewCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @NonNull View v, @Nullable Bundle savedInstanceState) {
                            onHandleFragmentPreViewCreated(fm, f);
                            super.onFragmentViewCreated(fm, f, v, savedInstanceState);
                            debug(f, " on fragment view created");
                        }

                        @Override
                        public void onFragmentActivityCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
                            super.onFragmentActivityCreated(fm, f, savedInstanceState);
                            debug(f, " on fragment activity created");
                        }

                        @Override
                        public void onFragmentStarted(@NonNull FragmentManager fm, @NonNull Fragment f) {
                            super.onFragmentStarted(fm, f);
                            debug(f, " on fragment fragment started");
                            onHandleFragmentStarted(fm, f);
                        }

                        @Override
                        public void onFragmentResumed(@NonNull FragmentManager fm, @NonNull Fragment f) {
                            super.onFragmentResumed(fm, f);
                            debug(f, " on fragment resumed");
                        }

                        @Override
                        public void onFragmentPaused(@NonNull FragmentManager fm, @NonNull Fragment f) {
                            super.onFragmentPaused(fm, f);
                            debug(f, " on fragment paused");
                        }

                        @Override
                        public void onFragmentStopped(@NonNull FragmentManager fm, @NonNull Fragment f) {
                            super.onFragmentStopped(fm, f);
                            debug(f, " on fragment stopped");
                        }

                        @Override
                        public void onFragmentViewDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
                            super.onFragmentViewDestroyed(fm, f);
                            debug(f, " on fragment view destroyed");
                        }

                        @Override
                        public void onFragmentDestroyed(@NonNull FragmentManager fm, @NonNull Fragment f) {
                            super.onFragmentDestroyed(fm, f);
                            debug(f, " on fragment destroyed");
                        }

                        @Override
                        public void onFragmentDetached(@NonNull FragmentManager fm, @NonNull Fragment f) {
                            super.onFragmentDetached(fm, f);
                            debug(f, " on fragment detached");
                        }
                    }, true);
        }
    }

    private void onHandleFragmentPreViewCreated(@NonNull FragmentManager fm, @NonNull Fragment f) {
        if (f instanceof DialogFragment) {
            DialogStyle dialogStyle = null;
            if ((dialogStyle = getStyle(f)) != null) {
                DialogFragment dialogFragment = (DialogFragment) f;
                debug(f, " 设置整体样式");
                //是否去除标题栏
                if (dialogStyle.featureNoTitle && dialogFragment.getDialog() != null) {
                    dialogFragment.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
                }
                //背景设置
                if (dialogFragment.getDialog() != null && dialogFragment.getDialog().getWindow() != null) {
                    if (dialogStyle.bgDrawable != null) {
                        dialogFragment.getDialog().getWindow()
                                .setBackgroundDrawable(dialogStyle.bgDrawable);
                    } else {
                        float[] outerCorners = new float[] {
                                dialogStyle.cornerLT, dialogStyle.cornerLT,
                                dialogStyle.cornerRT, dialogStyle.cornerRT,
                                dialogStyle.cornerRB, dialogStyle.cornerRB,
                                dialogStyle.cornerLB, dialogStyle.cornerLB
                        };
                        RoundRectShape roundRectShape
                                = new RoundRectShape(outerCorners, null, null);
                        ShapeDrawable bgDrawable = new ShapeDrawable(roundRectShape);
                        //指定填充颜色
                        bgDrawable.getPaint().setColor(dialogStyle.bgColor);
                        // 指定填充模式
                        bgDrawable.getPaint().setStyle(Paint.Style.FILL);

                        dialogFragment.getDialog().getWindow()
                                .setBackgroundDrawable(bgDrawable);
                    }
                }
            }
        }
    }

    /*
    * 设置宽高以及位置,设置fragment的大小
    * */
    private void onHandleFragmentStarted(@NonNull FragmentManager fm, @NonNull Fragment f) {
        if (f instanceof DialogFragment) {
            DialogFragment dialogFragment = (DialogFragment) f;

            DialogStyle dialogStyle = getStyle(f);

            Window window = dialogFragment.getDialog().getWindow();
            if (dialogStyle != null && window != null) {
                debug(f, "设置宽高，对齐方式");
                WindowManager.LayoutParams p = window.getAttributes();
                p.width = dialogStyle.width;
                p.height = dialogStyle.height;

                p.x = dialogStyle.xOffset;
                p.y = dialogStyle.yOffset;

                window.setGravity(dialogStyle.gravity);

                //动画
                if (dialogStyle.animationStyleResource != 0) {
                    window.setWindowAnimations(dialogStyle.animationStyleResource);
                }
                //背景变暗设置
                p.dimAmount = dialogStyle.dimAmount;
                //外部事件是否可点击
                if (dialogStyle.dontInterceptOutsideTouchEvent) {
                    p.flags = p.flags | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
                } else {
                    p.flags = p.flags & ~WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
                }

                window.setAttributes(p);

                dialogFragment.getDialog().setCancelable(dialogStyle.cancelable);
                dialogFragment.getDialog().setCanceledOnTouchOutside(dialogStyle.cancelableInTouchOutside);
            }
        }
    }

    private DialogStyle getStyle(Fragment f) {
        DialogStyle style = styleSet.get(f.getClass().getName());
        if (hashKeyStyleSet.get(f.getTag()) != null) style = hashKeyStyleSet.get(f.getTag());
        return style;
    }

    private static void updateDialogStyle(DialogFragment dialogFragment, WindowManager.LayoutParams p) {
        DialogStyle style;
        if ( (style = get().getStyle(dialogFragment)) != null) {
            style = style.copy();
        } else {
            style = new DialogStyle();
        }
        style.gravity = p.gravity;
        style.xOffset = p.x;
        style.yOffset = p.y;
        style.width = p.width;
        style.height = p.height;
        setUpDialogStyle(dialogFragment, style);
    }

    /**
     * dialog fragment属性样式
     */
    public static final class DialogStyle {
        /**
         * 无标题标记
         */
        private boolean featureNoTitle = true;

        /**
         * 背景变暗程度(0~1.0f,默认0.5f)
         */
        private float dimAmount = 0.5f;

        /**
         * 显示消失时的动画
         */
        private int animationStyleResource;

        /**
         * 默认拦截外部未覆盖区域事件
         */
        private boolean dontInterceptOutsideTouchEvent;

        /**
         * 颜色背景
         */
        private int bgColor = Color.WHITE;

        /**
         * 圆角设置，单位为px
         */
        private float cornerLT, cornerRT, cornerLB, cornerRB;

        /**
         * 样式背景
         */
        private Drawable bgDrawable;

        /**
         * 宽高，可以使用LayoutParam.MatchParent等属性
         */
        private int width = (int) (screenWidth * 0.8);
        private int height = ViewGroup.LayoutParams.WRAP_CONTENT;

        /**
         * 对齐模式，默认居中对齐
         */
        private int gravity = Gravity.CENTER;

        /**
         * 根据对齐方式设置的偏移量
         */
        private int xOffset, yOffset;
        /**
         * 可否被取消
         */
        private boolean cancelable = true;

        /**
         * 点击外部可否取消
         */
        private boolean cancelableInTouchOutside = true;

        public DialogStyle() {
        }

        public DialogStyle showTitle(boolean isShowTitle) {
            this.featureNoTitle = isShowTitle;
            return this;
        }

        public DialogStyle setDimAmount(float dimAmount) {
            this.dimAmount = dimAmount;
            return this;
        }

        public DialogStyle setAnimationStyleResource(int animationStyleResourceId) {
            this.animationStyleResource = animationStyleResourceId;
            return this;
        }

        public DialogStyle dontInterceptOutsideTouchEvent(boolean dontInterceptOutsideTouchEvent) {
            this.dontInterceptOutsideTouchEvent = dontInterceptOutsideTouchEvent;
            return this;
        }

        public DialogStyle cancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public DialogStyle cancelableInTouchOutside(boolean cancelableInTouchOutside) {
            this.cancelableInTouchOutside = cancelableInTouchOutside;
            return this;
        }

        public DialogStyle setBackground(int color) {
            this.bgColor = color;
            return this;
        }

        public DialogStyle setBackgroundCorner(float cornerPx) {
            this.cornerRT = cornerPx;
            this.cornerLT = cornerPx;
            this.cornerLB = cornerPx;
            this.cornerRB = cornerPx;
            return this;
        }

        public DialogStyle setBackgroundCorner(float cornerLTPx, float cornerRTPx,
                                               float cornerRBPx, float cornerLBPx) {

            this.cornerRT = cornerRTPx;
            this.cornerLT = cornerLTPx;
            this.cornerLB = cornerLBPx;
            this.cornerRB = cornerRBPx;
            return this;
        }

        public DialogStyle setBackground(Drawable drawable) {
            this.bgDrawable = drawable;
            return this;
        }

        public DialogStyle setWidth(int width) {
            this.width = width;
            return this;
        }

        public DialogStyle setHeight(int height) {
            this.height = height;
            return this;
        }

        public DialogStyle setGravity(int gravity, int xOffset, int yOffset) {
            this.gravity = gravity;
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            return this;
        }

        public DialogStyle copy() {
            DialogStyle style = new DialogStyle();
            style.featureNoTitle = featureNoTitle;
            style.dimAmount = dimAmount;
            style.animationStyleResource = animationStyleResource;
            style.dontInterceptOutsideTouchEvent = dontInterceptOutsideTouchEvent;
            style.bgColor = bgColor;
            style.bgDrawable = bgDrawable;
            style.cancelable = cancelable;
            style.cancelableInTouchOutside = cancelableInTouchOutside;

            style.width = width;
            style.height = height;
            style.gravity = gravity;
            style.xOffset = xOffset;
            style.yOffset = yOffset;
            style.cornerRT = cornerRT;
            style.cornerLT = cornerLT;
            style.cornerLB = cornerLB;
            style.cornerRB = cornerRB;
            return style;
        }
    }

    private void debug(Fragment fragment, String msg) {
        if (LoggerEnable && fragment instanceof DialogFragment)
            Log.i(TAG, fragment.getClass().getSimpleName() + ": " + msg);
    }
}
