严振杰的主页：[http://www.yanzhenjie.com](http://www.yanzhenjie.com)  
严振杰的博客：[http://blog.yanzhenjie.com](http://blog.yanzhenjie.com)  

**欢迎加入QQ技术交流群：[46523908](http://jq.qq.com/?_wv=1027&k=40hvC7E)**

关于Fragment、NoFragment的使用看博客：[【2016Ending，2017Starting】NoFragment之Fragment玩法新姿势](http://blog.csdn.net/yanzhenjie1003/article/details/54562328)。

# 特点
1. 支持传统`Fragment`的所有用法。
2. 支持`startFragmentForResult(Fragment)`、`onFragmentResult(int, int, Bundle)`，原生只有Activity。
3. 支持同一个`Fragment`启动多个实例。
5. 支持自动维护`Back Stack`，不会错乱。
6. 支持在`Fragment`中直接`setToolbar()`、`setTitle()`、`displayHomeButton()`。
7. 返回键和`homeButton`自动处理，支持开发者拦截处理。
8. 支持`ActionBar Menu`、溢出`Menu`等。
9. 开发者不用管跳转逻辑、back键处理、Toolbar加载菜单等。

# 使用方法
* Gradle一句话远程依赖
```groovy
compile 'com.yanzhenjie:fragment:1.0.1'
```
* Maven:
```xml
<dependency>
  <groupId>com.yanzhenjie</groupId>
  <artifactId>fragment</artifactId>
  <version>1.0.1</version>
  <type>pom</type>
</dependency>
```

* Eclipse ADT
请放弃治疗。

# 图示
第一种，结合ToolBar、Menu的演示：

<image src="https://github.com/yanzhenjie/NoFragment/blob/master/image/2.gif?raw=true" width="280px"/>

第二种，结合Toolbar、Menu + OverFlower的演示：

<image src="https://github.com/yanzhenjie/NoFragment/blob/master/image/3.gif?raw=true" width="280px"/>

第三种，`startFragmentForResult()`、`onFragmentResult()`演示：

<image src="https://github.com/yanzhenjie/NoFragment/blob/master/image/4.gif?raw=true" width="280px"/>

第四种，不保存的在回退栈的演示：

<image src="https://github.com/yanzhenjie/NoFragment/blob/master/image/5.gif?raw=true" width="280px"/>

# 代码展示
你的宿主`Activity`需要继承`CompatActivity`，然后启动一个`Fragment`：
```java
public class MainActivity extends CompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * 一句话即可，不要怀疑自己的眼睛，这是真的。
         */
        startFragment(MainFragment.class);
    }

    @Override
    protected int fragmentLayoutId() {
        return R.id.fragment_root;
    }

}
```

之后在Fragment中互相跳转，你可以不用管物理Back键被按下之类的：

## 一、以`standard`模式启动一个`Fragment`
```java
startFragment(MoreMenuFragment.class);
```

## 二、以`startActivityForResult()`方式启动一个`Fragment`
```java
// 启动，等待回调结果。
startFragmentForResult(StartResultFragment.class, 100);

// 不论怎样回来都会回调onFragmentResult()。
@Override
public void onFragmentResult(int requestCode, int resultCode, @Nullable Bundle result) {
    switch (requestCode) {
        case 100: {
            if (resultCode == RESULT_OK) {
                // 操作成功：result就是调用的Fragment返回的结果。
            } else if (resultCode == RESULT_CANCELED) {
                // 操作取消。
            }
            break;
        }
    }
}
```

在`StartResultFragment`中如果要返回结果，那么：
```java
Bundle bundle = new Bundle();
bundle.putString("message", result);
setResult(RESULT_OK, bundle);
finish();
```

当然你也不设置，那么`resultCode`的默认值是`RESULT_CANCELED`。

## 三、跳转时带参数
```java
// 封装参数：
Bundle bundle = new Bundle();
bundle.putString("hehe", "呵呵哒");
bundle.putString("meng", "萌萌哒");
bundle.putString("bang", "棒棒哒");
bundle.putString("meme", "么么哒");

// 在Activity中或者Fragment调用此方法：  
NoFragment fragment = fragment(ArgumentFragment.class, bundle);

// 最后启动：
startFragment(fragment);
```

## 四、跳转的`Fragment`不保存在`Back Stack`
这种方式显示的`fragment`中如果调用了其它`fragment`，从其它`fragment`中回来时，这个`fragment`将会跳过，不会显示，也就是说：A-B-C-[back]-A，从A到B，B不加入回退栈，B再到C，C按下返回键，或者调用`finish()`方法，将会直接回到A。
```java
startFragment(StackFragment.class, false);
```

## 五、同一个Fragment，启动多个实例
```java
startFragment(MoreMenuFragment.class);
startFragment(MoreMenuFragment.class);
startFragment(MoreMenuFragment.class);
startFragment(MoreMenuFragment.class);
```
比如我们这里调用四次，那么回退栈中有四个`MoreMenuFragment`，按下返回键时将一个个退出。

## 六、Toolbar菜单的加载和处理
我们知道MD设计中，Toolbar的菜单很好看，而且利用Toolbar也很好加载，那么NoFragment也是完美支持的，当重写了`onCreateOptionsMenu()`方法后，调用`setToolbar(Toolbar)`方法时，将会调用`onCreateOptionsMenu()`方法，此时你就该加载菜单了，当然也只需要一句话。

```java
@Override
public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    // Load your menu.
    inflater.inflate(R.menu.menu_fragment_main, menu);
}
```

当用户点击meun的item时将会回调这个方法，和原生Activity是一样的。
```java
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
```

## 七、Toolbar的返回按钮的处理
在正常开发中给Toolbar设置返回按钮也要好几行代码的，如果使用了NoFragment，那么：
```java
@Override
public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    // 首先设置Toolbar：
    setToolbar(mToolbar);

    // 设置标题：
    setTitle(R.string.title_fragment_main);

    // 显示返回按钮，图标开发者指定：
    displayHomeAsUpEnabled(R.drawable.ic_close_white);
}
```

设置了返回按钮后，用户点击返回按钮将自动杀死当前`Fragment`，当然你也可以拦截用户的返回行为：
```java
@Override
    public boolean onInterceptToolbarBack() {
        // 返回true将拦截，返回false将不拦截。
        return true;
    }
```

# 混淆
```text
-keep public class * extends android.support.v4.app.Fragment
```

# License
```text
Copyright 2017 Yan Zhenjie

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```