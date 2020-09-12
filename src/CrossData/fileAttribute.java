package CrossData;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;


public class fileAttribute {
	private String filepath;
	private String filename;
	private String separator;
	private int length;
	private String[] fields;
	private String fileFolder;
	private String fileEncode;
	public fileAttribute(String fpath,String encode,String separators,String fieldList){
		File f = new File(fpath);
		if(encode.contentEquals("")) {
			encode="UTF-8";//default set UTF-8
		}
		try {
			BufferedReader fr = new BufferedReader(new InputStreamReader(new FileInputStream(f),encode));
			filepath = f.getAbsolutePath();
			filename = f.getName();
			separator = separators;
			fileFolder = f.getParent();
			fileEncode = encode;
			if(f.length()<1) {
				System.err.println("File needs to have multiple rows of data"+"\r\n"+"error file:"+
			filepath);
			}
			String temp = "";
			temp=fr.readLine();
			temp=fr.readLine();
			length=0;
			for(int i=0;i<temp.length();i++) {
				if(String.valueOf(temp.charAt(i)).equals(separators)){
					length++;
				}
			}
			length++;
			if(fieldList.equals("")) {
				fields = new String[length];
				for(int i=0;i<length;i++) {
					fields[i]="unknown";
				}
			}else {
				fields=fieldList.split(separators);
			}
			fr.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public fileAttribute(String fpath,String separators,String fieldList){
		File f = new File(fpath);
		try {
			BufferedReader fr = new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF-8"));
			filepath = f.getAbsolutePath();
			filename = f.getName();
			separator = separators;
			fileFolder = f.getParent();
			fileEncode ="UTF-8";
			if(f.length()<1) {
				System.err.println("File needs to have multiple rows of data"+"\r\n"+"error file:"+
			filepath);
			}
			String temp = "";
			temp=fr.readLine();
			temp=fr.readLine();
			length=0;
			for(int i=0;i<temp.length();i++) {
				if(String.valueOf(temp.charAt(i)).equals(separators)){
					length++;
				}
			}
			length++;
			if(fieldList.equals("")) {
				fields = new String[length];
				for(int i=0;i<length;i++) {
					fields[i]="unknown";
				}
			}else {
				fields=fieldList.split(separators);
			}
			fr.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public fileAttribute() {
		
	}
	public fileAttribute(String fpath,String separators){
		File f = new File(fpath);
		try {
			BufferedReader fr = new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF-8"));
			filepath = f.getAbsolutePath();
			filename = f.getName();
			separator = separators;
			fileFolder = f.getParent();
			fileEncode ="UTF-8";
			if(f.length()<1) {
				System.err.println("File needs to have multiple rows of data"+"\r\n"+"error file:"+
			filepath);
			}
			String temp = "";
			temp=fr.readLine();
			temp=fr.readLine();
			length=0;
			for(int i=0;i<temp.length();i++) {
				if(String.valueOf(temp.charAt(i)).equals(separators)){
					length++;
				}
			}
			length++;
			fields = new String[length];
			for(int i=0;i<length;i++) {
				fields[i]="unknown";
			}
			fr.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public fileAttribute(String fpath){
		File f = new File(fpath);
		if(!f.exists()) {
			System.err.println("Not Json For this File!!!");
		}
		//System.err.println("File!!!");
		try {
			BufferedReader fr = new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF-8"));
			String jsonstr = "";
			String str ="";
			while((str = fr.readLine())!=null) {
				jsonstr+=str;
				//System.out.println(str);
				continue;
			}
			Map<String,String> attrs = new HashMap<String ,String>();
			//jsonstr= jsonstr.replaceAll("\\\\", "\\");
			jsonstr= jsonstr.replaceAll("\\{", "");
			jsonstr= jsonstr.replaceAll("\\}", "");
			jsonstr= jsonstr.replaceAll("\\[", "");
			jsonstr= jsonstr.replaceAll("\\]", "");
			jsonstr= jsonstr.replaceAll("\":\"", "::");
			String[] tstr = jsonstr.split("\",");
			for(int i=0;i<tstr.length;i++) {
				//System.out.println(tstr[i]);
				attrs.put(tstr[i].split("::")[0].replaceAll("\"",""),tstr[i].split("::")[1].replaceAll("\"",""));
			}
			//System.err.println("File!!!");
			filepath = attrs.get("filepath");
			filename = attrs.get("filename");
			separator = attrs.get("separator");
			fileFolder = attrs.get("fileFolder");
			fileEncode = attrs.get("fileEncode");
			length=Integer.parseInt(attrs.get("length"));
			fields = attrs.get("fields").split(separator);
			fr.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		//System.err.println("File!!!");
	}
	
	public String getFilePath() {
		return filepath;
	}
	
	public String getFileName() {
		return filename;
	}
	
	public String getFileSeparator() {
		return separator;
	}
	
	public int getRowLength() {
		return length;
	}
	
	public String[] getFieldList() {
		return  fields;
	}
	
	public String getFolder() {
		return fileFolder;
	}
	
	public String getEncode() {
		return fileEncode;
	}
	public void createJson() {
		String feature = "[{";
		feature+="\"filepath\":\""+filepath+"\",";//.replaceAll("\\","\\\\")
		feature+="\"filename\":\""+filename+"\",";
		feature+="\"separator\":\""+separator+"\",";
		feature+="\"length\":\""+length+"\",";
		feature+="\"fileEncode\":\""+fileEncode+"\",";
		feature+="\"fileFolder\":\""+fileFolder+"\",";//.replaceAll("\\","\\\\")
		String fieldstr = "";
		for(int i=0;i<fields.length;i++) {
			fieldstr+=fields[i]+separator;
		}
		feature+="\"fields\":\""+fieldstr+"\"";
		feature+="}]";

		String jstr = feature;
		File jfile = new File(fileFolder+"\\"+filename.replaceAll(".csv", ".json"));
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(jfile),"UTF-8"));
			bw.write(jstr);
			bw.flush();
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
