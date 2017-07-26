package cn.edu.nju.cs.seg.schooledinapp.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.QuestionActivity;
import cn.edu.nju.cs.seg.schooledinapp.adapter.base.BaseItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.model.FavoriteQuestionItem;

public class FavoriteQuestionItemAdapter
        extends BaseItemAdapter<FavoriteQuestionItem, FavoriteQuestionItemAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llItem;

        View vVerticalSeparator;

        TextView tvDirectedTo;

        TextView tvTitle;

        TextView tvHeat;

        TextView tvSupports;

        TextView tvCreatedAt;

        public ViewHolder(View v) {
            super(v);
            llItem = (LinearLayout) v.findViewById(R.id.ll_favorites_question_item);
            vVerticalSeparator = (View) v.findViewById(R.id.v_favorites_question_vertical_line);
            tvDirectedTo = (TextView) v.findViewById(R.id.tv_favorites_question_directed_to);
            tvTitle = (TextView) v.findViewById(R.id.tv_favorites_question_title);
            tvHeat = (TextView) v.findViewById(R.id.tv_favorites_question_heat);
            tvSupports = (TextView) v.findViewById(R.id.tv_favorites_question_supports);
            tvCreatedAt = (TextView) v.findViewById(R.id.tv_favorites_question_created_at);
        }

    }

    public FavoriteQuestionItemAdapter(List<FavoriteQuestionItem> questionItemList) {
        super(questionItemList);
    }

    @Override
    protected View doInflateItemView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.favorites_item_question, parent, false);
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
                final FavoriteQuestionItem item = getItemList().get(holder.getAdapterPosition());
                QuestionActivity.startActivityWithParameters(getContext(), item.getId());
            }
        });
    }

    @Override
    protected void doBindViewHolder( ViewHolder holder, FavoriteQuestionItem item) {
        int position = holder.getAdapterPosition();
        Context context = getContext();

        if (position % 2 == 0) {
            holder.vVerticalSeparator.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.verticalLinePink));
        } else {
            holder.vVerticalSeparator.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.verticalLineBlue));
        }

        holder.tvDirectedTo.setText(item.getDirectedTo());
        holder.tvHeat.setText(item.getHeat() + " 热度");
        holder.tvSupports.setText(item.getSupports() + " 赞同");
        holder.tvTitle.setText(item.getTitle());
        holder.tvCreatedAt.setText(new Date(item.getCreatedAt()).toLocaleString());
    }

}
