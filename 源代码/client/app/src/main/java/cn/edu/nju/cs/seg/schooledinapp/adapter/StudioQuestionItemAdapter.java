package cn.edu.nju.cs.seg.schooledinapp.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import cn.edu.nju.cs.seg.schooledinapp.AppContext;
import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.QuestionActivity;
import cn.edu.nju.cs.seg.schooledinapp.adapter.base.BaseItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.model.StudioQuestionItem;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import cn.edu.nju.cs.seg.schooledinapp.util.JsonRequestBodyBuilder;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static cn.edu.nju.cs.seg.schooledinapp.activity.UserQuestionsActivity.popupWindow;

public class StudioQuestionItemAdapter 
        extends BaseItemAdapter<StudioQuestionItem, StudioQuestionItemAdapter.ViewHolder> {

    private String TAG = "StudioQuestionItemAda";

    static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llItem;

        View vVerticalSeparator;
        
        TextView tvTitle;

        TextView tvHeat;

        TextView tvSupports;

        TextView tvCreatedAt;

        public ViewHolder(View v) {
            super(v);
            llItem = (LinearLayout) v.findViewById(R.id.ll_studio_questions_item);
            vVerticalSeparator = (View) v.findViewById(R.id.v_studio_questions_vertical_line);
            tvTitle = (TextView) v.findViewById(R.id.tv_studio_questions_title);
            tvHeat = (TextView) v.findViewById(R.id.tv_studio_questions_heat);
            tvSupports = (TextView) v.findViewById(R.id.tv_studio_questions_supports);
            tvCreatedAt = (TextView) v.findViewById(R.id.tv_studio_questions_created_at);
        }

    }

    public StudioQuestionItemAdapter(List<StudioQuestionItem> questionItemList) {
        super(questionItemList);
    }

    @Override
    protected View doInflateItemView(LayoutInflater inflater, ViewGroup parent) {
        return inflater.inflate(R.layout.studio_questions_item_question, parent, false);
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
                final StudioQuestionItem item = getItemList().get(holder.getAdapterPosition());
                QuestionActivity.startActivityWithParameters(getContext(), item.getId());
            }
        });

        //长按删除的监听事件
        holder.llItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final StudioQuestionItem item = getItemList().get(holder.getAdapterPosition());
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
                        RequestBody body = new JsonRequestBodyBuilder()
                                .append("manager_email_or_phone", AppContext.getOnlineUser().getEmail())
                                .append("manager_password",AppContext.getOnlineUser().getPassword())
                                .build();

                        ApiClient.getQuestionsService()
                                .deleteQuestion(item.getId(),body)
                                .enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        switch(response.code()){
                                            case 204:{
                                                //删除
                                                popupWindow.dismiss();
                                                StudioQuestionItemAdapter.this.getItemList().remove(holder.getAdapterPosition());
                                                StudioQuestionItemAdapter.this.notifyDataSetChanged();
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
    protected void doBindViewHolder( ViewHolder holder, StudioQuestionItem item) {
        int position = holder.getAdapterPosition();
        Context context = getContext();

        if (position % 2 == 0) {
            holder.vVerticalSeparator.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.verticalLinePink));
        } else {
            holder.vVerticalSeparator.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.verticalLineBlue));
        }

        holder.tvHeat.setText(item.getHeat() + " 热度");
        holder.tvSupports.setText(item.getSupports() + " 赞同");
        holder.tvTitle.setText(item.getTitle());
        holder.tvCreatedAt.setText(new Date(item.getCreatedAt()).toLocaleString());
    }
    
}
