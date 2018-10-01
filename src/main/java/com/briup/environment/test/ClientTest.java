package com.briup.environment.test;

import java.util.Collection;

import com.briup.environment.bean.Environment;
import com.briup.environment.client.Client;
import com.briup.environment.client.Gather;
import com.briup.environment.util.Configuration;

public class ClientTest {

	public static void main(String[] args) throws Exception {
		Configuration configuration=new Configuration();
		configuration.config();
		Gather gather=configuration.getGather();
		//System.out.println(gather);
		Collection<Environment>list=gather.gather();
		Client client=configuration.getClient();
		client.sent(list);
		//System.out.println(list.size());
	

	}

}
