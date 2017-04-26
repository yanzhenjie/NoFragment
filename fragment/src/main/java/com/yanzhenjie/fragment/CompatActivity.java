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
package com.yanzhenjie.fragment;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>Must extends CompatActivity.</p>
 * Created by Yan Zhenjie on 2017/1/13.
 */
public abstract class CompatActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_INVALID = -1;

    private FragmentManager mFManager;
    private AtomicInteger mAtomicInteger = new AtomicInteger();
    private List<NoFragment> mFragmentStack = new ArrayList<>();
    private Map<NoFragment, FragmentStackEntity> mFragmentEntityMap = new HashMap<>();

    static class FragmentStackEntity {
        private FragmentStackEntity() {
        }

        private boolean isSticky = false;
        private int requestCode = REQUEST_CODE_INVALID;
        @ResultCode
        int resultCode = RESULT_CANCELED;
        Bundle result = null;
    }

    public final <T extends NoFragment> T fragment(Class<T> fragmentClass) {
        //noinspection unchecked
        return (T) Fragment.instantiate(this, fragmentClass.getName());
    }

    public final <T extends NoFragment> T fragment(Class<T> fragmentClass, Bundle bundle) {
        //noinspection unchecked
        return (T) Fragment.instantiate(this, fragmentClass.getName(), bundle);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFManager = getSupportFragmentManager();
    }

    /**
     * Show a fragment.
     *
     * @param clazz fragment class.
     */
    public final <T extends NoFragment> void startFragment(Class<T> clazz) {
        try {
            NoFragment targetFragment = clazz.newInstance();
            startFragment(null, targetFragment, true, REQUEST_CODE_INVALID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Show a fragment.
     *
     * @param clazz       fragment class.
     * @param stickyStack sticky to back stack.
     */
    public final <T extends NoFragment> void startFragment(Class<T> clazz, boolean stickyStack) {
        try {
            NoFragment targetFragment = clazz.newInstance();
            startFragment(null, targetFragment, stickyStack, REQUEST_CODE_INVALID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Show a fragment.
     *
     * @param targetFragment fragment to display.
     * @param <T>            {@link NoFragment}.
     */
    public final <T extends NoFragment> void startFragment(T targetFragment) {
        startFragment(null, targetFragment, true, REQUEST_CODE_INVALID);
    }

    /**
     * Show a fragment.
     *
     * @param targetFragment fragment to display.
     * @param stickyStack    sticky back stack.
     * @param <T>            {@link NoFragment}.
     */
    public final <T extends NoFragment> void startFragment(T targetFragment, boolean stickyStack) {
        startFragment(null, targetFragment, stickyStack, REQUEST_CODE_INVALID);
    }

    /**
     * Show a fragment for result.
     *
     * @param clazz       fragment to display.
     * @param requestCode requestCode.
     * @param <T>         {@link NoFragment}.
     * @deprecated use {@link #startFragmentForResult(Class, int)} instead.
     */
    @Deprecated
    public final <T extends NoFragment> void startFragmentForResquest(Class<T> clazz, int requestCode) {
        startFragmentForResult(clazz, requestCode);
    }

    /**
     * Show a fragment for result.
     *
     * @param targetFragment fragment to display.
     * @param requestCode    requestCode.
     * @param <T>            {@link NoFragment}.
     * @deprecated use {@link #startFragmentForResult(NoFragment, int)} instead.
     */
    @Deprecated
    public final <T extends NoFragment> void startFragmentForResquest(T targetFragment, int requestCode) {
        startFragmentForResult(targetFragment, requestCode);
    }

    /**
     * Show a fragment for result.
     *
     * @param clazz       fragment to display.
     * @param requestCode requestCode.
     * @param <T>         {@link NoFragment}.
     */
    public final <T extends NoFragment> void startFragmentForResult(Class<T> clazz, int requestCode) {
        if (requestCode == REQUEST_CODE_INVALID)
            throw new IllegalArgumentException("The requestCode must be positive integer.");
        try {
            NoFragment targetFragment = clazz.newInstance();
            startFragment(null, targetFragment, true, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Show a fragment for result.
     *
     * @param targetFragment fragment to display.
     * @param requestCode    requestCode.
     * @param <T>            {@link NoFragment}.
     */
    public final <T extends NoFragment> void startFragmentForResult(T targetFragment, int requestCode) {
        if (requestCode == REQUEST_CODE_INVALID)
            throw new IllegalArgumentException("The requestCode must be positive integer.");
        startFragment(null, targetFragment, true, requestCode);
    }

    /**
     * Show a fragment.
     *
     * @param thisFragment Now show fragment, can be null.
     * @param thatFragment fragment to display.
     * @param stickyStack  sticky back stack.
     * @param requestCode  requestCode.
     * @param <T>          {@link NoFragment}.
     */
    protected final <T extends NoFragment> void startFragment(T thisFragment, T thatFragment,
                                                              boolean stickyStack, int requestCode) {
        FragmentTransaction fragmentTransaction = mFManager.beginTransaction();
        if (thisFragment != null) {
            FragmentStackEntity thisStackEntity = mFragmentEntityMap.get(thisFragment);
            if (thisStackEntity != null) {
                if (thisStackEntity.isSticky) {
                    thisFragment.onPause();
                    thisFragment.onStop();
                    fragmentTransaction.hide(thisFragment);
                } else {
                    fragmentTransaction.remove(thisFragment).commit();
                    fragmentTransaction.commitNow();
                    fragmentTransaction = mFManager.beginTransaction();

                    mFragmentEntityMap.remove(thisFragment);
                    mFragmentStack.remove(thisFragment);
                }
            }
        }

        String fragmentTag = thatFragment.getClass().getSimpleName() + mAtomicInteger.incrementAndGet();
        fragmentTransaction.add(fragmentLayoutId(), thatFragment, fragmentTag);
        fragmentTransaction.addToBackStack(fragmentTag);
        fragmentTransaction.commit();

        FragmentStackEntity fragmentStackEntity = new FragmentStackEntity();
        fragmentStackEntity.isSticky = stickyStack;
        fragmentStackEntity.requestCode = requestCode;
        thatFragment.setStackEntity(fragmentStackEntity);
        mFragmentEntityMap.put(thatFragment, fragmentStackEntity);

        mFragmentStack.add(thatFragment);
    }

    /**
     * When the back off.
     */
    protected final boolean onBackStackFragment() {
        if (mFragmentStack.size() > 1) {
            mFManager.popBackStack();
            NoFragment inFragment = mFragmentStack.get(mFragmentStack.size() - 2);

            FragmentTransaction fragmentTransaction = mFManager.beginTransaction();
            fragmentTransaction.show(inFragment);
            fragmentTransaction.commit();

            NoFragment outFragment = mFragmentStack.get(mFragmentStack.size() - 1);
            inFragment.onResume();

            FragmentStackEntity stackEntity = mFragmentEntityMap.get(outFragment);
            mFragmentStack.remove(outFragment);
            mFragmentEntityMap.remove(outFragment);

            if (stackEntity.requestCode != REQUEST_CODE_INVALID) {
                inFragment.onFragmentResult(stackEntity.requestCode, stackEntity.resultCode, stackEntity.result);
            }
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (!onBackStackFragment()) {
            finish();
        }
    }

    /**
     * Should be returned to display fragments id of {@link android.view.ViewGroup}.
     *
     * @return resource id of {@link android.view.ViewGroup}.
     */
    protected abstract
    @IdRes
    int fragmentLayoutId();

}