/*
 * 源文件名：ImageFolderAdapter
 * 文件版本：1.0.0
 * 创建作者：captailgodwin
 * 创建日期：2016/11/7
 * 修改作者：captailgodwin
 * 修改日期：2016/11/7
 * 文件描述：选择图片适配器
 * 版权所有：Copyright 2016 zjhz, Inc。 All Rights Reserved.
 */
package com.zjhz.teacher.ui.view.images.adapter;

import android.content.Context;
import android.media.Image;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.zjhz.teacher.R;
import com.zjhz.teacher.ui.view.images.view.PicturesPreviewerItemTouchCallback;

import java.util.ArrayList;
import java.util.List;

public class SelectImageAdapter extends RecyclerView.Adapter<SelectImageAdapter.TweetSelectImageHolder> implements PicturesPreviewerItemTouchCallback.ItemTouchHelperAdapter {
    private int MAX_SIZE = 9;
    private final int TYPE_NONE = 0;
    private final int TYPE_ADD = 1;
    private final List<Model> mModels = new ArrayList<>();
    private Callback mCallback;

    public void setMAX_SIZE(int MAX_SIZE){
        this.MAX_SIZE = MAX_SIZE;
    }

    public SelectImageAdapter(Callback callback) {
        mCallback = callback;
    }

    @Override
    public int getItemViewType(int position) {
        int size = mModels.size();
        if (size >= MAX_SIZE)
            return TYPE_NONE;
        else if (position == size) {
            return TYPE_ADD;
        } else {
            return TYPE_NONE;
        }
    }

