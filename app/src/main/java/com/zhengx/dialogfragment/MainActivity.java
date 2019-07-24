package com.zhengx.dialogfragment;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zhengx.dialogfragutil.DialogFragUtils;

public class MainActivity extends AppCompatActivity {


    SampleDialog oneDialog;

    static {
        DialogFragUtils.DialogStyle thizDialogStyle = new DialogFragUtils.DialogStyle()
                .cancelable(true) //点击是否可取消
                .cancelableInTouchOutside(false) //点击外部是否可取消
                .dontInterceptOutsideTouchEvent(true) //不拦截外部未被覆盖的区域的事件（也就是外部未覆盖的按钮等可点击）
                .setAnimationStyleResource(R.style.BottomAnimation) //动画
//                .setDimAmount(0.3f)//背景变暗程度
                .setBackground(Color.WHITE)//纯色背景，默认白色
                .setBackgroundCorner(20, 20, 0, 0)//纯色背景圆角
//                .setGravity(Gravity.BOTTOM, 0, 0)//位置，默认居中
//                .setWidth(ViewGroup.LayoutParams.WRAP_CONTENT)//宽度,默认80%宽度
                .setHeight(300);//高度
                ;

        DialogFragUtils.setUpDialogStyle(SampleDialog.class, thizDialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.textview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneDialog = SampleDialog.show(getSupportFragmentManager());
            }
        });
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SampleDialog.dismiss(getSupportFragmentManager());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
