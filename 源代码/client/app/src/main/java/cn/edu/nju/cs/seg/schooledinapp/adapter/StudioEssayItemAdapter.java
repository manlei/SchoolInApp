package cn.edu.nju.cs.seg.schooledinapp.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.EssayActivity;
import cn.edu.nju.cs.seg.schooledinapp.adapter.base.BaseItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.model.StudioEssayItem;

public class StudioEssayItemAdapter
        extends BaseItemAdapter<StudioEssayItem, StudioEssayItemAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llItem;

        View vVerticalSeparator;


        TextView tvTitle;

        TextView tvHeat;

        TextView tvSupports;

        TextView tvComments;

        public ViewHolder(View v) {
            super(v);
            llItem = (LinearLayout) v.findViewById(R.id.ll_studio_essays_item);
            vVerticalSeparator = (View) v.findViewById(R.id.v_studio_essays_vertical_line);
            tvTitle = (TextView) v.findViewById(R.id.tv_studio_essays_title);
            tvHeat = (TextView) v.findViewById(R.id.tv_studio_essays_heat);
            tvSupports = (TextView) v.findViewById(R.id.tv_studio_essays_supports);
            tvComments = (TextView) v.findViewById(R.id.tv_studio_essays_comments);
        }

    }


    public StudioEssayItemAdapter(List<StudioEssayItem> essayItemList) {
        super(essayItemList);
    }

    @Override
    protected View doInflateItemView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.studio_essays_item_essay, parent, false);
    }

    @Override
    protected ViewHolder doCreateViewHolder(View itemView, int viewType) {
        return new ViewHolder(itemView);
    }

    @Override
    protected void doBindItemEvents(View itemView, final ViewHolder holder) {
        super.doBindItemEvents(itemView, holder);

        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final StudioEssayItem item = getItemList().get(holder.getAdapterPosition());
                EssayActivity.startActivityWithParameters(getContext(), item.getId());
            }
        });
    }

    @Override
    protected void doBindViewHolder(ViewHolder holder, StudioEssayItem item) {
        int position = holder.getAdapterPosition();
        Context context = getContext();

        if (position % 2 == 0) {
            holder.vVerticalSeparator.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.verticalLineBlue));
        } else {
            holder.vVerticalSeparator.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.verticalLinePink));
        }

        holder.tvTitle.setText(item.getTitle());
        holder.tvHeat.setText(item.getHeat() + " 热度");
        holder.tvSupports.setText(item.getSupports() + " 赞同");
        holder.tvComments.setText(item.getComments() + " 评论");
    }

}
