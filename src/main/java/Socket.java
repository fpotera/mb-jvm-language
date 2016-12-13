
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.jni.Address;
import org.apache.tomcat.jni.Library;
import org.apache.tomcat.jni.Sockaddr;

import com.axway.jmb.ProcedureParameterIOType;
import com.axway.jmb.annotations.FuncOrProcParameterDefaultValue;
import com.axway.jmb.annotations.FuncOrProcParameterType;
import com.axway.jmb.annotations.ProcParameterNoiseWord;
import com.axway.jmb.annotations.ProcedureParameter;
import com.axway.jmb.annotations.ProcedureParameters;

import resources.ApplicationProperties;

/**
 * The SOCKET extension module contains functions and statements for handling sockets.
 * 
 * @author Codrin Cojocaru
 */

public class Socket implements ISocket {
	private static final int SERVER_SOCKET_START_ID = 0;
	private static final int SERVER_SOCKET_CONNECTED_START_ID = 10000;
	private static final int CLIENT_SOCKET_START_ID = 20000;
	
	private int serverSocketsIndex = SERVER_SOCKET_START_ID;
	private int serverSocketsConnectedIndex = SERVER_SOCKET_CONNECTED_START_ID;
	private int clientSocketsIndex = CLIENT_SOCKET_START_ID;

	private HashMap<Long, ServerSocketClass> serverSockets = new  HashMap<Long, ServerSocketClass>();
	private HashMap<Long, SocketChannel> serverSocketsConnected = new  HashMap<Long, SocketChannel>();
	
	private HashMap<Long, ClientSocketClass> clientSockets = new  HashMap<Long, ClientSocketClass>();
	
