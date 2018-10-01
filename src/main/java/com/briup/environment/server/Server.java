package com.briup.environment.server;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import com.briup.environment.bean.Environment;
import com.briup.environment.util.Configuration;
import com.briup.environment.util.ConfigurationAware;
import com.briup.environment.util.WossModel;

public class Server implements ConfigurationAware,WossModel {
	private int port;

	@Override
	public void init(Properties prop) throws Exception {
		port=Integer.parseInt(prop.getProperty("port"));
		
	}

	@Override
	public void setConfiguration(Configuration config) {
		
		
	}
	public Collection<Environment> receive() throws Exception{
		ServerSocket serverSocket=new ServerSocket(port);
		Socket socket=serverSocket.accept();
		InputStream inputStream=socket.getInputStream();
		ObjectInputStream objectInputStream=new ObjectInputStream(inputStream);
		ArrayList<Environment>list =new ArrayList<>();
		list=(ArrayList<Environment>) objectInputStream.readObject();
		inputStream.close();
		objectInputStream.close();
		socket.close();
		serverSocket.close();
		return list;
	}

}
