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
import cn.edu.nju.cs.seg.schooledinapp.activity.StudioInfoActivity;
import cn.edu.nju.cs.seg.schooledinapp.model.SearchStudioItem;

/**
 * Created by manlei on 2017/3/28.
 */

public class SearchStudiosItemAdapter extends RecyclerView.Adapter<SearchStudiosItemAdapter.ViewHolder> {
    //设置父布局
    private Context parentContext;

    private int studioId;

    private List<SearchStudioItem> studiosList;

    /**
     * 设置需要的在item中要显示的即可
     */
    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView studioImage;
        TextView studioName;
        TextView studioEssaysNum;

        public ViewHolder(View view){
            super(view);
            cardView  = (CardView)view;
            studioImage = (ImageView)view.findViewById(R.id.civ_search_studios_item_avator);
            studioName = (TextView)view.findViewById(R.id.tv_search_studios_item_studio_name);
            studioEssaysNum = (TextView) view.findViewById(R.id.tv_search_studios_item_essays_num);
        }
    }
    public SearchStudiosItemAdapter(List<SearchStudioItem> studiosList){
        this.studiosList = studiosList;
    }

    /**
     * 初始化viewholder,设置点击监听事件
     *
     * @param parent 父布局
     * @param viewType
     * @return
     */
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(parentContext == null)
            parentContext = parent.getContext();
        View view = LayoutInflater.from(parentContext).inflate(R.layout.search_item_studios,parent,false);

        final ViewHolder holder = new ViewHolder(view);

        holder.cardView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
				studioId = studiosList.get(holder.getLayoutPosition()).getId();
                StudioInfoActivity.startActivityWithParameters(parentContext,studioId);
            }
        });
        return holder;
    }

    /**
     *  向viewholder中添加要显示的内容
     *
     * @param holder
     * @param position
     */
    public void onBindViewHolder(ViewHolder holder,int position){
        SearchStudioItem studio = studiosList.get(position);
        holder.studioName.setText(studio.getName());
        holder.studioEssaysNum.setText(studio.getEssays()+" 文章");
        Glide.with(parentContext).load(studio.getAvatarUrl()).into(holder.studioImage);
    }

    public int getItemCount(){
        return studiosList.size();
    }
}
