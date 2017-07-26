package cn.edu.nju.cs.seg.schooledinapp.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.StudioInfoActivity;
import cn.edu.nju.cs.seg.schooledinapp.adapter.base.BaseItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.model.UserStudioItem;

public class UserStudioItemAdapter
        extends BaseItemAdapter<UserStudioItem, UserStudioItemAdapter.ViewHolder> {

    static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llItem;

        ImageView ivAvatar;

        TextView tvName;

        TextView tvQuestions;

        TextView tvEssays;

        public ViewHolder(View v) {
            super(v);
            llItem = (LinearLayout) v.findViewById(R.id.ll_user_studios_studio_item);
            ivAvatar = (ImageView) v.findViewById(R.id.civ_user_studios_studio_avatar);
            tvName = (TextView) v.findViewById(R.id.tv_user_studios_studio_name);
            tvQuestions = (TextView) v.findViewById(R.id.tv_user_studios_studio_questions);
            tvEssays = (TextView) v.findViewById(R.id.tv_user_studios_studio_essays);
        }

    }

    public UserStudioItemAdapter(List<UserStudioItem> studioItemList) {
        super(studioItemList);
    }

    @Override
    protected View doInflateItemView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.user_studios_item_studio, parent, false);
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
            public void onClick(View view) {
                final UserStudioItem item = getItemList().get(holder.getAdapterPosition());
                StudioInfoActivity.startActivityWithParameters(getContext(), item.getId());
            }
        });
    }

    @Override
    protected void doBindViewHolder(ViewHolder holder, UserStudioItem item) {
        Glide.with(getContext()).
                load(item.getAvatarUrl())
                .into(holder.ivAvatar);
        holder.tvName.setText(item.getName());
        holder.tvQuestions.setText(item.getQuestions() + " 问题");
        holder.tvEssays.setText(item.getEssays() + " 文章");
    }

}
