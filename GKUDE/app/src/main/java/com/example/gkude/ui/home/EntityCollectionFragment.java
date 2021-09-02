package com.example.gkude.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gkude.EntityViewActivity;
import com.example.gkude.Manager;
import com.example.gkude.R;
import com.example.gkude.adapter.EntityCollectionAdapter;
import com.example.gkude.bean.EntityBean;
import com.orm.SugarContext;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class EntityCollectionFragment extends Fragment implements EntityCollectionAdapter.OnEntitySelectedListener {

    private static String TAG = new String();
    private Observer<List<EntityBean>> observer = null;
    private List<EntityBean> entityList = new LinkedList<>();
    private EntityCollectionAdapter mAdapter;

    public EntityCollectionFragment(final String tag) {
        Log.e("EntityCollectionFragment", TAG);
        this.TAG = tag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("EntityCollectionFragment", "Create a news fragment");
        super.onCreate(savedInstanceState);
        SugarContext.init(getContext());
        initObserver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_entity_collection, container, false);

        // Set layout manager
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        // Set adapter for recyclerView
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new EntityCollectionAdapter(entityList, this);
        recyclerView.setAdapter(mAdapter);

        return rootView;
    }

    private void initObserver() {
        observer = new Observer<List<EntityBean>>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG,"observer subscribed");
            }
            @Override
            public void onNext(List<EntityBean> entities) {
                Log.e(TAG,"getList");
                mAdapter.setEntityList(entities);
            }
            @Override
            public void onError(Throwable e) {
            }
            @Override
            public void onComplete() {
                Log.e(TAG,"Complete");
            }
        };
        // TODO(zixuanyi): Manage.refresh_n
        Log.e("EntityCollectionFragment", "libai got here ");
        Manager.searchEntity(TAG, "李白", observer);
        // Get information according to the TAG
        //Manager.refresh_n(TAG, observer);
    }

    @Override
    public void onEntitySelected(EntityBean entity) {
        if(!entity.isVisited()){
            entity.save();
        }

        // Go to the detailed page
        Intent intent = new Intent(getActivity(), EntityViewActivity.class);
        intent.putExtra("entity_id", entity.getId());
        startActivity(intent);
    }
}