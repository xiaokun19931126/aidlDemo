# Android跨进程通信总结
### 前言：

很多时候我们在写demo的时候会觉得很烦，主要是关于view绑定的(又懒得在demo中集成ButterKnife)，我提供一下我的方法。

**1.<a href = "https://github.com/laobie/FindViewByMe">findviewbyme插件</a>**

一款自动帮你集成findViewById的插件，非常好用。但是这款插件并没有像ButterKnife一样提供点击事件的绑定，所以有点遗憾。那么我的方法不得不进行第二步，因为我感觉每次写绑定事件都很烦！

**2.给view设置点击监听**

其实是一个方法：

```
private void initListener(View... views)
{
    for (View view : views)
    {
        view.setOnClickListener(this);
    }
}
```

然后现在代码是这样的：

```
private void initView()
{
    mMessengerBtn = (Button) findViewById(R.id.messenger_btn);
    mAidlBtn = (Button) findViewById(R.id.aidl_btn);
    initListener(mAidlBtn, mMessengerBtn);
}

private void initListener(View... views)
{
    for (View view : views)
    {
        view.setOnClickListener(this);
    }
}
```

但是可以注意到initListener方法是固定不变的，所以可以使用Android Studio提供的模板来提高效率。

模板如下：

![](imgs/initListener.png)

定义好模板好，我们就可以这样：

![](imgs/initListener1.gif)

这样可以快速生成此方法。然后接下来你肯定也想到了onClick方法也可以用模板。

**3.view点击回调onClick()方法**

模板如下：

![](imgs/onClick.png)

定义好模板，使用如下：

![](imgs/onClick1.gif)

**这样利用插件和模板就可以快速生成令人讨厌的findviewById和点击监听。**



### Android中的IPC方式(正文内容全部来自艺术探索书籍)

**1.Bundle**

不多说，这是基本intent在组件间(Activity/Service/BroadcastReceiver)传递Bundle。要注意的是我们传输的数据必须能够被序列化。

**2.文件共享**

**3.Messenger**

**4.AIDL**

**5.ContentProvider**

**6.Socket**



### 如何选择合适IPC方式

| 名称              | 优点                                       | 缺点                                       | 使用场景                               |
| :-------------- | :--------------------------------------- | :--------------------------------------- | :--------------------------------- |
| Bundle          | 简单易用                                     | 只能传输Bundle支持的数据类型                        | 组件间的进程间通信                          |
| 文件共享            | 简单易用                                     | 不适合高并发场景，并且无法做到进程间的即时通信                  | 无并发访问情形，交换简单的数据实时性不高               |
| Messenger       | 功能一般，支持一对多串行通信，支持实时通信                    | 不能很好处理高并发情形，不支持RPC(远程调用方法)，数据通过Messenge进行传输，因此只能传输Bundle支持的数据类型 | 低并发的一对多即时通信，无RPC要求，或者无须要返回后果的RPC要求 |
| AIDL            | 功能强大，支持一对多并发通信，支持实时通信                    | 使用稍复杂，需要处理好线程同步                          | 一对多通信且有RPC要求                       |
| ContentProvider | 在数据源访问方面功能强大，支持一对多并发数据共享，可通过Call方法扩展其他操作 | 可以理解为受约束的AIDL，主要提供数据源的CRUD操作             | 一对多的进程间的数据共享                       |
| Socket          | 功能强大，可以通过网络传输字节流，支持一对多并发实时通信             | 实现细节稍微有点繁琐，不支持直接的RPC                     | 网络数据交换                             |

