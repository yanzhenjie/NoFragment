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
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.yanzhenjie.alertdialog.AlertDialog;
import com.yanzhenjie.fragment.NoFragment;
import com.yanzhenjie.fragment.sample.R;

/**
 * Created by Yan Zhenjie on 2017/1/15.
 */
public class MainFragment extends NoFragment implements View.OnClickListener {

    private Toolbar mToolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        view.findViewById(R.id.btn_menu_more).setOnClickListener(this);
        view.findViewById(R.id.btn_argument).setOnClickListener(this);
        view.findViewById(R.id.btn_for_result).setOnClickListener(this);
        view.findViewById(R.id.btn_stack).setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // First must set toolbar, will invoke: onCreateOptionsMenu();
        setToolbar(mToolbar);

        // Set title for toolbar:
        setTitle(R.string.title_fragment_main);

        // Display close button.
        displayHomeAsUpEnabled(R.drawable.ic_close_white);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_menu_more: {
                startFragment(MoreMenuFragment.class);
                break;
            }
            case R.id.btn_for_result: {
                startFragmentForResquest(StartResultFragment.class, 100);
                break;
            }
            case R.id.btn_argument: {
                Bundle bundle = new Bundle();
                bundle.putString("hehe", "呵呵哒");
                bundle.putString("meng", "萌萌哒");
                bundle.putString("bang", "棒棒哒");
                bundle.putString("meme", "么么哒");

                // Create fragment_menu for bundle.
                NoFragment fragment = fragment(ArgumentFragment.class, bundle);

                startFragment(fragment);
                break;
            }
            case R.id.btn_stack: {
                // Second argument false: don't join the back stack.
                startFragment(StackFragment.class, false);
                break;
            }
        }
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, @Nullable Bundle result) {
        switch (requestCode) {
            case 100: {
                if (resultCode == RESULT_OK) {
                    String message = result.getString("message");
                    AlertDialog.build(getContext())
                            .setCancelable(true)
                            .setTitle(R.string.result)
                            .setMessage(message)
                            .setPositiveButton(R.string.ok, (dialog, which) -> {
                                // TODO nothing.
                            })
                            .show();
                } else if (resultCode == RESULT_CANCELED) {
                    Snackbar.make(mToolbar, R.string.message_canceled, Snackbar.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    // ========================= Menu Sample ========================= //

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Load your menu.
        inflater.inflate(R.menu.menu_fragment_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle menu item click.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings: {
                Snackbar.make(mToolbar, R.string.action_settings, Snackbar.LENGTH_SHORT).show();
                break;
            }
            case R.id.action_exit: {
                Snackbar.make(mToolbar, R.string.action_exit, Snackbar.LENGTH_SHORT).show();
                break;
            }
        }
        return true;
    }

    // ========================= Close Button ========================= //

    @Override
    public boolean onInterceptToolbarBack() {
        // Intercept close button click event.
        Snackbar.make(mToolbar, R.string.intercept_close, Snackbar.LENGTH_SHORT).show();
        return true;
    }
}
