package club.zarddy.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import club.zarddy.library.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 自动换行的RadioGroup
 */
public class FlowRadioGroup extends RadioGroup {

    private List<List<View>> mAllViews = new ArrayList<>(); // 保存所有行的所有View
    private List<Integer> mLineHeight = new ArrayList<>(); // 保存每一行的行高
    private List<View> mLineViews = new ArrayList<>(); // 保存每一行的控件
    private int margin;

    public FlowRadioGroup(Context context) {
        this(context, null);
    }

    public FlowRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.HORIZONTAL);
        margin = ScreenUtils.dip2px(context, 6);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);

        int width = 0, height = 0;
        int lineWidth = 0, lineHeight = 0;

        mAllViews.clear();
        mLineHeight.clear();
        mLineViews.clear();

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }

            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            int childWidth = child.getMeasuredWidth() + margin * 2;
            int childHeight = child.getMeasuredHeight() + margin * 2;

            if (lineWidth + childWidth > sizeWidth - getPaddingLeft() - getPaddingRight()) {
                width = Math.max(width, lineWidth);
                height += lineHeight;
                mLineHeight.add(lineHeight);
                mAllViews.add(mLineViews);

                lineWidth = childWidth;
                lineHeight = childHeight;
                mLineViews = new ArrayList<>();
            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(childHeight, lineHeight);
            }
            mLineViews.add(child);

            if (i == (count - 1)) {
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }
        }
        mLineHeight.add(lineHeight);
        mAllViews.add(mLineViews);
        width += getPaddingLeft() + getPaddingRight();
        height += getPaddingTop() + getPaddingBottom();

        setMeasuredDimension(modeWidth == MeasureSpec.AT_MOST ? width : sizeWidth, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int top = getPaddingTop();//开始布局子view的 top距离
        int left = getPaddingLeft();//开始布局子view的 left距离

        int lineNum = mAllViews.size();//行数
        List<View> lineView;
        int lineHeight;
        for (int i = 0; i < lineNum; i++) {
            lineView = mAllViews.get(i);
            lineHeight = mLineHeight.get(i);

            for (int j = 0; j < lineView.size(); j++) {
                View child = lineView.get(j);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }

                int ld = left + margin;
                int td = top + margin;
                int rd = ld + child.getMeasuredWidth();//不需要加上 params.rightMargin,
                int bd = td + child.getMeasuredHeight();//不需要加上 params.bottomMargin, 因为在 onMeasure , 中已经加在了 lineHeight 中
                child.layout(ld, td, rd, bd);

                left += child.getMeasuredWidth() + margin * 2;//因为在 这里添加了;
            }

            left = getPaddingLeft();
            top += lineHeight;
        }
    }
}
