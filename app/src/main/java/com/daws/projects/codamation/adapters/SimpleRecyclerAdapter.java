package com.daws.projects.codamation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SimpleRecyclerAdapter<T> extends RecyclerView.Adapter<SimpleRecyclerAdapter.SimpleViewHolder> {

    protected List<T> mainData;
    private SimpleRecyclerAdapter.OnViewHolder<T> listener;
    private @LayoutRes
    int layoutRes;

    public SimpleRecyclerAdapter(List<T> mainData, int layoutRes, SimpleRecyclerAdapter.OnViewHolder<T> listener){
        this.mainData = mainData;
        this.layoutRes = layoutRes;
        this.listener = listener;
    }

    public SimpleRecyclerAdapter(int layoutRes, SimpleRecyclerAdapter.OnViewHolder<T> listener){
        this.layoutRes = layoutRes;
        this.listener = listener;
    }

    public interface OnViewHolder<T>{
        void onBindView(SimpleViewHolder holder, T item);
    }


    public static class SimpleViewHolder extends RecyclerView.ViewHolder{

        private SimpleRecyclerAdapter.OnViewHolder listener;
        private ViewDataBinding layoutBinding;

        public SimpleViewHolder(View itemView, SimpleRecyclerAdapter.OnViewHolder listener) {
            super(itemView);

            try {
                this.listener = listener;
                layoutBinding = DataBindingUtil.bind(itemView);
            } catch (IllegalArgumentException e){
                e.printStackTrace();
            }
        }

        public ViewDataBinding getLayoutBinding() {
            return layoutBinding;
        }
    }

    @NonNull
    @Override
    public SimpleRecyclerAdapter.SimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutRes(), parent, false);
        return new SimpleViewHolder(view, getListener());
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleRecyclerAdapter.SimpleViewHolder holder, int position) {
        T t = mainData.get(position);
        getListener().onBindView(holder, t);
    }

    @Override
    public int getItemCount() {
        return mainData == null ? 0 : mainData.size();
    }

    public void setListener(OnViewHolder<T> listener) {
        this.listener = listener;
    }

    public OnViewHolder<T> getListener() {
        return listener;
    }

    public void setLayoutRes(int layoutRes) {
        this.layoutRes = layoutRes;
    }

    public int getLayoutRes() {
        return layoutRes;
    }

    public T getItemBy(T t){
        for (T data : mainData){
            if (mainData.equals(data))
                return data;
        }
        return null;
    }

    public T getItemAt(int position){
        return this.mainData.get(position);
    }

    public void addItemAt(final T data, final int position){
        this.mainData.add(position, data);
        notifyItemInserted(position);
    }

    public void addItem(final T data){
        addItemAt(data, mainData.size());
        notifyDataSetChanged();
    }

    public void addAllItem(final List<T> dataList){
        this.mainData.addAll(dataList);
        notifyDataSetChanged();
    }

    public void removeItemAt(final T data, final int position){
        this.mainData.remove(position);
        notifyItemRemoved(position);
    }

    public void removeItem(final T data){
        this.mainData.remove(data);
        notifyDataSetChanged();
    }

    public void removeByPosition(final int position){
        this.mainData.remove(position);
        notifyItemRemoved(position);
    }

    public void removeByList(final List<T> dataList){
        for (T data : dataList){
            this.mainData.remove(data);
        }
        notifyDataSetChanged();
    }

    public void removeAll(){
        this.mainData.clear();
        notifyDataSetChanged();
    }

    public List<T> getMainData(){
        return mainData;
    }

    public void setMainData(List<T> mainData) {
        this.mainData = mainData;
        notifyDataSetChanged();
    }
}
