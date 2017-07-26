package cn.edu.nju.cs.seg.schooledinapp.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.activity.QuestionActivity;
import cn.edu.nju.cs.seg.schooledinapp.model.SearchQuestionItem;

/**
  * Author: manley
  * Description: 搜索问题的配适器，对应的是SearchQuestion和search_questions_item
  * Created at: 2017/4/28 21:16
  */
public class SearchQuestionsItemAdapter extends RecyclerView.Adapter<SearchQuestionsItemAdapter.ViewHolder> {

    private Context parentContext;

    private int questionId;

    private List<SearchQuestionItem> questionsList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout rlSearchQuestionItem;
        TextView questionTitle;
        TextView questionTime;
        View vVerticalSeparator;

        public ViewHolder(View view){
            super(view);
            rlSearchQuestionItem = (RelativeLayout) view.findViewById(R.id.rl_search_questions_item);
            questionTitle = (TextView)view.findViewById(R.id.tv_search_questions_item_title);
            questionTime = (TextView)view.findViewById(R.id.tv_search_questions_item_time);
            vVerticalSeparator = (View) view.findViewById(R.id.v_search_question_vertical_line);
        }
    }

    public SearchQuestionsItemAdapter(List<SearchQuestionItem> questionsList){
        this.questionsList = questionsList;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(parentContext == null)
            parentContext = parent.getContext();
        View view = LayoutInflater.from(parentContext).inflate(R.layout.search_item_questions,parent,false);

        //设置holder的监听事件，点击即进入另外一个页面
        final ViewHolder holder = new ViewHolder(view);
        holder.rlSearchQuestionItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                questionId = questionsList.get(holder.getAdapterPosition()).getId();
                QuestionActivity.startActivityWithParameters(parentContext,questionId);
            }
        });
        return holder;
    }

     /**
      * 绑定每个view的数据，将view的数据填充进来
      *
      * @param holder
      * @param position
      *
      */
    public void onBindViewHolder(ViewHolder holder,int position){
        SearchQuestionItem question = questionsList.get(position);

        if (position % 2 == 0) {
            holder.vVerticalSeparator.setBackgroundColor(
                    ContextCompat.getColor(parentContext, R.color.verticalLinePink));
        } else {
            holder.vVerticalSeparator.setBackgroundColor(
                    ContextCompat.getColor(parentContext, R.color.verticalLineBlue));
        }

        holder.questionTitle.setText(question.getTitle());
        SimpleDateFormat format =  new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        String time = format.format(question.getCreatedAt());
        holder.questionTime.setText(time);
    }

    public int getItemCount(){
        return questionsList.size();
    }
}
