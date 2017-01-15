/*
 * Copyright © Yan Zhenjie. All Rights Reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanzhenjie.fragment.sample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yanzhenjie.fragment.NoFragment;
import com.yanzhenjie.fragment.sample.R;
import com.yanzhenjie.fragment.sample.adapter.RecyclerAdapter;

/**
 * Created by Yan Zhenjie on 2017/1/15.
 */
public class MoreMenuFragment extends NoFragment {

    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // First must set toolbar, will invoke: onCreateOptionsMenu();
        setToolbar(mToolbar);

        // Set title for toolbar:
        setTitle(R.string.title_fragment_setting);

        // Display close button.
        displayHomeAsUpEnabled(R.drawable.ic_back_white);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(new RecyclerAdapter(getContext(), 100));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Load your menu.
        inflater.inflate(R.menu.menu_fragment_setting, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu item click.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add_friend: {
                Snackbar.make(mRecyclerView, R.string.action_add_friend, Snackbar.LENGTH_SHORT).show();
                break;
            }
            case R.id.action_satisfied: {
                Snackbar.make(mRecyclerView, R.string.action_satisfied, Snackbar.LENGTH_SHORT).show();
                break;
            }
            case R.id.action_dissatisfied: {
                Snackbar.make(mRecyclerView, R.string.action_dissatisfied, Snackbar.LENGTH_SHORT).show();
                break;
            }
            case R.id.action_neutral: {
                Snackbar.make(mRecyclerView, R.string.action_neutral, Snackbar.LENGTH_SHORT).show();
                break;
            }

        }
        return true;
    }

    @Override
    public boolean onInterceptToolbarBack() {
        Toast.makeText(getContext(), R.string.no_intercept_close, Toast.LENGTH_SHORT).show();
        return false;
    }

}
