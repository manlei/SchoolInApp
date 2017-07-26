package cn.edu.nju.cs.seg.schooledinapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.edu.nju.cs.seg.schooledinapp.AppContext;
import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.UserInfoActivity;
import cn.edu.nju.cs.seg.schooledinapp.adapter.base.BaseItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.model.StudioMemberItem;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import cn.edu.nju.cs.seg.schooledinapp.util.JsonRequestBodyBuilder;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static cn.edu.nju.cs.seg.schooledinapp.activity.UserQuestionsActivity.popupWindow;

public class StudioMemberItemAdapter
        extends BaseItemAdapter<StudioMemberItem, StudioMemberItemAdapter.ViewHolder> {

    private static final String TAG = "StudioMemberItemAdapter";

    private int studioId;

    static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llItem;

        ImageView ivAvatar;

        TextView tvName;

        TextView tvAnswers;

        TextView tvBio;

        public ViewHolder(View v) {
            super(v);
            llItem = (LinearLayout) v.findViewById(R.id.ll_studio_members_member_item);
            ivAvatar = (ImageView) v.findViewById(R.id.civ_studio_members_member_avatar);
            tvName = (TextView) v.findViewById(R.id.tv_studio_members_member_name);
            tvAnswers = (TextView) v.findViewById(R.id.tv_studio_members_member_answers);
            tvBio = (TextView) v.findViewById(R.id.tv_studio_members_member_bio);
        }

    }

    public StudioMemberItemAdapter(List<StudioMemberItem> memberItemList,int studioId) {
        super(memberItemList);
        this.studioId = studioId;
    }

    @Override
    protected View doInflateItemView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.studio_members_item_member, parent, false);
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
                final StudioMemberItem item = getItemList().get(holder.getAdapterPosition());
                UserInfoActivity.startActivityWithParameters(getContext(), item.getId());
            }
        });

        //长按删除的监听事件
        holder.llItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final StudioMemberItem item = getItemList().get(holder.getAdapterPosition());
                View popView = LayoutInflater.from(getContext()).inflate(R.layout.popupwindow_delete_user_question,null);
                popupWindow = new PopupWindow(popView, RecyclerView.LayoutParams.WRAP_CONTENT,RecyclerView.LayoutParams.WRAP_CONTENT);
                popupWindow.setAnimationStyle(R.style.MyDialogStyle);
                popupWindow.setOutsideTouchable(true);

                TextView tvDelete = (TextView) popView.findViewById(R.id.tv_popupwindow_delete);

                int deleteWidth=tvDelete.getWidth();
                popupWindow.showAsDropDown(v,(v.getWidth()-deleteWidth)/2,-v.getHeight());

                tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //获取当前位置，发送删除请求，重新显示

                        RequestBody body = new JsonRequestBodyBuilder()
                                .append("manager_email_or_phone", AppContext.getOnlineUser().getEmail())
                                .append("manager_password",AppContext.getOnlineUser().getPassword())
                                .build();

                        ApiClient.getStudiosService()
                                .deleteMember(studioId,item.getId(),body)
                                .enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        switch(response.code()){
                                            case 204:{
                                                //删除
                                                popupWindow.dismiss();
                                                StudioMemberItemAdapter.this.getItemList().remove(holder.getAdapterPosition());
                                                StudioMemberItemAdapter.this.notifyDataSetChanged();
                                                Toast.makeText(getContext(),"删除成功",Toast.LENGTH_LONG).show();
                                            }
                                            break;
                                            case 401:{
                                                popupWindow.dismiss();
                                                Toast.makeText(getContext(),"没有权限",Toast.LENGTH_LONG).show();
                                            }
                                            break;
                                            case 503:{
                                                popupWindow.dismiss();
                                                Toast.makeText(getContext(),"服务器忙",Toast.LENGTH_LONG).show();
                                            }
                                            break;
                                            default:{
                                                popupWindow.dismiss();
                                                Toast.makeText(getContext(),"服务器忙",Toast.LENGTH_LONG).show();}
                                            break;

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        popupWindow.dismiss();
                                        Toast.makeText(getContext(), "提问失败，请检查网络", Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                });

                return true;
            }
        });

    }

    @Override
    protected void doBindViewHolder(ViewHolder holder, StudioMemberItem item) {
        Glide.with(getContext()).
                load(item.getAvatarUrl())
                .into(holder.ivAvatar);
        holder.tvName.setText(item.getName());
        holder.tvAnswers.setText(item.getAnswers() + " 回答");
        holder.tvBio.setText(item.getBio());
    }

}
