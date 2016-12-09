package sl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.jni.Address;
import org.apache.tomcat.jni.Library;
import org.apache.tomcat.jni.Sockaddr;

import resources.ApplicationProperties;


public class Socket implements ISocket {

	private static java.util.HashMap<java.lang.Long, ServerSocketClass> serverSockets =new  HashMap<java.lang.Long, ServerSocketClass>();
	private static java.util.HashMap<java.lang.Long, ClientSocketClass> sockets =new  HashMap<java.lang.Long, ClientSocketClass>();
	private static java.lang.String errorMessage;
	
	
	private static Socket instance;

	private Socket() {
		super();
	}

	public static Socket getModule() {
		if (instance == null) {
			synchronized (Socket.class) {
				if (instance == null) {
					instance = new Socket();
				}
			}
		}
		return instance;
	}
	

	@Override
	public java.lang.String accept(Object... args) {
		
		java.lang.Long ssocket = (java.lang.Long) args[0];
		
		ServerSocketClass serverSocket = serverSockets.get(ssocket);
		if(ssocket == null)
		{
			System.out.println(ERROR_SOCKET_ACCEPT_UNKNOWN_SOCKET);
			errorMessage=ERROR_SOCKET_ACCEPT_UNKNOWN_SOCKET;
			return null;
		}
		java.lang.String port=serverSocket.accept(false, false, false, null);
		return port;
		
	}

	@Override
	public Object connect(Object... args) {
		java.lang.String host = (java.lang.String) args[0];
		java.lang.String service = (java.lang.String) args[1];
		
		int port=0;
		if (service.matches(".*[a-z].*")) { 
			try {
				 Library.initialize("tcnative-1");
				 long sa = Address.info(Address.APR_ANYADDR, org.apache.tomcat.jni.Socket.APR_INET, 0, 0,
		                    Library.globalPool());
		            Sockaddr addr = new Sockaddr();
		            Address.getservbyname(sa, service);
		            Address.fill(addr, sa);
		            port=addr.port;
			} catch (Exception e) {
				System.out.println(ERROR_SOCKET_LISTEN_UNKNOWN_SERVICE_NAME);
				errorMessage = ERROR_SOCKET_LISTEN_UNKNOWN_SERVICE_NAME;
				e.printStackTrace();
			}
		}
		else 
			port = java.lang.Integer.valueOf(service);
		
		java.net.InetSocketAddress ipaddress = new java.net.InetSocketAddress(host,port);
		ClientSocketClass cliSocket = new ClientSocketClass(host, port);
		sockets.put(java.lang.Long.valueOf(port), cliSocket);
		return java.lang.Long.valueOf(port);
		
	}

	@Override
	public void disconnect(Object... args) {
		java.lang.Long toBeClosed = (java.lang.Long) args[0];
		
		if(serverSockets.containsKey(toBeClosed))
			serverSockets.remove(toBeClosed);
		if(sockets.containsKey(toBeClosed))
			sockets.remove(toBeClosed);
		
	}

	@Override
	public Object error(Object... args) {
		return errorMessage;
	}

	@Override
	public Object get(Object... args) {
		
		java.lang.Long port = (java.lang.Long) args[0];
		java.lang.Integer size = (java.lang.Integer) args[1];
		
		ServerSocketClass ssocket = null;
		ClientSocketClass socket = null;
		boolean isServerSocket=false;
		java.io.InputStream is = null;
		byte[] data = new byte[size];
		if(serverSockets.containsKey(port))
		{
			ssocket = serverSockets.get(port);
			isServerSocket = true;
		}
		if(sockets.containsKey(port))
		{
			socket = sockets.get(port);
		}
		
		if(socket == null && ssocket == null)
		{
			errorMessage = ERROR_SOCKET_GET_UNKNOWN_SOCKET;
			System.out.println(ERROR_SOCKET_GET_UNKNOWN_SOCKET);
			return null;
		}
		
		if(isServerSocket){
			return ssocket.accept(true, false, false, null);
		}
		else{
			return socket.read(false);
		}
	}