    @Override
    public TweetSelectImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_images_publish_selecter, parent, false);
        if (viewType == TYPE_NONE) {
            return new TweetSelectImageHolder(view, new TweetSelectImageHolder.HolderListener() {
                @Override
                public void onDelete(Model model) {
                    Callback callback = mCallback;
                    if (callback != null) {
                        int pos = mModels.indexOf(model);
                        if (pos == -1)
                            return;
                        mModels.remove(pos);
                        if (mModels.size() > 0)
                            notifyItemRemoved(pos);
                        else
                            notifyDataSetChanged();
                        callback.onDelete(pos);
                    }
                }

                @Override
                public void onDrag(TweetSelectImageHolder holder) {
                    Callback callback = mCallback;
                    if (callback != null) {
                        // Start a drag whenever the handle view it touched
                        mCallback.onStartDrag(holder);
                    }
                }
            });
        } else {
            return new TweetSelectImageHolder(view, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Callback callback = mCallback;
                    if (callback != null) {
                        callback.onLoadMoreClick();
                    }
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(final TweetSelectImageHolder holder, final int position) {
        int size = mModels.size();
        if (size >= MAX_SIZE || size != position) {
            Model model = mModels.get(position);
            holder.bind(position, model, mCallback.getImgLoader(), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallback != null) {
                        mCallback.clickItem(position);
                    }
                }
            });
        }
    }

    @Override
    public void onViewRecycled(TweetSelectImageHolder holder) {
        Glide.clear(holder.mImage);
    }

    @Override
    public int getItemCount() {
        int size = mModels.size();
        if (size == MAX_SIZE) {
            return size;
        } else if (size == 0) {
            return 1;
        } else {
            return size + 1;
        }
    }

    public void clear() {
        List<Model> tmpModells = new ArrayList<>();
        for(Model model : mModels){
            if(model.isUpload)
                tmpModells.add(model);
        }
        mModels.clear();
        mModels.addAll(tmpModells);
    }

    public void add(Model model) {
        if (mModels.size() >= MAX_SIZE)
            return;
        mModels.add(model);
    }

    public void add(String path) {
        add(new Model(path));
    }
    public void addLoad(String path) {
        add(new Model(path, true));
    }

    public String[] getPaths() {
        int size = mModels.size();
        if (size == 0)
            return null;
        String[] paths = new String[size];
        int i = 0;
        for (Model model : mModels) {
            paths[i++] = model.path;
        }
        return paths;
    }

    public String[] getNotLoadPaths() {
        if (mModels.size() == 0)
            return null;
        List<String> pathList = new ArrayList<>();
        for (Model model : mModels) {
            if(!model.isUpload)
                pathList.add(model.path);
        }

        String[] paths = new String[pathList.size()];
        for(int i = 0; i < pathList.size(); i++){
            paths[i] = pathList.get(i);
        }
        return paths;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        //Collections.swap(mModels, fromPosition, toPosition);
        if (fromPosition == toPosition)
            return false;

        if (fromPosition < toPosition) {
            Model fromModel = mModels.get(fromPosition);
            Model toModel = mModels.get(toPosition);

            mModels.remove(fromPosition);
            mModels.add(mModels.indexOf(toModel) + 1, fromModel);
        } else {
            Model fromModel = mModels.get(fromPosition);
            mModels.remove(fromPosition);
            mModels.add(toPosition, fromModel);
        }

        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        mModels.remove(position);
        notifyItemRemoved(position);
    }

    public static class Model {
        public Model(String path) {
            this.path = path;
        }
        public Model(String path, boolean isUpload){
            this.path = path;
            this.isUpload = isUpload;
        }
        public String path;
        public boolean isUpload;
    }

    public interface Callback {
        void onLoadMoreClick();

        RequestManagerWithVideo getImgLoader();

        /**
         * Called when a view is requesting a start of a drag.
         *
         * @param viewHolder The holder of the view to drag.
         */
        void onStartDrag(RecyclerView.ViewHolder viewHolder);

        void clickItem(int position);

        void onDelete(int position);
    }

    /**
     * TweetSelectImageHolder
     */
    public static class TweetSelectImageHolder extends RecyclerView.ViewHolder implements PicturesPreviewerItemTouchCallback.ItemTouchHelperViewHolder {
        private ImageView mImage;
        private ImageView mDelete;
        private ImageView mGifMask;
        private ImageView videoIcon;
        private HolderListener mListener;

        private TweetSelectImageHolder(View itemView, HolderListener listener) {
            super(itemView);
            mListener = listener;
            mImage = (ImageView) itemView.findViewById(R.id.iv_content);
            mDelete = (ImageView) itemView.findViewById(R.id.iv_delete);
            mGifMask = (ImageView) itemView.findViewById(R.id.iv_is_gif);
            videoIcon = (ImageView) itemView.findViewById(R.id.video_icon);

            mDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Object obj = v.getTag();
                    final HolderListener holderListener = mListener;
                    if (holderListener != null && obj != null && obj instanceof Model) {
                        holderListener.onDelete((Model) obj);
                    }
                }
            });
            mImage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final HolderListener holderListener = mListener;
                    if (holderListener != null) {
                        holderListener.onDrag(TweetSelectImageHolder.this);
                    }
                    return true;
                }
            });
            mImage.setBackgroundColor(0xffdadada);
        }

        private TweetSelectImageHolder(View itemView, View.OnClickListener clickListener) {
            super(itemView);

            mImage = (ImageView) itemView.findViewById(R.id.iv_content);
            mDelete = (ImageView) itemView.findViewById(R.id.iv_delete);

            mDelete.setVisibility(View.GONE);
            mImage.setImageResource(R.mipmap.ic_tweet_add);
            mImage.setOnClickListener(clickListener);
            mImage.setBackgroundDrawable(null);
        }

        public void bind(int position, Model model, RequestManagerWithVideo requestManagerWithVideo, View.OnClickListener clickListener) {
            mDelete.setTag(model);
            // In this we need clear before load
            Glide.clear(mImage);
            // Load image
            if (model.path.toLowerCase().endsWith("gif")) {
                requestManagerWithVideo.loader.load(model.path)
                        .asBitmap()
                        .centerCrop()
                        .error(R.mipmap.ic_split_graph)
                        .into(mImage);
                // Show gif mask
                mGifMask.setVisibility(View.VISIBLE);
            } else {
                requestManagerWithVideo.loader.load(model.path)
                        .centerCrop()
                        .error(R.mipmap.ic_split_graph)
                        .into(mImage);
                mGifMask.setVisibility(View.GONE);
            }
            if(requestManagerWithVideo.isVideo){
                videoIcon.setVisibility(View.VISIBLE);
                mImage.setOnClickListener(clickListener);
            }else {
                videoIcon.setVisibility(View.GONE);
            }
        }

        @Override
        public void onItemSelected() {
            try {
                Vibrator vibrator = (Vibrator) itemView.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(20);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onItemClear() {

        }



        /**
         * Holder 与Adapter之间的桥梁
         */
        interface HolderListener {
            void onDelete(Model model);

            void onDrag(TweetSelectImageHolder holder);
        }
    }

    public class RequestManagerWithVideo{
        public RequestManager loader;
        public boolean isVideo = false;
    }
}
