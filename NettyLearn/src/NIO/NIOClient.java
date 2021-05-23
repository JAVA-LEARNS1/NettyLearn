package NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class NIOClient {

	public static void main(String[] args) {
		try {
			SocketChannel sc=SocketChannel.open();
			sc.connect(new InetSocketAddress("localhost",9000));
		sc.write(Charset.defaultCharset().encode("hello world 123456789"));
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	
	}
}
