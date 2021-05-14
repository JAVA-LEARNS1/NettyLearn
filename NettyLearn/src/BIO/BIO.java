package BIO;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BIO {

	/**
	 * BIO 一次只能建立一个连接，之后执行handler
	 *  问题
	 *  1，当前连接，没有执行完时，后面的连接无发去连接
	 * 考虑解决方案
	 * 连接处理加上线程，当读取量不大时可以很快读完，同时不是大量并发时，方法可行
	 * 先不考虑度的速度问题（如果度的数据非常大，会导致阻塞，redis的使用就是不能去读写大的数据）
	 * 大量的并发，会创建大量的线程，线程是有开销的，当线程数达到一定的量后，会发现内存会爆掉，
	 * 可以再试着优化，加入线程池，每次只允许创建500个线程
	 * 对于少量并发可以，但是对于10w 100w的并发，线程池的使用（而且当500线程满了后）此时
	 * 再来连接，会发现还是连不上，
	 * 
	 * 此时引入NIO,非阻塞IO
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ServerSocket serverSocket =new ServerSocket(9000);
		while(true){
			System.out.print("等待连接..");
			//阻塞方法
			Socket clientSocket=serverSocket.accept();
			System.out.print("有客户端连接了...");
			handler(clientSocket);
			
		}
	}
	private static void handler(Socket clientSocket) throws IOException {
		byte[] bytes=new byte[1024];
		System.out.print("准备read..");
		//接受客户端的数据，阻塞方法，没有数据可读时就阻塞
		int read=clientSocket.getInputStream().read(bytes);
		if(read != -1) {
			System.out.print("接受客户端的数据"+new String(bytes,0,read));
		}
	}

}
