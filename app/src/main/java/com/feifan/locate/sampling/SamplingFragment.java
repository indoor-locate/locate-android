package com.feifan.locate.sampling;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.feifan.locate.Constants;
import com.feifan.locate.R;
import com.feifan.locate.ToolbarActivity;
import com.feifan.locate.provider.LocateData.Zone;
import com.feifan.locate.utils.SizeUtils;
import com.feifan.locate.widget.cursorwork.AbsLoaderFragment;
import com.feifan.locate.widget.cursorwork.RecyclerCursorAdapter;
import com.feifan.locate.widget.cursorwork.SimpleCursorAdapter;
import com.feifan.locate.widget.recycler.SpaceItemDecoration;
import com.feifan.locate.sampling.model.ZoneModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class SamplingFragment extends AbsLoaderFragment implements View.OnClickListener {

    // 使用异步加载组件时分配的ID，不能与其他数据使用的ID相同
    private static final int LOADER_ID = 1;

    // 数据适配器
    private SimpleCursorAdapter<ZoneModel> mAdapter;

    public SamplingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sampling, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = findView(R.id.sampling_zone_list);
        mAdapter = new SimpleCursorAdapter<ZoneModel>(ZoneModel.class){
            @Override
            protected void onBindViewHolder(ViewHolder holder, Cursor cursor) {
                super.onBindViewHolder(holder, cursor);
                holder.itemView.setOnClickListener(SamplingFragment.this);
            }
        };
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new SpaceItemDecoration(SizeUtils.dp2px(getContext(), 5)));
    }

    @Override
    protected int getLoaderId() {
        return LOADER_ID;
    }

    @Override
    protected Uri getContentUri() {
        return Zone.CONTENT_URI;
    }

    @Override
    protected <A extends RecyclerCursorAdapter> A getAdapter() {
        return (A)mAdapter;
    }

    @Override
    public void onClick(View v) {
        ZoneModel model = (ZoneModel)v.getTag();
        Constants.setExportParentPathName(model.name);

        Intent intent = new Intent(getContext().getApplicationContext(), ToolbarActivity.class);
        intent.putExtra(ToolbarActivity.EXTRA_KEY_FRAGMENT, SpotPlanFragment.class.getName());

        Bundle args = new Bundle();
        args.putString(LOADER_KEY_SELECTION, "zone=?");
        args.putStringArray(LOADER_KEY_SELECTION_ARGS, new String[]{ String.valueOf(model.id) });
        args.putParcelable(SpotPlanFragment.EXTRA_KEY_ZONE, (Parcelable) v.getTag());
        intent.putExtra(ToolbarActivity.EXTRA_KEY_ARGUMENTS, args);

        startActivity(intent);
    }
}