
#### 介绍
    android DialogFragment 显示样式设置工具，依赖lifecycle
    **只支持support包的DialogFragment**

#### 安装

1. project目录
    ```
    allprojects {
        repositories {
            //...
            maven { url "https://jitpack.io" }
        }
    }
    ```
2. module目录
    `implementation 'com.github.fzhengx:DialogFragUtil:1.0.1`
    
    或者如果v4包冲突
    ```
    implementation("com.github.fzhengx:DialogFragUtil:1.0.1") {
        exclude group: 'com.android.support', module: 'support-v4'
    }
    ```
    
    android x:
    `implementation 'com.github.fzhengx:DialogFragUtil:androidx-1.0.1`
#### 使用说明
1. 样式设置
    ```
    DialogFragUtils.LoggerEnable = false;//默认开启日志显示

    DialogFragUtils.DialogStyle thizDialogStyle = new DialogFragUtils.DialogStyle()
                .cancelable(true) //点击是否可取消，默认true
                .cancelableInTouchOutside(false) //点击外部是否可取消,默认true
                //不拦截外部未被覆盖的区域的事件（也就是外部未覆盖的按钮等可点击）,默认false;该属性影响cancelableInTouchOutside
                .dontInterceptOutsideTouchEvent(true) 
                .setAnimationStyleResource(R.style.BottomAnimation) //动画
                .setDimAmount(0.3f)//背景变暗程度,默认0.5
                .setBackground(Color.WHITE)//纯色背景，默认白色
                .setBackgroundCorner(20, 20, 0, 0)//纯色背景圆角,从左上顺时针方向设置
                .setGravity(Gravity.BOTTOM, xOffset, yOffset)//位置，默认居中
                .setWidth(ViewGroup.LayoutParams.WRAP_CONTENT)//宽度,默认80%宽度
                .setHeight(300);//高度px

        //设置某个dialogFragment类的样式，也可单独设置某个实例的样式
        DialogFragUtils.setUpDialogStyle(SampleDialog.class, thizDialogStyle);
    ```
2. 显示（非必要）
    ```
    DialogFragUtils.show(fm, sampleDialog, showTag);//默认showTag 为类名
    
    DialogFragUtils.show(fm, sampleDialog, style, showTag); //style为dialog设置显示样式
    
    //修改对话框长宽
    DialogFragUtils.changeSize(DialogFragment dialogFragment, int width, int height)
    
    //修改对话框位置
    DialogFragUtils.changeLocation(DialogFragment dialogFragment,
                                         int gravity, int xOffset, int yOffset)
    ```