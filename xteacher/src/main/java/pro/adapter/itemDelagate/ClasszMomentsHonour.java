package pro.adapter.itemDelagate;

import android.view.View;

import com.zhy.adapter.abslistview.ViewHolder;
import com.zhy.adapter.abslistview.base.ItemViewDelegate;
import com.zjhz.teacher.R;
import com.zjhz.teacher.bean.ClasszMoment;
import com.zjhz.teacher.bean.ImageUrl;
import com.zjhz.teacher.ui.view.images.activity.ImageGalleryActivity;

import java.util.ArrayList;
import java.util.List;

import pro.widget.NineGridLayout.CustomImageView;
import pro.widget.NineGridLayout.NineGridlayout;

/**
 * Created by Tabjin on 2017/4/14.
 * Description:
 * What Changed:
 */

public class ClasszMomentsHonour implements ItemViewDelegate<ClasszMoment> {

    public static final String HONOUR = "1003";

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_classz_moments_hounor_roll;
    }

    @Override
    public boolean isForViewType(ClasszMoment item, int position) {
        if(HONOUR.equals(item.getDcCode()))
            return true;
        return false;
    }

    @Override
    public void convert(final ViewHolder holder, final ClasszMoment classzMoment, final int position) {
        //图片设置
        showPic(holder, classzMoment);
//        holder.setText(R.id.title_tv, classzMoment.getCcdTitle());
       /* NineGridlayout picView = holder.getView(R.id.pic_grid);
        List<ImageUrl> picUrls = classzMoment.getImageUrls();
        final List<String> urlList = new ArrayList<>();
        for(ImageUrl imageUrl:picUrls){
            urlList.add(imageUrl.getPicPath());
        }
        picView.setImagesData(urlList);
        picView.setOnItemClickListener(new NineGridlayout.OnItemClickListener() {
            @Override
            public void OnItemClick(int position, View view) {
                String[] urls = new String[urlList.size()];
                for(int i=0;i<urlList.size();i++){
                    urls[i] = urlList.get(i);
                }
                ImageGalleryActivity.show(view.getContext(),urls , position, false);
            }
        });*/
    }

    /**
     * 显示图片
     * @param holder
     * @param classzMoment
     */
    private void showPic(ViewHolder holder, ClasszMoment classzMoment) {
        NineGridlayout picView = holder.getView(R.id.pic_grid);
        List<ImageUrl> picUrls = classzMoment.getImageUrls();
        final List<String> urlList = new ArrayList<>();
        if(picUrls!=null) {
            for (ImageUrl imageUrl : picUrls) {
                urlList.add(imageUrl.getPicPath());
            }
        }else{
            picView.setVisibility(View.GONE);
            holder.getView(R.id.image_view).setVisibility(View.GONE);
        }
        if (urlList.size() == 1) {
            picView.setVisibility(View.GONE);
            holder.getView(R.id.image_view).setVisibility(View.VISIBLE);
            ((CustomImageView) holder.getView(R.id.image_view)).setImageUrl(urlList.get(0));
            holder.setOnClickListener(R.id.image_view, new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String[] urls = {urlList.get(0)};
                    ImageGalleryActivity.show(v.getContext(), urls, 0, false);
                }
            });
        }else if(urlList.size()>1) {
            picView.setVisibility(View.VISIBLE);
            holder.setVisible(R.id.image_view, false);
            picView.setImagesData(urlList);
            picView.setOnItemClickListener(new NineGridlayout.OnItemClickListener() {
                @Override
                public void OnItemClick(int position, View view) {
                    String[] urls = new String[urlList.size()];
                    for (int i = 0; i < urlList.size(); i++) {
                        urls[i] = urlList.get(i);
                    }
                    ImageGalleryActivity.show(view.getContext(), urls, position, false);
                }
            });
        }
    }
}