	@Override
	public Object listen(Object... args) {
		
		java.lang.String portNumber = (java.lang.String) args[0];
		java.lang.String backlog = (java.lang.String) args[1];
		int port=0;
		if (portNumber.matches(".*[a-z].*")) { 
			try {
				 Library.initialize("tcnative-1");
				 long sa = Address.info(Address.APR_ANYADDR, org.apache.tomcat.jni.Socket.APR_INET, 0, 0,
		                    Library.globalPool());
		            Sockaddr addr = new Sockaddr();
		            Address.getservbyname(sa, portNumber);
		            Address.fill(addr, sa);
		            port=addr.port;
			} catch (Exception e) {
				System.out.println(ERROR_SOCKET_LISTEN_UNKNOWN_SERVICE_NAME);
				errorMessage = ERROR_SOCKET_LISTEN_UNKNOWN_SERVICE_NAME;
				e.printStackTrace();
			}
		}
		else 
			port = java.lang.Integer.valueOf(portNumber);
		ServerSocketClass serverSocket;
		
		if(backlog == null)
		{
			 serverSocket = new ServerSocketClass("localhost", port, 2);
			 serverSockets.put(java.lang.Long.valueOf(port), serverSocket);
			 return java.lang.Long.valueOf(port);
		}
		else
		{
			serverSocket = new ServerSocketClass("localhost",port, java.lang.Integer.valueOf(backlog));
			serverSockets.put(java.lang.Long.valueOf(port), serverSocket);
			return java.lang.Long.valueOf(port);
		}
	
	}

	@Override
	public Object nget(Object... args) {
		
		java.lang.Long port = (java.lang.Long) args[0];
		java.lang.Integer size = (java.lang.Integer) args[1];
		
		ServerSocketClass ssocket = null;
		ClientSocketClass socket = null;
		boolean isServerSocket=false;
		java.io.InputStream is = null;
		byte[] data = new byte[size];
		if(serverSockets.containsKey(port))
		{
			ssocket = serverSockets.get(port);
			isServerSocket = true;
		}
		if(sockets.containsKey(port))
		{
			socket = sockets.get(port);
		}
		
		if(socket == null && ssocket == null)
		{
			errorMessage = ERROR_SOCKET_GET_UNKNOWN_SOCKET;
			System.out.println(ERROR_SOCKET_GET_UNKNOWN_SOCKET);
			return null;
		}
		
		if(isServerSocket){
			return ssocket.accept(true, false, true, null);
		}
		else{
			return socket.read(true);
		}
	}

	@Override
	public Object nput(Object... args) {
		java.lang.Long port = (java.lang.Long) args[0];
		java.lang.String data = (java.lang.String) args[1];
		
		ServerSocketClass ssocket = null;
		ClientSocketClass socket = null;
		boolean isServerSocket=false;
		java.io.InputStream is = null;
		if(serverSockets.containsKey(port))
		{
			ssocket = serverSockets.get(port);
			isServerSocket = true;
		}
		if(sockets.containsKey(port))
		{
			socket = sockets.get(port);
		}
		
		if(socket == null && ssocket == null)
		{
			errorMessage = ERROR_SOCKET_GET_UNKNOWN_SOCKET;
			System.out.println(ERROR_SOCKET_GET_UNKNOWN_SOCKET);
			return null;
		}
		
		if(isServerSocket){
			return ssocket.accept(false, true, true, data);
		}
		else{
			return socket.write(data, true);
		}

	}

	@Override
	public Object put(Object... args) {
		java.lang.Long port = (java.lang.Long) args[0];
		java.lang.String data = (java.lang.String) args[1];
		
		ServerSocketClass ssocket = null;
		ClientSocketClass socket = null;
		boolean isServerSocket=false;
		java.io.InputStream is = null;
		if(serverSockets.containsKey(port))
		{
			ssocket = serverSockets.get(port);
			isServerSocket = true;
		}
		if(sockets.containsKey(port))
		{
			socket = sockets.get(port);
		}
		
		if(socket == null && ssocket == null)
		{
			errorMessage = ERROR_SOCKET_GET_UNKNOWN_SOCKET;
			System.out.println(ERROR_SOCKET_GET_UNKNOWN_SOCKET);
			return null;
		}
		
		if(isServerSocket){
			return ssocket.accept(false, true, false, data);
		}
		else{
			return socket.write(data, false);
		}
	}
	
	@Override
	public Object version(Object... args) {
		return ApplicationProperties.getSL_ARGUMENTversion();
	}
	
	private class ServerSocketClass{
		
		private Selector selector;
		private Map<SocketChannel,List> dataMapper;
		private InetSocketAddress listenAddress;
		
		public ServerSocketClass(java.lang.String hostname, int port, int backlog){
			
			listenAddress = new java.net.InetSocketAddress(hostname, port);
	        dataMapper = new HashMap<SocketChannel,List>();
	        this.startServer(backlog);
	        
		}
		
