package cn.edu.nju.cs.seg.schooledinapp.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.AnswerActivity;
import cn.edu.nju.cs.seg.schooledinapp.adapter.base.BaseItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.model.FavoriteAnswerItem;
import cn.edu.nju.cs.seg.schooledinapp.widgets.RichTextView;

public class FavoriteAnswerItemAdapter
        extends BaseItemAdapter<FavoriteAnswerItem, FavoriteAnswerItemAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llItem;

        View vVerticalSeparator;

        ImageView ivAnswererAvatar;

        TextView tvAnswerer;

        TextView tvQuestionTitle;

        RichTextView tvContent;

        TextView tvComments;

        TextView tvCreatedAt;

        public ViewHolder(View v) {
            super(v);
            llItem = (LinearLayout) v.findViewById(R.id.ll_favorites_answer_item);
            vVerticalSeparator = (View) v.findViewById(R.id.v_favorites_answer_vertical_line);
            ivAnswererAvatar = (ImageView) v.findViewById(R.id.civ_favorites_answer_answerer_avatar);
            tvAnswerer = (TextView) v.findViewById(R.id.tv_favorites_answer_answerer);
            tvQuestionTitle = (TextView) v.findViewById(R.id.tv_favorites_answer_question_title);
            tvContent = (RichTextView) v.findViewById(R.id.tv_favorites_answer_content);
            tvComments = (TextView) v.findViewById(R.id.tv_favorites_answer_comments);
            tvCreatedAt = (TextView) v.findViewById(R.id.tv_favorites_answer_created_at);
        }

    }

    public FavoriteAnswerItemAdapter(List<FavoriteAnswerItem> answerItemList) {
        super(answerItemList);
    }

    @Override
    protected View doInflateItemView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.favorites_item_answer, parent, false);
    }

    @Override
    protected void doBindItemEvents(View itemView, final ViewHolder holder) {
        super.doBindItemEvents(itemView, holder);

        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FavoriteAnswerItem item = getItemList().get(holder.getAdapterPosition());
                AnswerActivity.startActivityWithParameters(getContext(), item.getId());
            }
        });

    }

    @Override
    protected ViewHolder doCreateViewHolder(View itemView, int viewType) {
        return new ViewHolder(itemView);
    }

    @Override
    protected void doBindViewHolder(ViewHolder holder, FavoriteAnswerItem item) {
        int position = holder.getAdapterPosition();
        Context context = getContext();

        if (position % 2 == 0) {
            holder.vVerticalSeparator.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.verticalLinePink));
        } else {
            holder.vVerticalSeparator.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.verticalLineBlue));
        }

        Glide.with(context)
                .load(item.getAnswererAvatarUrl())
                .into(holder.ivAnswererAvatar);
        holder.tvAnswerer.setText(item.getAnswerer());
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
