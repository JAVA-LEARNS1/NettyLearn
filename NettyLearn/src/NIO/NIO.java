package NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NIO {

	//保存客户端连接
	static List<SocketChannel> channellist=new ArrayList();
	/**
	 * NIO非阻塞IO,当有连接时会将连接存到channellist中，之后遍历channellist来去读取数据
	 * 想想大并发时可能出现的问题，读数据慢先不考虑
	 * 
	 * 问题 
	 * 当有10w的连接存在时，只有10个进行了读取操作，程序还是会跑10w遍，
	 * 如何解决
	 * 引入多路复用器selector
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ServerSocketChannel serverSocketChannel=ServerSocketChannel.open();
		serverSocketChannel.socket().bind(new InetSocketAddress(9000));
		//设置serverSocketChannel非阻塞
		serverSocketChannel.configureBlocking(false);

		while(true){
			//非阻塞模式accept方法不会阻塞
			//NIO的非阻塞是由操作系统内部实现的，底层调用了linux内核的accept函数
			SocketChannel socketChannel=serverSocketChannel.accept();
			if(socketChannel!=null) {
				System.out.print("连接成功");
				socketChannel.configureBlocking(false);
				channellist.add(socketChannel);
			}
			//遍历连接进行数据读取
			Iterator<SocketChannel> iterator=channellist.iterator();
			while(iterator.hasNext()) {
				SocketChannel sc=iterator.next();
				ByteBuffer byteBuffer=ByteBuffer.allocate(128);
				int len =sc.read(byteBuffer);
				if(len>0) {
					System.out.print("接受到消息："+new String(byteBuffer.array()));
				}else if(len==-1) {
					iterator.remove();
					System.out.print("客户端断开连接");
				}
			}
		}
	}

}
