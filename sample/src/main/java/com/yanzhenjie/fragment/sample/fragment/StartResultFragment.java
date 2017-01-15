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
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.yanzhenjie.fragment.NoFragment;
import com.yanzhenjie.fragment.sample.R;

/**
 * Created by Yan Zhenjie on 2017/1/15.
 */
public class StartResultFragment extends NoFragment {

    private Toolbar mToolbar;
    private EditText mEditText;
    private Button mBtnBack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mEditText = (EditText) view.findViewById(R.id.edit);
        mBtnBack = (Button) view.findViewById(R.id.btn_back);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setToolbar(mToolbar);

        displayHomeAsUpEnabled(R.drawable.ic_back_white);
        setTitle(R.string.title_fragment_result);

        mBtnBack.setOnClickListener(v -> {
            String result = mEditText.getText().toString();
            if (TextUtils.isEmpty(result)) {
                result = getString(R.string.message_null);
            }
            Bundle bundle = new Bundle();
            bundle.putString("message", result);
            setResult(RESULT_OK, bundle);
            finish();
        });
    }
}