	private static String errorMessage;
	
	
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
	public Object accept(Object... args) {
		Long ssocket = (Long) args[0];

		if(ssocket == null)
		{
			System.out.println(ERROR_SOCKET_ACCEPT_UNKNOWN_SOCKET);
			errorMessage=ERROR_SOCKET_ACCEPT_UNKNOWN_SOCKET;
			System.exit(1);
		}
		
		ServerSocketClass serverSocket = serverSockets.get(ssocket);
		
		//String port=serverSocket.accept(false, false, false, null);
		//return port;
		
		Long socketId = null;
		try {
			socketId = (long) serverSocketsConnectedIndex;
			serverSocketsConnected.put((long) socketId, serverSocket.accept());
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return socketId;
	}

	@Override
	public Object connect(Object... args) {
		String host = (String) args[0];
		String service = (String) args[1];
		
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
			port = Integer.valueOf(service);
		
		ClientSocketClass cliSocket = null;
		try {
			cliSocket = new ClientSocketClass(host, port);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}		
		
		int socketId = clientSocketsIndex++;
		
		clientSockets.put((long) socketId, cliSocket);
		
		return Long.valueOf(socketId);
	}

	@Override
	@ProcedureParameters({
		@ProcedureParameter(paramIOType=ProcedureParameterIOType.IN,
				paramType=@FuncOrProcParameterType(type = FuncOrProcParameterType.Type.PRIMITIVE),
				defaulValue=@FuncOrProcParameterDefaultValue(FuncOrProcParameterDefaultValue.Value.NONE),
				noiseWord=@ProcParameterNoiseWord(ProcParameterNoiseWord.Word.DEFAULT))
	})
	public void disconnect(Object... args) {
		Long toBeClosed = (Long) args[0];
		
		if(serverSockets.containsKey(toBeClosed)) {
			ServerSocketClass ss = serverSockets.remove(toBeClosed);
			try {
				ss.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		if(serverSocketsConnected.containsKey(toBeClosed)) {
			SocketChannel ss = serverSocketsConnected.remove(toBeClosed);
			try {
				ss.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		if(clientSockets.containsKey(toBeClosed)) {
			ClientSocketClass ss = clientSockets.remove(toBeClosed);
			try {
				ss.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	@Override
	public Object error(Object... args) {
		return errorMessage;
	}


	@Override
	public Object listen(Object... args) {
		
		String portNumber = (String) args[0];
		Long backlog = (long) 2;
		if (args.length > 1) {
			backlog = (Long) args[1];
		}

		int port = 0;
		if (portNumber.matches(".*[a-z].*")) {
			try {
				Library.initialize("tcnative-1");
				long sa = Address.info(Address.APR_ANYADDR, org.apache.tomcat.jni.Socket.APR_INET, 0, 0,
						Library.globalPool());
				Sockaddr addr = new Sockaddr();
				Address.getservbyname(sa, portNumber);
				Address.fill(addr, sa);
				port = addr.port;
			} catch (Exception e) {
				System.out.println(ERROR_SOCKET_LISTEN_UNKNOWN_SERVICE_NAME);
				errorMessage = ERROR_SOCKET_LISTEN_UNKNOWN_SERVICE_NAME;
				e.printStackTrace();
			}
		} else
			port = Integer.valueOf(portNumber);

		ServerSocketClass serverSocket;

		serverSocket = new ServerSocketClass("localhost", port, backlog.intValue());
		int socketId = serverSocketsIndex++;
		
		serverSockets.put((long) socketId, serverSocket);
		
		return Long.valueOf(socketId);
	}

	@Override
	public Object nget(Object... args) {
		List<Object> lst =  Arrays.asList(args);
		lst.add(new Boolean(true));
		
		return get (lst.toArray());
	}

	@Override
	public Object get(Object... args) {		
		Long socketId = (Long) args[0];
		Long size = (Long) args[1];
		
		boolean isNonBlocking = false;
		if ( args.length == 3 ) {
			isNonBlocking = (boolean) args[2];
		}
		
		SocketChannel ssocket = null;
		ClientSocketClass socket = null;
		boolean isServerSocket=false;

		ByteBuffer buffer = ByteBuffer.allocate(size.intValue());
		buffer.clear();
		
		if(serverSocketsConnected.containsKey(socketId))
		{
			ssocket = serverSocketsConnected.get(socketId);
			isServerSocket = true;
		}
		if(clientSockets.containsKey(socketId))
		{
			socket = clientSockets.get(socketId);
		}
		
		if(socket == null && ssocket == null)
		{
			errorMessage = ERROR_SOCKET_GET_UNKNOWN_SOCKET;
			System.out.println(ERROR_SOCKET_GET_UNKNOWN_SOCKET);
			return "";
		}
		
		if(isServerSocket){
			try {
				ssocket.configureBlocking(true);
				int cnt = ssocket.read(buffer);
				if ( cnt != -1 ) {
					byte[] buff = new byte[cnt];
					buffer.rewind();
					buffer.get(buff, 0, cnt);
					return new String( buff );
				} 
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			try {
				byte[] buff = new byte[1000];
				int cnt =  socket.read(buff, false).intValue();
				if ( cnt != -1 ) {
					return new String( buff, 0, cnt );					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}	
	
	@Override
	public Object nput(Object... args) {
		List<Object> lst =  Arrays.asList(args);
		lst.add(new Boolean(true));
		
		return put (lst.toArray());
	}

	@Override
	public Object put(Object... args) {
		Long socketId = (Long) args[0];
		String data = (String) args[1];
		
		boolean isNonBlocking = false;
		if ( args.length == 3 ) {
			isNonBlocking = (boolean) args[2];
		}
		
		SocketChannel ssocket = null;
		ClientSocketClass socket = null;
		
		boolean isServerSocket=false;

		if(serverSocketsConnected.containsKey(socketId))
		{
			ssocket = serverSocketsConnected.get(socketId);
			isServerSocket = true;
		}
		if(clientSockets.containsKey(socketId))
		{
			socket = clientSockets.get(socketId);
		}
		
		if(socket == null && ssocket == null)
		{
			errorMessage = ERROR_SOCKET_GET_UNKNOWN_SOCKET;
			System.out.println(ERROR_SOCKET_GET_UNKNOWN_SOCKET);
			return 0L;
		}
		
		if(isServerSocket){
			try {
				ssocket.configureBlocking(true);
				return new Long ( ssocket.write( ByteBuffer.wrap( data.getBytes() ) ) );
			} catch (IOException e) {
				e.printStackTrace();
				return 0L;
			}
		}
		else{
			try {
				return new Long ( socket.write(data, false) );
			} catch (IOException e) {
				e.printStackTrace();
				return 0L;
			}
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
		
		private ServerSocketChannel serverChannel;
		
		public ServerSocketClass(String hostname, int port, int backlog){
			
			listenAddress = new InetSocketAddress(hostname, port);
	        dataMapper = new HashMap<SocketChannel,List>();
	        this.startServer(backlog);
		}
		
		public SocketChannel accept () throws IOException {
			return serverChannel.accept();
		}
		
		// create server channel	
	    private void startServer(int backlog ) {
	        try {
				selector = Selector.open();
			
		        serverChannel = ServerSocketChannel.open();
		        serverChannel.configureBlocking(true);
	
		        // retrieve server socket and bind to port
		        serverChannel.bind(listenAddress, backlog);
		        
//		        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
	        } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	    
	    public String accept(boolean isRead, boolean isWrite, boolean isNonBlocking, String data){
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
	    
	    public void close() throws IOException {
	    	serverChannel.close();
	    }
	    
	    //accept a connection made to this channel's socket
	    private String accept(SelectionKey key, boolean isNonBlocking) throws IOException {
	        
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
	    private String read(SelectionKey key) throws IOException {
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
	        return new String(data);
	    }
	    
	    //write data to a socket
	    private String write(SelectionKey key, String data) throws IOException{
	    	 SocketChannel channel = (SocketChannel) key.channel();
	    	 byte [] message = data.getBytes();
	    	 int written=0;
	    	 ByteBuffer buffer = ByteBuffer.wrap(message);
	    	 written = channel.write(buffer);
	    	 buffer.clear();   
	    	 channel.close();
	    	 return String.valueOf(written);
	    }
	}
	
	private class ClientSocketClass{
		
		private InetAddress hostAddress;
		private java.net.Socket client;
		
		public ClientSocketClass(String hostname, int port) throws IOException{
			hostAddress = InetAddress.getByName(hostname);

			client = new java.net.Socket(hostAddress, port);

		}
		
		public Long write(String data, boolean isNonBlocking) throws IOException {


			client.getOutputStream().write(data.getBytes());

			return (long) data.length();
		}
		
		public Long read(byte[] buffer, boolean isNonBlocking) throws IOException {

			int numRead = -1;

			numRead = client.getInputStream().read(buffer);

			return (long) numRead;
		}
		
		public void close() throws IOException {
			client.close();
		}
	}
	
	
/*	
	private class ClientSocketClass{
		
		private InetSocketAddress hostAddress;
		private SocketChannel client;
		
		public ClientSocketClass(String hostname, int port){
			hostAddress = new InetSocketAddress(hostname, port);
	        try {
				client = SocketChannel.open(hostAddress);
				
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}
		
		public Long write(String data, boolean isNonBlocking) throws IOException {
			byte[] message = data.getBytes();
			ByteBuffer buffer = ByteBuffer.wrap(message);

			if (isNonBlocking)
				client.configureBlocking(false);
			else
				client.configureBlocking(true);

			return (long) client.write(buffer);
		}
		
		public Long read(ByteBuffer buffer, boolean isNonBlocking) throws IOException {

			int numRead = -1;

			if (isNonBlocking) {
				client.configureBlocking(false);
			}
			else {
				client.configureBlocking(true);			
			}
			numRead = client.read(buffer);

			return (long) numRead;
		}
		
		public void close() throws IOException {
			client.close();
		}
	}
*/	
}
