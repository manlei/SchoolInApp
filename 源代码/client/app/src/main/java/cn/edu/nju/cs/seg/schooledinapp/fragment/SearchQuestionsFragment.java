package cn.edu.nju.cs.seg.schooledinapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.cs.seg.schooledinapp.R;
import cn.edu.nju.cs.seg.schooledinapp.adapter.SearchQuestionsItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.fragment.base.BaseFragment;
import cn.edu.nju.cs.seg.schooledinapp.model.SearchQuestionItem;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import cn.edu.nju.cs.seg.schooledinapp.service.QuestionsService;
import cn.edu.nju.cs.seg.schooledinapp.util.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchQuestionsFragment extends BaseFragment {

    private static final String TAG = "SearchQuestionsFragment";

    private static QuestionsService questionsService;

    private List<SearchQuestionItem> questionsList = new ArrayList<SearchQuestionItem>();

    private SearchQuestionsItemAdapter adapter;

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_search_questions, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.recycleview_fragment_search_questions);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new SearchQuestionsItemAdapter(questionsList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    /**
     * 创建一个SearchQuestionsFragment
     *
     * @return
     */
    public static SearchQuestionsFragment newFragment(){
        SearchQuestionsFragment fragment = new SearchQuestionsFragment();
        return fragment;
    }

    /**
     * 提供搜索问题的统一接口
     *
     * @param queryText 搜索条件
     */
    public  void searchQuestions(final String queryText){

        if (questionsService == null)
            questionsService = ApiClient.getQuestionsService();

        Call<List<SearchQuestionItem>> call = questionsService.fetch(0,10,queryText);
        call.enqueue(new Callback<List<SearchQuestionItem>>() {
            @Override
            public void onResponse(Call<List<SearchQuestionItem>> call, Response<List<SearchQuestionItem>> response) {
                switch (response.code()){
                    case 200:{

                        //将获取到的用户信息加到用户列表
                        questionsList.clear();
                        for (int i = 0; i < response.body().size(); i++) {
                            questionsList.add(response.body().get(i));
                        }
                        adapter.notifyDataSetChanged();

                    }
                    break;

                    case 404: {
                        Toast.makeText(getContext(),"找不到",Toast.LENGTH_SHORT).show();
                    }
                    break;

                    case 503: {
                        Toast.makeText(getContext(),"服务器忙",Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }

            @Override
            public void onFailure(Call<List<SearchQuestionItem>> call, Throwable t) {
                Toast.makeText(getContext(),"检查网络",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 清空以前的搜索记录
     */
    public void clearQuestions(){
        if (adapter != null) {
            questionsList.clear();
            adapter.notifyDataSetChanged();
        }
        else {
            LogUtil.e(TAG,"question !adpter=null");
        }
    }

}
