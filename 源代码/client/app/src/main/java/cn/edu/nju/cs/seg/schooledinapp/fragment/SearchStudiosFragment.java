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
import cn.edu.nju.cs.seg.schooledinapp.adapter.SearchStudiosItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.fragment.base.BaseFragment;
import cn.edu.nju.cs.seg.schooledinapp.model.SearchStudioItem;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import cn.edu.nju.cs.seg.schooledinapp.service.StudiosService;
import cn.edu.nju.cs.seg.schooledinapp.util.LogUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */

public class SearchStudiosFragment extends BaseFragment {

    private static final String TAG = "SearchStudiosFragment";

    private static StudiosService studiosService;

    private  List<SearchStudioItem> studiosList = new ArrayList<SearchStudioItem>();

    private SearchStudiosItemAdapter adapter;

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_search_studios, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.recycleview_fragments_search_studios);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new SearchStudiosItemAdapter(studiosList);
        recyclerView.setAdapter(adapter);
        return view;
    }


    /**
     * 创建一个SearchStudiosFragment
     *
     * @return
     */
    public static SearchStudiosFragment newFragment(){
        SearchStudiosFragment fragment = new SearchStudiosFragment();
        return fragment;
    }

    /**
     * 搜索studios的公用方法
     *
     * @param queryText 搜索条件
     */
    public void searchStudios(String queryText) {

        if ( studiosService == null)
            studiosService = ApiClient.getStudiosService();

        Call<List<SearchStudioItem>> call = studiosService.fetch(0, 10,queryText);
        call.enqueue(new Callback<List<SearchStudioItem>>() {
            @Override
            public void onResponse(Call<List<SearchStudioItem>> call, Response<List<SearchStudioItem>> response) {
                switch (response.code()) {
                    case 200: {

                        //将获取到的用户信息加到用户列表
                        studiosList.clear();
                        for (int i = 0; i < response.body().size(); i++) {
                            studiosList.add(response.body().get(i));
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
            public void onFailure(Call<List<SearchStudioItem>> call, Throwable t) {
                Toast.makeText(getContext(),"检查网络",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 清空以前的搜索记录
     */
    public void clearStudios(){

        //找不到原因为什么adapter会被情况
        if (adapter != null) {
            studiosList.clear();
            adapter.notifyDataSetChanged();
        }
        else {
            LogUtil.e("searchuserfragment:","adapter is null!I do not konw why");
        }
    }

}
