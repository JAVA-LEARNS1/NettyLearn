package com.netty.NettyLearnMaven;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelloServer {

	public static void main(String[] args) {
		// 
		EventLoopGroup group =new DefaultEventLoopGroup();//和NioEventLoopGroup,区别，Nio可以处理io事件，它只能处理普通事件，和定时事件
		//1.启动器，负责组装netty组件，启动服务器
		new ServerBootstrap()
		//2.BossEventLoop,WorkerEventLoop(selector,thread),group组
		//第一eventloopgroup 监听ServerSocketChannel的accept事件
		//第二个eventloopgroup，监听SocketChannel的io事件
		//因为eventloop和channel有绑定关系
		//而服务器只有一个ServerSocketChannle,所以线程参数不设也没关系，只绑定一个
		//workdereventloop视情况而定，不设的化默认会取当前cpu核数*2，最小是1
		.group(new NioEventLoopGroup(),new NioEventLoopGroup(2))
		//3.选择服务器的 ServerSocketChannel 实现
		.channel(NioServerSocketChannel.class)
		//4.boss 负责处理连接 worker(child)负责读写，决定了 worker(child)能执行的事件
		.childHandler(
				//5.channel 代表和客户端进行数据读写的通道 Initializer初始化
				new ChannelInitializer<NioSocketChannel>() {

					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
						//6.添加具体handler
						ch.pipeline().addLast(new StringDecoder());
						//handle1使用的workerEventLoopGroup分配的线程
						//workerEventLoopGroup肯定监听不止一个channel，所以当handle1是一个非常耗时的操作时
						//其他channel会被影响
						//解决办法可以自己建立一个EventLoopGoup,来处理耗时事件
						//举例 建立一个handle2，不使用workerEventLoopGroup分配的线程
						ch.pipeline().addLast("handle1",new ChannelInboundHandlerAdapter() {
							@Override
							public void channelRead(ChannelHandlerContext ctx, Object msg){
								System.out.println(msg);
							}
						}).addLast(group,"handle2",new ChannelInboundHandlerAdapter() {
							@Override
							public void channelRead(ChannelHandlerContext ctx, Object msg){
								System.out.println(msg+"handle2");
							}
						});
						
					}
					
				})
		//7.绑定监听端口
		.bind(8000);
	}

}
