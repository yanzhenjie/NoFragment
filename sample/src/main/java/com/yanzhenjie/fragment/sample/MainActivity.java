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
package com.yanzhenjie.fragment.sample;

import android.os.Bundle;

import com.yanzhenjie.fragment.CompatActivity;
import com.yanzhenjie.fragment.sample.fragment.MainFragment;


/**
 * Created by Yan Zhenjie on 2017/1/15.
 */
public class MainActivity extends CompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * 继承CompatActivity后，显示一个fragment就这么简单，不要怀疑自己的眼睛，这是真的。
         */
        startFragment(MainFragment.class);
    }

    @Override
    protected int fragmentLayoutId() {
        return R.id.fragment_root;
    }

}
