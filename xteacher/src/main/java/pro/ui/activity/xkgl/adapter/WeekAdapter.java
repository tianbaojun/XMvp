package pro.ui.activity.xkgl.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zjhz.teacher.R;

import java.util.List;

import pro.widget.Selector.AutoLocateHorizontalView;

/**
 * Created by jianglei on 2/4/17.
 */

public class WeekAdapter extends RecyclerView.Adapter<WeekAdapter.AgeViewHolder> implements AutoLocateHorizontalView.IAutoLocateHorizontalView {
    private Context context;
    private View view;
    private List<String> ages;
    public WeekAdapter(Context context, List<String>ages){
        this.context = context;
        this.ages = ages;
    }

    @Override
    public AgeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.item_week,parent,false);
        return new AgeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AgeViewHolder holder, int position) {
        holder.tvAge.setText(ages.get(position));
    }

    @Override
    public int getItemCount() {
        return  ages.size();
    }

    @Override
    public View getItemView() {
        return view;
    }

    @Override
    public void onViewSelected(boolean isSelected, int pos, RecyclerView.ViewHolder holder, int itemWidth) {
        if(isSelected) {
            ((AgeViewHolder) holder).tvAge.setTextSize(20);
        }else{
            ((AgeViewHolder) holder).tvAge.setTextSize(14);
        }
    }

    static class AgeViewHolder extends RecyclerView.ViewHolder{
        TextView tvAge;
        AgeViewHolder(View itemView) {
            super(itemView);
            tvAge = (TextView)itemView.findViewById(R.id.tv_age);
        }
    }
}
