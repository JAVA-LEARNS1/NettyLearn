package NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class NIOSelector {

	/**
	 * 创建一个多路复用器selector，将serverSocketChannel注册到selector同时监测连接事件OP_ACCEPT
	 * 创建事件的阻塞selector.select() 当没有连接事件时阻塞，一旦有连接进来，阻塞停止
	 * 之后遍历这个连接事件，同时注册读取事件
	 * 这样selector就注册了连接事件和读取事件，当客户端发来消息时，selector.select()阻塞停止，处理读取事件
	 * 
	 * 问题
	 * 没有解决读大数据的问题
	 * 
	 * 引入netty
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ServerSocketChannel serverSocketChannel=ServerSocketChannel.open();
		serverSocketChannel.socket().bind(new InetSocketAddress(9000));
		//设置serverSocketChannel非阻塞
		serverSocketChannel.configureBlocking(false);
		
		//打开Selector处理channel，即创建epoll(linux内核函数)
		//相当epoll_create函数
		Selector selector=Selector.open();
		//把ServerSocketChannel注册到selector上，并且selector对客户端accept连接监视
		//epoll_ctl
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		while(true){
			//阻塞等待处理的事件发生（连接事件SelectionKey.OP_ACCEPT）
			//相当于epoll_wati函数
			selector.select();
			//获取selector中注册的全部事件的SelectionKey 实例
			Set<SelectionKey> selectionKeys=selector.selectedKeys();
			Iterator<SelectionKey> iterator=selectionKeys.iterator();
			
			//遍历SelectionKey对象进行处理
			while(iterator.hasNext()) {
				SelectionKey key=iterator.next();
				//如果是OP_ACCEPT事件，则进行连接获取accept，和事件注册（读取事件）
				if(key.isAcceptable()) {
					ServerSocketChannel server=(ServerSocketChannel)key.channel();
					SocketChannel socketChannel=server.accept();
					socketChannel.configureBlocking(false);
					socketChannel.register(selector, SelectionKey.OP_READ);
					System.out.print("客户端连接成功");
				}else if(key.isReadable()) {//OP_READ 事件
					SocketChannel socketChannel=(SocketChannel)key.channel();
					ByteBuffer bytteBuffer =ByteBuffer.allocate(128);
					int len=socketChannel.read(bytteBuffer);
					if(len>0) {
						System.out.print("接受到数据"+new String(bytteBuffer.array()));
					}else if(len ==-1) {
						System.out.print("客户端断开连接");
						socketChannel.close();
					}
				}
				iterator.remove();
			}
			

		}

	}

}
