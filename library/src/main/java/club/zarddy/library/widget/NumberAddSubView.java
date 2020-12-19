package club.zarddy.library.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.TintTypedArray;

import club.zarddy.library.R;
import club.zarddy.library.util.ScreenUtils;

public class NumberAddSubView extends LinearLayout implements View.OnClickListener {

    private Button btnSub, btnAdd;
    private TextView txtNumber;

    /** 设置默认值 */
    private int mValue = 1;
    private int mMinValue = 1;
    private int mMaxValue = 10;
    private int mButtonSize = 28;

    private boolean mUnlimited = false; // 是否不限制最大值

    private OnButtonClickListener onButtonClickListener;

    public NumberAddSubView(Context context) {
        this(context, null);
    }

    public NumberAddSubView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("RestrictedApi")
    public NumberAddSubView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
        //得到属性
        if (attrs != null) {
            TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.NumberAddSubView, defStyleAttr, 0);
            int value = a.getInt(R.styleable.NumberAddSubView_nasv_value, 0);
            setValue(value);
            // 最小值
            int minValue = a.getInt(R.styleable.NumberAddSubView_nasv_minValue, mMinValue);
            setMinValue(minValue);
            // 最大值
            int maxValue = a.getInt(R.styleable.NumberAddSubView_nasv_maxValue, mMaxValue);
            setMaxValue(maxValue);
            // 是否不限制最大值
            mUnlimited = a.getBoolean(R.styleable.NumberAddSubView_nasv_unlimited, false);
            // 按钮大小
            mButtonSize = a.getDimensionPixelSize(R.styleable.NumberAddSubView_nasv_buttonSize, ScreenUtils.dip2px(getContext(), mButtonSize));

            ViewGroup.LayoutParams lp = btnSub.getLayoutParams();
            lp.width = mButtonSize;
            lp.height = mButtonSize;
            btnSub.setLayoutParams(lp);
            btnAdd.setLayoutParams(lp);

            Drawable btnSubBackground = a.getDrawable(R.styleable.NumberAddSubView_nasv_btnSubBackground);
            if (btnSubBackground != null)
                btnSub.setBackground(btnSubBackground);
            Drawable btnAddBackground = a.getDrawable(R.styleable.NumberAddSubView_nasv_btnAddBackground);
            if (btnAddBackground != null)
                btnAdd.setBackground(btnAddBackground);
            Drawable textViewBackground = a.getDrawable(R.styleable.NumberAddSubView_nasv_textViewBackground);
            if (textViewBackground != null)
                txtNumber.setBackground(textViewBackground);

            a.recycle();
        }
    }

    @Override
    public void onClick(View view) {
        boolean addValue = true; // 加/减操作

        if (R.id.button_sub == view.getId()) { // 减
            subNum();
            addValue = false;

        } else if (R.id.button_add == view.getId()) { // 加
            addNum();
            addValue = true;
        }

        if (onButtonClickListener != null) {
            onButtonClickListener.onValueChanged(view, mValue, addValue);
        }
    }

    private void initView(Context context) {
        //第三个参数：把当前View加载到NumberAddSubView控件上
        View.inflate(context, R.layout.widget_number_add_sub_view, this);
        btnSub = (Button) findViewById(R.id.button_sub); // 减
        btnAdd = (Button) findViewById(R.id.button_add); // 加
        txtNumber = (TextView) findViewById(R.id.txt_number); // 数量

        btnSub.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
    }

    public int getValue() {
        String val = txtNumber.getText().toString();
        if (!TextUtils.isEmpty(val)) {
            mValue = Integer.parseInt(val);
        }
        return mValue;
    }

    public void setValue(int value) {
        this.mValue = value;
        txtNumber.setText(String.valueOf(value));
    }

    public int getMinValue() {
        return mMinValue;
    }

    public void setMinValue(int minValue) {
        this.mMinValue = minValue;
    }

    public int getMaxValue() {
        return mMaxValue;
    }

    public void setMaxValue(int maxValue) {
        this.mMaxValue = maxValue;
    }

    public boolean isUnlimited() {
        return mUnlimited;
    }

    public void setUnlimited(boolean unlimited) {
        this.mUnlimited = unlimited;
    }

    /**
     * 减少数据
     */
    private void subNum() {
        if (mValue > mMinValue) {
            mValue = mValue - 1;
            txtNumber.setText(String.valueOf(mValue));
        }
    }

    /**
     * 添加数据
     */
    private void addNum() {
        if (mUnlimited || mValue < mMaxValue) {
            mValue = mValue + 1;
            txtNumber.setText(String.valueOf(mValue));
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        btnSub.setEnabled(enabled);
        btnAdd.setEnabled(enabled);
        int colorResId = enabled ? R.color.common_text : R.color.gray_acacac;
        txtNumber.setTextColor(this.getContext().getResources().getColor(colorResId));
    }

    public interface OnButtonClickListener {
        /**
         * 价格修改事件
         * @param view 按钮控件
         * @param newValue 当前最新值
         * @param addValue 加/减值
         */
        void onValueChanged(View view, int newValue, boolean addValue);
    }

    public void setOnButtonClickListener(OnButtonClickListener onButtonClickListener) {
        this.onButtonClickListener = onButtonClickListener;
    }
}
