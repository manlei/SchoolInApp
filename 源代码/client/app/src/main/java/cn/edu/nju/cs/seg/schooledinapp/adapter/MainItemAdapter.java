package cn.edu.nju.cs.seg.schooledinapp.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.model.MainItem;


public class MainItemAdapter
        extends BaseMultiItemQuickAdapter<MainItem, BaseViewHolder> {

    private Context context;

    private static final String TAG = "MainItemAdapter";

    public MainItemAdapter(Context context, List<MainItem> itemList) {
        super(itemList);
        addItemType(MainItem.QUESTION, R.layout.main_body_item_question);
        addItemType(MainItem.ESSAY, R.layout.main_body_item_essay);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, MainItem item) {

        switch (helper.getItemViewType()) {

            case MainItem.QUESTION: {

                helper.setText(R.id.tv_main_body_question_directed_to,
                        (String) item.get(MainItem.QuestionKey.DIREDTED_TO_KEY));
                helper.setText(R.id.tv_main_body_question_title,
                        (String) item.get(MainItem.CommonKey.TITLE_KEY));
                helper.setText(R.id.tv_main_body_question_heat,
                        Integer.toString((int)(double) item.get(MainItem.CommonKey.HEAT_KEY)) + " 热度");
                helper.setText(R.id.tv_main_body_question_supports,
                        Integer.toString((int)(double) item.get(MainItem.CommonKey.SUPPORTS_KEY)) + " 赞同");
                helper.setBackgroundColor(R.id.v_main_body_question_vertical_line,
                        ContextCompat.getColor(context, R.color.verticalLineBlue));

            } break;

            case MainItem.ESSAY: {

                helper.setText(R.id.tv_main_body_essay_studio,
                        (String) item.get(MainItem.EssayKey.STUDIO_KEY));
                helper.setText(R.id.tv_main_body_essay_title,
                        (String) item.get(MainItem.CommonKey.TITLE_KEY));
                helper.setText(R.id.tv_main_body_essay_heat,
                        Integer.toString((int)(double) item.get(MainItem.CommonKey.HEAT_KEY)) + " 热度");
                helper.setText(R.id.tv_main_body_essay_supports,
                        Integer.toString((int)(double) item.get(MainItem.CommonKey.SUPPORTS_KEY)) + " 赞同");
                helper.setText(R.id.tv_main_body_essay_comments,
                        Integer.toString((int)(double) item.get(MainItem.EssayKey.COMMENTS_KEY)) + " 评论");
                helper.setBackgroundColor(R.id.v_main_body_essay_vertical_line,
                        ContextCompat.getColor(context, R.color.verticalLinePink));
            } break;

        }

    }

}
