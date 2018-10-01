package com.briup.environment.backup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;

import com.briup.environment.util.Configuration;
import com.briup.environment.util.ConfigurationAware;
import com.briup.environment.util.WossModel;

public class BackUp implements ConfigurationAware,WossModel {
	private String filepath;
	@Override
	public void setConfiguration(Configuration config) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(Properties prop) throws Exception {
		filepath=prop.getProperty("filepath");
		
	}
	public void store(String fileName, Object obj) throws IOException{
		filepath="src/main/java/com/briup/environment/backup";
		File file=new File(filepath,fileName);
		if(!file.exists()){
			file.createNewFile();
		}else{
			file.delete();
			file.createNewFile();
		}
		FileOutputStream fileOutputStream=new FileOutputStream(file);
		ObjectOutputStream objectOutputStream=new ObjectOutputStream(fileOutputStream);
		objectOutputStream.writeObject(obj);
		objectOutputStream.flush();
		objectOutputStream.close();
		fileOutputStream.close();
		
	}
	public Object load(String fileName) throws IOException, ClassNotFoundException{
		File file=new File(filepath,fileName);
		Object object=null;
		if(!file.exists()){
			file.createNewFile();
		}else{
			FileInputStream fileInputStream=new FileInputStream(file);
			ObjectInputStream objectInputStream=new ObjectInputStream(fileInputStream);
			object=objectInputStream.readObject();
			fileInputStream.close();
			objectInputStream.close();
		}
		return object;
		
	}
	public void deleteBackup(String fileName){
		File  file=new File(filepath,fileName);
		if(file.exists()){
			file.delete();
		
	}
	

}

	
}
