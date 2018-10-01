package com.briup.environment.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Properties;

import com.briup.environment.backup.BackUp;
import com.briup.environment.bean.Environment;
import com.briup.environment.util.Configuration;
import com.briup.environment.util.ConfigurationAware;
import com.briup.environment.util.WossModel;

public class Client implements ConfigurationAware,WossModel {
	private String host;
	private int port;
	private String backfilename;
	private BackUp backup;
	private Gather gather;
	@Override
	public void init(Properties prop) throws Exception {
	    host=prop.getProperty("host");
	    port=Integer.parseInt(prop.getProperty("port"));
	    backfilename=prop.getProperty("backfilename");
		
	}

	@Override
	public void setConfiguration(Configuration config) {
		backup=config.getBackUp();
		gather=config.getGather();
		
	}
	public void sent(Collection<Environment>  list) throws Exception{
		Socket socket=new Socket(host,port);
		list=gather.gather();
		if(backup.load(backfilename)!=null){
			Collection<Environment>ocollection=(Collection<Environment>) backup.load(backfilename);
			backup.deleteBackup(backfilename);
			list.addAll(ocollection);
		}
		OutputStream outputStream=null;
		ObjectOutputStream objectOutputStream=null;
		try {
			outputStream=socket.getOutputStream();	
			objectOutputStream=new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject(list);
			objectOutputStream.flush();
		} catch (IOException e) {
			backup.store(backfilename, list);
			e.printStackTrace();
			
		}
		
		outputStream.close();
		objectOutputStream.close();
		socket.close();
	}


}
