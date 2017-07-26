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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.AnswerActivity;
import cn.edu.nju.cs.seg.schooledinapp.adapter.base.BaseItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.model.UserAnswerItem;
import cn.edu.nju.cs.seg.schooledinapp.widgets.RichTextView;

public class UserAnswerItemAdapter
        extends BaseItemAdapter<UserAnswerItem, UserAnswerItemAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llItem;

        View vVerticalSeparator;
        
        TextView tvQuestionTitle;

        RichTextView tvContent;

        TextView tvComments;

        TextView tvCreatedAt;

        public ViewHolder(View v) {
            super(v);
            llItem = (LinearLayout) v.findViewById(R.id.ll_user_answers_item);
            vVerticalSeparator = (View) v.findViewById(R.id.v_user_answers_vertical_line);
            tvQuestionTitle = (TextView) v.findViewById(R.id.tv_user_answers_question_title);
            tvContent = (RichTextView) v.findViewById(R.id.tv_user_answers_content);
            tvComments = (TextView) v.findViewById(R.id.tv_user_answers_comments);
            tvCreatedAt = (TextView) v.findViewById(R.id.tv_user_answers_created_at);
        }

    }

    public UserAnswerItemAdapter(List<UserAnswerItem> itemList) {
        super(itemList);
    }

    @Override
    protected View doInflateItemView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.user_answers_item_answer, parent, false);
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
                final UserAnswerItem item = getItemList().get(holder.getAdapterPosition());
                AnswerActivity.startActivityWithParameters(getContext(), item.getId());
            }
        });

    }

    @Override
    protected void doBindViewHolder(ViewHolder holder, UserAnswerItem item) {
        int position = holder.getAdapterPosition();
        Context context = getContext();

        if (position % 2 == 0) {
            holder.vVerticalSeparator.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.verticalLinePink));
        } else {
            holder.vVerticalSeparator.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.verticalLineBlue));
        }

        holder.tvQuestionTitle.setText(item.getQuestionTitle());
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