		// create server channel	
	    private void startServer(int backlog ) {
	        try {
				this.selector = Selector.open();
			
		        ServerSocketChannel serverChannel = ServerSocketChannel.open();
		        serverChannel.configureBlocking(false);
	
		        // retrieve server socket and bind to port
		        serverChannel.socket().bind(listenAddress, backlog);
		        serverChannel.register(this.selector, SelectionKey.OP_ACCEPT);
	        } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    public java.lang.String accept(boolean isRead, boolean isWrite, boolean isNonBlocking, java.lang.String data){
    	  while (true) {
              // wait for events
              try {
            	  this.selector.select();
			
	              //work on selected keys
	              Iterator keys = this.selector.selectedKeys().iterator();
	              while (keys.hasNext()) {
	                  SelectionKey key = (SelectionKey) keys.next();
	
	                  // this is necessary to prevent the same key from coming up 
	                  // again the next time around.
	                  keys.remove();
	
	                  if (!key.isValid()) {
	                      continue;
	                  }
	
	                  if (key.isAcceptable()) {
	                	   return this.accept(key, isNonBlocking);
	                  }
	                  
	                  if (key.isReadable() && isRead){
	                	  return this.read(key);
	                  }
	                  
	                  if (key.isWritable() && isWrite){
	                	  return this.write(key, data);
	                  }
	              }
	        } catch (IOException e) {
  				
  				e.printStackTrace();
  			}
    	  }
	    }
	    
	    //accept a connection made to this channel's socket
	    private java.lang.String accept(SelectionKey key, boolean isNonBlocking) throws IOException {
	        
	    	ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
	        SocketChannel channel = serverChannel.accept();
	        
	        //Set the channel non-blocking
	        if(isNonBlocking)
	        	channel.configureBlocking(false);
	    	else
	    		channel.configureBlocking(true);
	        
	        java.net.Socket socket = channel.socket();
	        SocketAddress remoteAddr = socket.getRemoteSocketAddress();
	        System.out.println("Connected to: " + remoteAddr);

	        // register channel with selector for further IO
	        dataMapper.put(channel, new ArrayList());
	        channel.register(this.selector, SelectionKey.OP_READ);
	       
	        return channel.getLocalAddress().toString();
	        
	    }
	    
	    //read from the socket channel
	    private java.lang.String read(SelectionKey key) throws IOException {
	        SocketChannel channel = (SocketChannel) key.channel();
	        ByteBuffer buffer = ByteBuffer.allocate(1024);
	        int numRead = -1;
	        numRead = channel.read(buffer);

	        if (numRead == -1) {
	            this.dataMapper.remove(channel);
	            java.net.Socket socket = channel.socket();
	            SocketAddress remoteAddr = socket.getRemoteSocketAddress();
	            System.out.println("Connection closed by client: " + remoteAddr);
	            channel.close();
	            key.cancel();
	            return "";
	        }

	        byte[] data = new byte[numRead];
	        System.arraycopy(buffer.array(), 0, data, 0, numRead);
	        return new java.lang.String(data);
	    }
	    
	    //write data to a socket
	    private java.lang.String write(SelectionKey key, java.lang.String data) throws IOException{
	    	 SocketChannel channel = (SocketChannel) key.channel();
	    	 byte [] message = data.getBytes();
	    	 int written=0;
	    	 ByteBuffer buffer = ByteBuffer.wrap(message);
	    	 written = channel.write(buffer);
	    	 buffer.clear();   
	    	 channel.close();
	    	 return java.lang.String.valueOf(written);
	    }
	}
	
	private class ClientSocketClass{
		
		private InetSocketAddress hostAddress;
		private SocketChannel client;
		
		public ClientSocketClass(java.lang.String hostname, int port){
			hostAddress = new java.net.InetSocketAddress(hostname, port);
	        try {
				client = SocketChannel.open(hostAddress);
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		
		 
		
		private java.lang.Long write(java.lang.String data, boolean isNonBlocking){
	    	byte [] message = data.getBytes();
	    	ByteBuffer buffer = ByteBuffer.wrap(message);
	    	long written = 0;
	    	
	    	try {
	    		if(isNonBlocking)
		    		client.configureBlocking(false);
		    	else
		    		client.configureBlocking(true);
	    		
	    		 written = client.write(buffer);
				 client.close();
				 return written;
			} catch (IOException e) {
				
				e.printStackTrace();
			}
	    	 buffer.clear();   
	    	
			return written;
		}
		
		public java.lang.String read(boolean isNonBlocking){
					
			ByteBuffer buffer = ByteBuffer.allocate(1024);
	        int numRead = -1;
	        try {
	        	if(isNonBlocking)
		    		client.configureBlocking(false);
		    	else
		    		client.configureBlocking(true);
				numRead = client.read(buffer);
				if (numRead == -1) {
		        
		            java.net.Socket socket = client.socket();
		            SocketAddress remoteAddr = socket.getRemoteSocketAddress();
		            System.out.println("Connection closed by client: " + remoteAddr);
		            client.close();
		            return "";
		        }
	        } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	        byte[] data = new byte[numRead];
	        System.arraycopy(buffer.array(), 0, data, 0, numRead);
	        return new java.lang.String(data);
			
		}
	}
	
}
