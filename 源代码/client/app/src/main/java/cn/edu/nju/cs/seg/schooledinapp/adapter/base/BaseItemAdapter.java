package cn.edu.nju.cs.seg.schooledinapp.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * RecyclerView Adapter 基类
 *
 */
public abstract class BaseItemAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    // 上下文
    private Context context;

    // 维护的项目列表
    private List<T> itemList;

    public BaseItemAdapter(List<T> itemList) {
        this.itemList = itemList;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }

        // 填充 itemView
        View itemView = doInflateItemView(LayoutInflater.from(context), parent);

        // 创建 ViewHolder
        VH holder = doCreateViewHolder(itemView, viewType);

        // 绑定事件到 itemView
        doBindItemEvents(itemView, holder);

        return holder;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        T item = itemList.get(position);
        doBindViewHolder(holder, item);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    /**
     * 仅仅填充 item , 绑定事件到 item 请在 doBindItemEvents 中完成
     *
     * @param inflater 填充器
     * @param parent 父组件
     * @return item view
     */
    protected abstract View doInflateItemView(LayoutInflater inflater, ViewGroup parent);

    /**
     * 创建 ViewHolder
     *
     * @param itemView 父上下文
     * @param viewType 类型
     * @return
     */
    protected abstract VH doCreateViewHolder(View itemView, int viewType);

    /**
     * 绑定事件到 itemView
     *
     * @param itemView 欲绑定事件的 item
     * @param holder item 对应的 holder
     */
    protected void doBindItemEvents(View itemView, final VH holder) { }

    /**
     * 绑定 ViewHolder
     *
     * @param holder
     * @param item
     * @return
     */
    protected abstract void doBindViewHolder(VH holder, T item);

    /**
     * 获取上下文
     *
     * @return 上下文
     */
    public Context getContext() {
        return context;
    }

    /**
     * 获取项目列表
     *
     * @return 项目列表
     */
    public List<T> getItemList() {
        return itemList;
    }

    /**
     * 设置 itemList
     *
     * @param itemList 新的列表
     */
    public void setItemList(List<T> itemList) {
        this.itemList = itemList;
    }

}
