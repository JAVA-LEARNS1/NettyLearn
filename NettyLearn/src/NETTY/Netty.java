package NETTY;

public class Netty {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EventLoopGroup bossGroup=new NioEventLoopGroup(1);
		EventLoopGroup workGroup=new NioEventLoopGroup(8);
	}

}
