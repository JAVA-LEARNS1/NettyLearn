1.EventLoop
 EventLoop 本质是一个单线程执行器（同时维护了一个Selector），里面有run方法处理channel上IO事件
 继承关系比较复杂
 1）继承自java.util.current.ScheduledExecutorService因此包含类线程池中所有的方法
 2）继承自netty自己的EventExecutor
 
 事件循环组
 EventLoopGroup是一组EventLoop，Channel一般会调用EventLoopGroup的register方法来绑定其中一个
 EvetnLoop，后续这个Channel山的io事件都有此Eventloop来处理（保证了io事件处理的线程安全）

NioEventLoopGroup->MultithreadEventLoopGroup->MultithreadEventExecutorGroup
里面有方法children[i] = newChild(executor, args);->NioEventLoopGroup
(protected EventLoop newChild(Executor executor, Object... args))
->(return new NioEventLoop)
里面有（final SelectorTuple selectorTuple = openSelector();）相当于NIO Selector.create()
->SingleThreadEventLoop->SingleThreadEventExecutor(this.executor = ThreadExecutorMap.apply(executor, this);)
->ThreadExecutorMap(executor.execute(apply(command, eventExecutor)))->SingleThreadEventExecutor
(public void execute(Runnable task) )->(startThread();)->(doStartThread)
->(SingleThreadEventExecutor.this.run();)->NioEventLoop(protected void run())
里面有(case SelectStrategy.SELECT:)(select(wakenUp.getAndSet(false));) 相当于NIO Selector.select(xx) 阻塞方法
