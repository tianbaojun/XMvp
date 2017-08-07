package pro.widget.NineGridLayout;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * 博客地址：http://blog.csdn.net/gdutxiaoxu
 * @author xujun
 * @time 2015/11/27 16:13.
 */
public class NineGridlayout extends ViewGroup {

    private final int MAX_COLUMNS = 3;
    private final int MAX_ROW3 = 3;
    /**
     * 图片之间的间隔
     */
    private int gap = 5;
    private int columns;//列数
    private int rows;//行数
    private List<String> listData;
    private int totalWidth;
    private OnItemClickListener itemClickListener;

    public NineGridlayout(Context context) {
        super(context);
    }

    public NineGridlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        ScreenTools screenTools=ScreenTools.instance(getContext());
//        初始总宽度
        totalWidth=screenTools.getScreenWidth()-screenTools.dip2px(80);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    private void layoutChildrenView(){
        int childrenCount = listData.size();

        int singleWidth = (totalWidth - gap * (3 - 1)) / 3;
        int singleHeight = singleWidth;

        //根据子view数量确定高度
        LayoutParams params = getLayoutParams();
        int marginHeight = getPaddingTop() + getPaddingTop();
        params.height = singleHeight * rows + gap * (rows - 1)+marginHeight;

        setLayoutParams(params);
//摆放孩子的位置
        for (int i = 0; i < childrenCount; i++) {
            CustomImageView childrenView = (CustomImageView) getChildAt(i);
            childrenView.setImageUrl(listData.get(i));
            final int p = i;
            childrenView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.OnItemClick(p,v);
                }
            });
            // TODO: 2017/4/21 设置ItemClickListener
            int[] position = findPosition(i);

//            加上getPaddingLeft（），为了支持Padding属性
            int left = (singleWidth + gap) * position[1]+getPaddingLeft();
            int top = (singleHeight + gap) * position[0]+getPaddingTop();
            int right = left + singleWidth;
            int bottom = top + singleHeight;

            childrenView.layout(left, top, right, bottom);
        }

    }

    private int[] findPosition(int childNum) {
        int[] position = new int[2];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if ((i * columns + j) == childNum) {
                    position[0] = i;//行
                    position[1] = j;//列
                    break;
                }
            }
        }
        return position;
    }

    public int getGap() {
        return gap;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }

    public void setImagesData(List<String> lists) {
        if (lists == null || lists.isEmpty()) {
            return;
        }
        //初始化布局
        generateChildrenLayout(lists.size());
        //这里做一个重用view的处理
        if (listData == null) {
            int i = 0;
            while (i < lists.size()) {
                CustomImageView iv = generateImageView();
                addView(iv,generateDefaultLayoutParams());
                i++;
            }
        } else {
            int oldViewCount = listData.size();
            int newViewCount = lists.size();
            if (oldViewCount > newViewCount) {
                removeViews(newViewCount - 1, oldViewCount - newViewCount);
            } else if (oldViewCount < newViewCount) {
                for (int i = 0; i < newViewCount - oldViewCount; i++) {
                    CustomImageView iv = generateImageView();
                    addView(iv,generateDefaultLayoutParams());
                }
            }
        }
        listData = lists;
        layoutChildrenView();
    }

    /**
     * 根据图片个数确定行列数量
     * 对应关系如下
     * visitorNum	row	column
     * 1	   1	1
     * 2	   1	2
     * 3	   1	3
     * 4	   2	2
     * 5	   2	3
     * 6	   2	3
     * 7	   3	3
     * 8	   3	3
     * 9	   3	3
     *
     * @param length
     */
    private void generateChildrenLayout(int length) {
        if (length <= MAX_COLUMNS) {
            rows = 1;
            columns = length;
        } else if (length <= MAX_COLUMNS*2) {
            rows = 2;
            columns = MAX_COLUMNS;
            if (length == MAX_COLUMNS+1) {
                columns = 2;
            }
        } else {
            rows = 3;
            columns = MAX_COLUMNS;
        }
    }

    private CustomImageView generateImageView() {
        CustomImageView iv = new CustomImageView(getContext());
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        iv.setBackgroundColor(Color.parseColor("#f5f5f5"));
        return iv;
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener{
        void OnItemClick(int position, View view);
    }
}
