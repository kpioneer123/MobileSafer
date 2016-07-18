package com.cloudhome.mobilesafer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 开始就有焦点
 * Created by xionghu on 2016/7/15.
 * Email：965705418@qq.com
 */
public class FocusedTextView extends TextView {


    /**通常在代码实例化的时候用到
     * @param context
     */
    public FocusedTextView(Context context) {
        super(context);
    }


    /**在Android系统中，我们布局文件使用某个控件，默认会调用带有两个参数的构造方法
     * @param context
     * @param attrs
     */
    public FocusedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    /**设置样式的时候
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public FocusedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**让android系统以获取焦点的方式去处理事务
     * @return
     */
    @Override
    public boolean isFocused() {
        return true;
    }
}
