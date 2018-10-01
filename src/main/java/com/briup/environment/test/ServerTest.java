package com.briup.environment.test;

import java.util.Collection;

import com.briup.environment.bean.Environment;
import com.briup.environment.server.DBStore;
import com.briup.environment.server.Server;
import com.briup.environment.util.Configuration;

public class ServerTest {

	public static void main(String[] args) throws Exception {
		Configuration configuration=new Configuration();
		configuration.config();
		Server server=configuration.getServer();
		Collection<Environment>list=server.receive();
		DBStore dbStore=configuration.getDBStore();
		dbStore.saveDB(list);

	}

}