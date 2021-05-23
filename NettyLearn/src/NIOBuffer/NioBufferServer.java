package NIOBuffer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;


public class NioBufferServer {

	public static void main(String[] args) throws IOException {
		ServerSocketChannel ssc=ServerSocketChannel.open();
		ssc.configureBlocking(false);
		
		Selector selector=Selector.open();
		ssc.register(selector, SelectionKey.OP_ACCEPT);
		
		ssc.bind(new InetSocketAddress(8090));
		while(true) {
			selector.select();
			
			Iterator<SelectionKey> keys=selector.selectedKeys().iterator();
			while(keys.hasNext()) {
				SelectionKey key=keys.next();
				keys.remove();
				if(key.isAcceptable()) {
					System.out.println("connected");
					SocketChannel sc=ssc.accept();
				
					sc.configureBlocking(false);
					StringBuilder sb =new StringBuilder();
					for(int i=0;i<3000000;i++) {
						sb.append("a");
					}
					ByteBuffer buffer=Charset.defaultCharset().encode(sb.toString());
					//可能一次写不完（从buffer度向channel写） write是实际写的数量
					while(buffer.hasRemaining()) {
						int write=sc.write(buffer);
						System.out.println(write);
					}
				}
			}
		}

	}

}
