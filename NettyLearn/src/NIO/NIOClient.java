package NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

public class NIOClient {

	public static void main(String[] args) {
		try {
			SocketChannel sc=SocketChannel.open();
			sc.connect(new InetSocketAddress("localhost",9000));
			SocketAddress address=sc.getLocalAddress();
			System.out.print("watting:"+address);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	
	}
}
