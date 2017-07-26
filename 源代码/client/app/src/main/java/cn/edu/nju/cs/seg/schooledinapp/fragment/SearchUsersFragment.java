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
import cn.edu.nju.cs.seg.schooledinapp.adapter.SearchUsersItemAdapter;
import cn.edu.nju.cs.seg.schooledinapp.fragment.base.BaseFragment;
import cn.edu.nju.cs.seg.schooledinapp.model.SearchUserItem;
import cn.edu.nju.cs.seg.schooledinapp.service.ApiClient;
import cn.edu.nju.cs.seg.schooledinapp.service.UsersService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchUsersFragment extends BaseFragment {

    private static final String TAG = "SearchUsersFragment";

    private static UsersService usersService;

    private  List<SearchUserItem> usersList = new ArrayList<SearchUserItem>();

    private SearchUsersItemAdapter adapter;

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_search_users, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.recycleview_fragment_search_users);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new SearchUsersItemAdapter(usersList);
        recyclerView.setAdapter(adapter);
        return view;
    }

    /**
     * 创建一个searchFragment
     *
     * @return SearchFragment
     */
    public static SearchUsersFragment newFragment(){
        SearchUsersFragment fragment = new SearchUsersFragment();
        return  fragment;
    }

    /**
     *外部搜索用户的统一接口
     *
     * @param queryText 搜索条件
     * @return
     */
    public void searchUsers(String queryText){

        if (usersService == null)
            usersService = ApiClient.getUsersService();

        Call<List<SearchUserItem>> call = usersService.fetch(0,10,queryText);
        call.enqueue(new Callback<List<SearchUserItem>>() {
            @Override
            public void onResponse(Call<List<SearchUserItem>> call, Response<List<SearchUserItem>> response) {

                switch (response.code()){
                    case 200:{

                        //将获取到的用户信息加到用户列表
                        usersList.clear();
                        usersList.addAll(response.body());
                        adapter.notifyDataSetChanged();

                    } break;

                    case 404: {
                        Toast.makeText(getContext(),"找不到",Toast.LENGTH_SHORT).show();
                    } break;

                    case 503: {
                        Toast.makeText(getContext(),"服务器忙",Toast.LENGTH_SHORT).show();
                    } break;
                }

            }

            @Override
            public void onFailure(Call<List<SearchUserItem>> call, Throwable t) {
                Toast.makeText(getContext(),"检查网络",Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 清空以前的搜索记录
     */
    public void clearUsers(){
        usersList.clear();
        adapter.notifyDataSetChanged();
    }

}
