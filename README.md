# NettyLearn
BIO 阻塞IO ：一次只允许一个连接，可以加线程优化，但是当并发大时，线程创建过多会造成内存爆掉，
采用线程池，设置每次处理500个线程，同样当并发大时
假设有10M的请求同时过来，而每次只能处理500个，最后肯定会有大量连接等待超时

NIO 非阻塞IO：客户端的连接不用等待，连接后存到list中，最后遍历list来处理逻辑，会发现一个问题，
当连接数是10万时，需要处理的逻辑只有1条时，仍然会遍历这10万条数据
如果解决，采用多路复用器selector，

selector是基于事件驱动的底层采用的是linux内核函数epoll_create,epoll_ctl,epoll_wait来实现的

epoll_creat:会创建一个基于事件的文件句柄（int类型：epoll的位置，找到这个int就能找到epoll空间）

相当于：Selector selector=Selector.open();

epoll_ctl:调用epoll_ctl向epoll对象中添加socket，由系统中断处理检查socket事件放到rdllist双向链表中

epoll_wait:监视rdllist，有增加时，停止阻塞

相当于selector.select();

这样,将我们的sock注册进selector同时监听连接事件，一旦客户端有连接，就会触发wait，之后遍历事件就可以了，这样就解决了上面的问题
