# liren91
这是一个91liren.com的Android客户端，因为没有服务器端的API，所以数据是用Jsoup从
网页上抓取的。。。


界面截图2张

![image](https://github.com/smallgp/liren91/blob/master/p1.png)
![image](https://github.com/smallgp/liren91/blob/master/p2.png)

这个APP里，用了Jsoup从网站上更新数据，然后通过ListView，和ViewBinder显示出来，
刷新界面用的是自带的SwipRefreshLayout，登录和退出时通过Cookie判断的。用了一个
队列来缓存list里的item，每次向下滑到底时，会从队列取出6个item，接着后台再补6个，
这样滑动比较光滑。每次退出时会保存6个item在手机里，这样下次一启动可以有内容显示
不至于太空白。

另外写的过程中，发现了2个疑似bug，一个是写SwipRefreshLayout的xml文件时，无论怎么
嵌套，哪怕不嵌套，实际运行下拉时那个指示器都会卡，但只要嵌套中有个空的ListView就
没问题了。
另一个bug是在5.0系统下ExpandableListView的打开和关闭的listener会出现首次事件不响应，
但在4.1系统下就正常了。

我知道写的不怎么好，但是题主很萌哒，毕竟题主不匿名哦，所以，欢迎评论啦。




非常感谢你的到来very much!
