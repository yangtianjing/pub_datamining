package CrossData;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import CrossData.fileAttribute;
public class fileCut {
	public static void cutFileByAttr(fileAttribute f, int attr, int start, int end) {
		Map<String,BufferedWriter> sliceWriter = new HashMap<String,BufferedWriter>();
		
		try {
			File infile = new File(f.getFilePath());
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(infile),"UTF-8"));
			File rsFolder = new File(f.getFolder()+"\\cutFile");
			if(!rsFolder.exists()) {
				rsFolder.mkdir();
			}
			String str="";
			while((str = reader.readLine())!=null) {
				String key = "";
				boolean errornot = false;
				try{
					key = str.split(f.getFileSeparator())[attr].substring(start,end);
				}catch(Exception e) {
					errornot = true;
					//System.out.println(str);
					//e.printStackTrace();
				}
				if(errornot) {
					continue;
				}
				//System.out.println(str.split(f.getFileSeparator())[attr]);
				if(!sliceWriter.containsKey(key)) {
					File tempf = new File(f.getFolder()+"\\cutFile\\"+key+"_"+f.getFileName());
					BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempf),"UTF-8"));
					//writer.write("Label,Source,Target,Weight\r\n");
					sliceWriter.put(key,writer);
					sliceWriter.get(key).write(str+"\r\n");
				}else {
					sliceWriter.get(key).write(str+"\r\n");
				}
			}
			for(String key : sliceWriter.keySet()) {
				sliceWriter.get(key).close();
			}
			System.out.println("cut file finished!!!");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		File rootpath = new File("G:\\CombHkTl\\comb");
		String[] rootlist = rootpath.list();
		for(int i=0;i<rootlist.length;i++) {
			File temp = new File(rootpath+"\\"+rootlist[i]);
			if(!temp.isFile()||(!rootlist[i].contains(".csv"))||rootlist[i].contains("_id")) {
				continue;
			}
			fileAttribute f = new fileAttribute(temp.getAbsolutePath(),",");
			System.out.println("cut file:"+temp.getAbsolutePath());
			if(rootlist[i].contains("tl")) {
				cutFileByAttr(f,16,0,4);	
			}else {
				cutFileByAttr(f,0,0,4);	
			}
		}
		*/
		/*
		fileAttribute f = new fileAttribute("G:\\Train_Data_highway\\refined\\dy_mig_raildata_highway.csv",",");
		cutFileByAttr(f,5,0,4);	
		*/
		
		File rootpath = new File("E:\\dataManageWorkplace\\20191228\\TL\\total");
		String[] rootlist = rootpath.list();
		for(int i=0;i<rootlist.length;i++) {
			File temp = new File(rootpath+"\\"+rootlist[i]);
			if(!temp.isFile()||(!rootlist[i].contains("csv"))) {//(!rootlist[i].contains("mig"))||||rootlist[i].contains("node")
				continue;
			}
			fileAttribute f = new fileAttribute(temp.getAbsolutePath(),",");
			System.out.println("cut file:"+temp.getAbsolutePath());
			cutFileByAttr(f,16,0,4);	
		}
	}

}
