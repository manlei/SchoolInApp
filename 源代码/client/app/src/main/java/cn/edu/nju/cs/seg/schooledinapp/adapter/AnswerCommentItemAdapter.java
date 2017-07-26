package cn.edu.nju.cs.seg.schooledinapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Date;
import java.util.List;

import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.adapter.base.BaseItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.model.AnswerCommentItem;

/**
 * Created by luping on 2017/5/18.
 */

public class AnswerCommentItemAdapter
        extends BaseItemAdapter<AnswerCommentItem, AnswerCommentItemAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llItem;

        ImageView ivAuthorAvatar;

        TextView tvAuthorName;

        TextView tvContent;

        TextView tvCreatedAt;

        public ViewHolder(View v) {
            super(v);
            llItem = (LinearLayout) v.findViewById(R.id.ll_comment_item);
            ivAuthorAvatar = (ImageView) v.findViewById(R.id.civ_item_comment_header_avatar);
            tvAuthorName = (TextView) v.findViewById(R.id.tv_item_comment_author_name);
            tvContent = (TextView) v.findViewById(R.id.tv_item_comment_content);
            tvCreatedAt = (TextView) v.findViewById(R.id.tv_item_comment_created_at);
        }
    }

    public AnswerCommentItemAdapter(List<AnswerCommentItem> commentItemList) {
        super(commentItemList);
    }

    @Override
    protected View doInflateItemView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.item_comment, parent, false);
    }

    @Override
    protected void doBindItemEvents(View itemView, final ViewHolder holder) {
        super.doBindItemEvents(itemView, holder);
    }

    @Override
    protected ViewHolder doCreateViewHolder(View itemView, int viewType) {
        return new ViewHolder(itemView);
    }

    @Override
    protected void doBindViewHolder(ViewHolder holder, AnswerCommentItem item) {
        Context context = getContext();
        Glide.with(context)
                .load(item.getCommenterAvatarUrl())
                .into(holder.ivAuthorAvatar);
        holder.tvAuthorName.setText(item.getCommenter());
        holder.tvContent.setText(item.getContent());
        holder.tvCreatedAt.setText(new Date(item.getCreatedAt()).toLocaleString());
    }


}
