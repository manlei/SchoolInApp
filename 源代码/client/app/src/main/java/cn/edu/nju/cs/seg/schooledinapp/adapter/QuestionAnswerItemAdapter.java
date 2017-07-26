package cn.edu.nju.cs.seg.schooledinapp.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.AnswerActivity;
import cn.edu.nju.cs.seg.schooledinapp.widgets.RichTextView;
import cn.edu.nju.cs.seg.schooledinapp.adapter.base.BaseItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.model.QuestionAnswerItem;

public class QuestionAnswerItemAdapter
        extends BaseItemAdapter<QuestionAnswerItem, QuestionAnswerItemAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout rlItem;

        ImageView ivAnswererAvatar;

        TextView tvAnswerer;

        RichTextView tvContent;

        TextView tvComments;

        TextView tvCreatedAt;

        public ViewHolder(View v) {
            super(v);
            rlItem = (RelativeLayout) v.findViewById(R.id.rl_item_answer);
            ivAnswererAvatar = (ImageView) v.findViewById(R.id.civ_item_answer_author_avatar);
            tvAnswerer = (TextView) v.findViewById(R.id.tv_item_answer_author_name);
            tvContent = (RichTextView) v.findViewById(R.id.tv_item_answer_content);
            tvComments = (TextView) v.findViewById(R.id.tv_item_answer_comments);
            tvCreatedAt = (TextView) v.findViewById(R.id.tv_item_answer_created_at);
        }

    }

    public QuestionAnswerItemAdapter(List<QuestionAnswerItem> questionAnswerItemItemList) {
        super(questionAnswerItemItemList);
    }

    @Override
    protected View doInflateItemView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.item_answer, parent, false);
    }

    @Override
    protected void doBindItemEvents(View itemView, final ViewHolder holder) {
        super.doBindItemEvents(itemView, holder);

        holder.rlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final QuestionAnswerItem item = getItemList().get(holder.getAdapterPosition());
                AnswerActivity.startActivityWithParameters(getContext(), item.getId());
            }
        });

    }

    @Override
    protected ViewHolder doCreateViewHolder(View itemView, int viewType) {
        return new ViewHolder(itemView);
    }

    @Override
    protected void doBindViewHolder(ViewHolder holder, QuestionAnswerItem item) {
        Context context = getContext();
        Glide.with(context)
                .load(item.getAnswererAvatarUrl())
                .into(holder.ivAnswererAvatar);
        holder.tvAnswerer.setText(item.getAnswerer());
        holder.tvComments.setText(item.getComments() + " 评论");
        holder.tvCreatedAt.setText(new Date(item.getCreatedAt()).toLocaleString());
        if ("text".equals(item.getType())) {
            // 图片显示[图片]
            String mtext = item.getContent();
            String pattern = "<img src=(.*?)[^>]*?>";
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(mtext);
            String changedText = m.replaceAll("[图片]");
            holder.tvContent.setHtml(changedText, 500);
        } else if("audio".equals(item.getType())) {
            holder.tvContent.setText("[语音]");
        }
    }

}

