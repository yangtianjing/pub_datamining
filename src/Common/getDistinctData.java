package Common;

import CrossData.fileAttribute;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class getDistinctData {
	public static void getDistinctAttr(fileAttribute f,int attr) {
		try {
			//read file
			System.out.println("distinct file:"+f.getFilePath());
			Map<String,String> dlist = new HashMap<String,String>();
			File infile = new File(f.getFilePath());
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(infile),"UTF-8"));
			String str = "";
			File outfile = new File(f.getFolder()+"\\distinctData\\attrDistinct_"+f.getFileName());
			//System.out.println(outfile.getName());
			if(!outfile.exists()) {
				outfile.getParentFile().mkdir();
			}
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile),"UTF-8"));
			while((str = reader.readLine())!=null) {
				String[] temp = str.split(f.getFileSeparator());
				if(dlist.containsKey(temp[attr])){
					continue;
				}else {
					dlist.put(temp[attr],"");
				}
			}
			for(String key : dlist.keySet()) {
				writer.write(key+"\r\n");
			}
			reader.close();
			writer.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void getDistinctAttr(fileAttribute f,int[] attr) {
		try {
			//read file
			System.out.println("distinct file:"+f.getFilePath());
			Map<String,String> dlist = new HashMap<String,String>();
			File infile = new File(f.getFilePath());
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(infile),"UTF-8"));
			String str = "";
			File outfile = new File(f.getFolder()+"\\distinctData\\attrDistinct_"+f.getFileName());
			//System.out.println(outfile.getName());
			if(!outfile.exists()) {
				outfile.getParentFile().mkdir();
			}
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile),"UTF-8"));
			while((str = reader.readLine())!=null) {
				String[] temp = str.split(f.getFileSeparator());
				String tag = "";
				tag+=temp[attr[0]].substring(0,4)+",";
				for(int i=1;i<attr.length;i++) {
					tag+=temp[attr[i]]+",";
				}
				if(dlist.containsKey(tag)){
					continue;
				}else {
					dlist.put(tag,str);
				}
			}
			for(String key : dlist.keySet()) {
				writer.write(dlist.get(key)+"\r\n");
			}
			reader.close();
			writer.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		File rootpath = new File("E:\\dataManageWorkplace\\20191228\\HK\\total\\cutFile\\filterByFileAttr\\fixed");//tl
		String[] rootlist = rootpath.list();
		int[] attr = {0,1};
		for(int i=0;i<rootlist.length;i++) {
			File temp = new File(rootpath+"\\"+rootlist[i]);
			if(temp.isFile()&&(!rootlist[i].contains("nameList"))) {
				fileAttribute f = new fileAttribute(temp.getAbsolutePath(),",");
				//System.out.println("distinct file:"+f.getFilePath());
				getDistinctAttr(f,attr);
			}
		}
		
		/*
		String loc = "dy";
		for(int i=0;i<3;i++) {
			if(i==1) {
				loc = "mig";
			}
			if(i==2) {
				loc="ctu";
			}
			File rootpath = new File("G:\\CombHkTl\\cutFile");//tl
			String[] rootlist = rootpath.list();
			for(int j=0;j<rootlist.length;j++) {
				if(!rootlist[j].contains(loc)||(!rootlist[j].contains("2015"))||(!rootlist[j].contains("tl"))) {
					continue;
				}
				fileAttribute f = new fileAttribute(rootpath.getAbsolutePath()+"\\"+rootlist[j],",");
				//System.out.println("distinct file:"+f.getFilePath());
				getDistinctAttr(f,5);
			}
			
			rootpath = new File("G:\\CombHkTl\\cutFile" );//tl
			rootlist = rootpath.list();
			for(int j=0;j<rootlist.length;j++) {
				if(!rootlist[j].contains(loc)||(!rootlist[j].contains("2015"))||(!rootlist[j].contains("hk"))) {
					continue;
				}
				fileAttribute f = new fileAttribute(rootpath.getAbsolutePath()+"\\"+rootlist[j],",");
				//System.out.println("distinct file:"+f.getFilePath());
				getDistinctAttr(f,1);
			}
		}
		*/
	}

}
