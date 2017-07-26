package cn.edu.nju.cs.seg.schooledinapp.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.UserInfoActivity;
import cn.edu.nju.cs.seg.schooledinapp.model.SearchUserItem;

/**
 * Created by manlei on 2017/3/28.
 */

public class SearchUsersItemAdapter extends RecyclerView.Adapter<SearchUsersItemAdapter.ViewHolder> {

    private Context parentContent;

    private int userId;

    private List<SearchUserItem> userList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView userImage;
        TextView userName;
        TextView userAnswersNum;

        public ViewHolder(View view){
            super(view);
            cardView  = (CardView)view;
            userImage = (ImageView)view.findViewById(R.id.civ_search_users_item_avator);
            userAnswersNum = (TextView) view.findViewById(R.id.tv_search_users_item_answers_num);
            userName = (TextView)view.findViewById(R.id.tv_search_users_item_name);
        }

    }

    public SearchUsersItemAdapter(List<SearchUserItem> userList){
        this.userList = userList;
    }

    /**
     * 设置每一个item对应的布局，设置他的监听事件
     *
     * @param parent
     * @param viewType
     * @return
     */
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
        if(parentContent == null)
            parentContent = parent.getContext();
        View view = LayoutInflater.from(parentContent).inflate(R.layout.search_item_users,parent,false);

        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                userId = userList.get(holder.getLayoutPosition()).getId();
                UserInfoActivity.startActivityWithParameters(parentContent, userId);
            }
        });
        return holder;
    }

    /**
     * 填充每一项的数据
     *
     * @param holder
     * @param position
     */
    public void onBindViewHolder(ViewHolder holder,int position){
        SearchUserItem user = userList.get(position);
        holder.userName.setText(user.getName());
        holder.userAnswersNum.setText(user.getAnswers()+" 回答");
        Glide.with(parentContent).load(user.getAvatarUrl()).into(holder.userImage);
    }

    public int getItemCount(){
        return userList.size();
    }
}
