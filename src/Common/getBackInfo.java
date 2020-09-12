package Common;
import java.io.*;
import java.util.Map;
import java.util.HashMap;
import CrossData.fileAttribute;
import Common.getDistinctData;

public class getBackInfo {
	public static Map<String,String> getBackInFile(fileAttribute ls, fileAttribute backf, int lscnt, int checkcnt, int backcnt){
		Map<String,String> rs = new HashMap<String,String>();
		Map<String,String> lsmap = new HashMap<String,String>();
		Map<String,String> backmap = new HashMap<String,String>();
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(ls.getFilePath()),"UTF-8"));
			String str = "";
			while((str = reader.readLine())!=null) {
				String[] temp = str.split(",");
				lsmap.put(temp[lscnt],"");
			}
			reader.close();
			
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(backf.getFilePath()),"UTF-8"));
			while((str = reader.readLine())!=null) {
				String[] temp = str.split(",");
				backmap.put(temp[checkcnt],temp[backcnt]);
			}
			reader.close();
			
			
			for(String keys : lsmap.keySet()) {
				if(backmap.containsKey(keys)) {
					rs.put(backmap.get(keys), "");
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public static Map<String,String> getBackInFile(fileAttribute ls, fileAttribute backf, int lscnt, int checkcnt, int backcnt,boolean tl){
		
		Map<String,String> rs = new HashMap<String,String>();
		Map<String,String> lsmap = new HashMap<String,String>();
		Map<String,String> backmap = new HashMap<String,String>();
		
		//System.out.println("1");
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(ls.getFilePath()),"UTF-8"));
			String str = "";
			while((str = reader.readLine())!=null) {
				String[] temp = str.split(",");
				lsmap.put(temp[lscnt],"");
			}
			reader.close();
			//System.out.println("2");
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(backf.getFilePath()),"UTF-8"));
			while((str = reader.readLine())!=null) {
				String[] temp = str.split(",");
				backmap.put(temp[checkcnt],temp[backcnt]);
			}
			reader.close();
			
			//System.out.println("3");
			for(String keys : lsmap.keySet()) {
				if(backmap.containsKey(keys)) {
					if(tl) {
						rs.put(keys,backmap.get(keys));
					}else {
						rs.put(backmap.get(keys),keys);
					}
					
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public static Map<String,String> cutByYear(Map<String,String> source,int y, int fromage, int toage){
		Map<String,String> rs = new HashMap<String,String>();
		String ageSex = "";
		for(String keys:source.keySet()) {
			String ystr = source.get(keys).substring(6,10);
			ageSex = String.valueOf(y+1-Integer.parseInt(ystr))+","+String.valueOf(Integer.parseInt(source.get(keys).substring(16,17))%2);
			if(y+1-Integer.parseInt(ystr)>=fromage&&y+1-Integer.parseInt(ystr)<=toage) {
				rs.put(keys,ageSex);
			}
		}
		return rs;
	}
	
	public static Map<String,String> cutByAge(Map<String,String> source, int fromage, int toage){
		Map<String,String> rs = new HashMap<String,String>();
		for(String keys:source.keySet()) {
			//System.out.println(source.get(keys));
			String ystr = source.get(keys).replaceAll("\"","").split(";")[0];
			//System.out.println(ystr);
			if(ystr.contains("null")) {
				continue;
			}
			if(Integer.parseInt(ystr)>=fromage&&Integer.parseInt(ystr)<=toage) {
				//System.out.println("ok");
				rs.put(keys,source.get(keys).replaceAll("\"","").replaceAll(";",","));
			}
		}
		return rs;
	}
	
	public static Map<String,String> getBackInFile(Map<String,String> lsmap, fileAttribute backf, int checkcnt){
		Map<String,String> rs = new HashMap<String,String>();
		Map<String,String> backmap = new HashMap<String,String>();
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(backf.getFilePath()),"UTF-8"));
			String str = "";
			while((str = reader.readLine())!=null) {
				String[] temp = str.split(",");
				backmap.put(temp[checkcnt],str);
			}
			reader.close();
			
			
			for(String keys : lsmap.keySet()) {
				if(backmap.containsKey(keys)) {
					rs.put(backmap.get(keys), "");
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public static Map<String,String> getBackInFile(Map<String,String> lsmap, fileAttribute backf, int checkcnt, int backcnt){
		Map<String,String> rs = new HashMap<String,String>();
		Map<String,String> backmap = new HashMap<String,String>();
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(backf.getFilePath()),"UTF-8"));
			String str = "";
			while((str = reader.readLine())!=null) {
				String[] temp = str.split(",");
				backmap.put(temp[checkcnt],temp[backcnt]);
				//System.out.println(temp[checkcnt]);
			}
			reader.close();
			
			
			for(String keys : lsmap.keySet()) {
				//System.out.println(lsmap.get(keys));
				if(backmap.containsKey(keys)) {
					//System.out.println(lsmap.get(keys));
					rs.put(lsmap.get(keys),backmap.get(keys));
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public static Map<String,String> getDistinctStrAttr(fileAttribute f,int attr) {//filter distinct string
		Map<String,String> dlist = new HashMap<String,String>();
		try {
			//read file
			System.out.println("distinct file:"+f.getFilePath());
			
			File infile = new File(f.getFilePath());
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(infile),"UTF-8"));
			String str = "";
			while((str = reader.readLine())!=null) {
				String[] temp = str.split(f.getFileSeparator());
				if(dlist.containsKey(temp[attr])){
					continue;
				}else {
					//System.out.println(temp[attr]);
					dlist.put(temp[attr],str);
				}
			}
			reader.close();
			return dlist;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return dlist;
	}
	
	public static void fixPeople(fileAttribute f, fileAttribute[] fs, int[] attr) {
		Map<String,String> dlist = getDistinctStrAttr(f, attr[0]);
		Map<String,String> namels = new HashMap<String,String>();
		File outfile = new File(f.getFolder()+"\\fixed\\nameList_"+f.getFileName());
		File folder = new File(f.getFolder()+"\\fixed");
		if(!folder.exists()) {
			folder.mkdir();
		}
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile),"UTF-8"));
			for(String key:dlist.keySet()) {
				String[] temp = dlist.get(key).split(f.getFileSeparator());
				String target = "";
				for(int i=0;i<attr.length-1;i++) {
					target+=temp[attr[i]]+",";
				}
				target+=temp[attr[attr.length-1]];
				bw.write(target+"\r\n");
				namels.put(target,"");
			}
			bw.close();
			
			//step find
			for(int i=0;i<fs.length;i++) {
				String str = "";
				File newf = new File(fs[i].getFilePath());
				File outf = new File(fs[i].getFolder()+"\\fixed\\fixed_"+fs[i].getFileName());
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(newf),"UTF-8"));
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outf),"UTF-8"));
				while((str = br.readLine())!=null) {
					String[] temp = str.split(fs[i].getFileSeparator());
					boolean p = false;
					String tag = "";
					for(int k=0;k<attr.length-1;k++) {
						tag+=temp[attr[k]]+",";
					}
					tag+=temp[attr[attr.length-1]];
					if(namels.containsKey(tag)) {
						writer.write(str+"\r\n");
					}
				}
				br.close();
				writer.close();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		File rootpath = new File("E:\\dataManageWorkplace\\20191228\\HK\\total\\cutFile\\filterByFileAttr");
		String[] rootlist = rootpath.list();
		String[] loc = {"dy","mig","ctu"};
		int[] attr = {1};
		for(int i=0;i<loc.length;i++) {
			fileAttribute f = new fileAttribute();
			for(int j=0;j<rootlist.length;j++) {
				if(!rootlist[j].contains(loc[i])||!rootlist[j].contains("2015")) {
					continue;
				}
				f = new fileAttribute(rootpath+"\\"+rootlist[j],",");
			}
			fileAttribute[] fs  = new fileAttribute[4];
			int cnt=0;
			for(int j=0;j<rootlist.length;j++) {
				if(!rootlist[j].contains(loc[i])) {
					continue;
				}
				fs[cnt] = new fileAttribute(rootpath+"\\"+rootlist[j],",");
				cnt++;
			}
			System.out.println("fix file:"+f.getFilePath());
			fixPeople(f, fs, attr);
		}
		
		
		/*
		fileAttribute prlist = new fileAttribute("E:\\dataManageWorkplace\\dy_tl\\prlist.txt",",");
		fileAttribute namelist = new fileAttribute("E:\\dataManageWorkplace\\dy_tl\\nameList.csv",",");
		fileAttribute recordlist = new fileAttribute("E:\\dataManageWorkplace\\2018_dy_raildata_md5.csv",",");
		Map<String,String> rs = new HashMap<String,String>();
		Map<String,String> info = new HashMap<String,String>();
		rs = getBackInFile(prlist, namelist, 0, 1, 0);//prlist id,namelist id,namelist need id
		info = getBackInFile(rs, recordlist, 6);//file id
		File outfile = new File("E:\\dataManageWorkplace\\dy_tl_pagerank.csv");
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile),"UTF-8"));
			for(String keys:info.keySet()) {
				writer.write(keys.replaceAll("\"", "").replaceAll(";", ",")+"\r\n");
			}
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		*/
//		File folderls = new File("E:\\dataManageWorkplace");
//		String[] folderlist = folderls.list();
//		
//		for(int i=0;i<folderlist.length;i++) {
//			File secFolder = new File(folderls.getAbsolutePath()+"\\"+folderlist[i]);
//			if(!(folderlist[i].contains("_")&&secFolder.isDirectory())) {
//				continue;
//			}
//			System.out.println("read folder:"+secFolder.getAbsolutePath());
//			if(folderlist[i].contains("tl")) {
//				File[] thirdFolder = new File[2];
//				String[] thirdlist = secFolder.list();
//				for(int j=0;j<thirdlist.length;j++) {
//					if(thirdlist[j].contains("networkFilter")) {
//						thirdFolder[Integer.parseInt(thirdlist[j].replace("networkFilter2", "1").replace("networkFilter", "0"))]
//								= new File(secFolder.getAbsolutePath()+"\\"+thirdlist[j]);
//					}
//				}
//				for(int j=0;j<2;j++) {
//					System.out.println("read folder:"+thirdFolder[j].getAbsolutePath());
//					fileAttribute varOne = new fileAttribute(thirdFolder[j].getAbsolutePath()+"\\cmpls.csv",",");
//					fileAttribute varTwo = new fileAttribute(secFolder.getAbsolutePath()+"\\nameList.csv",",");
//					Map<String,String> rs = new HashMap<String,String>();
//					//System.out.println("11111");
//					rs = getBackInFile(varOne, varTwo, 0, 1, 0,true);
//					//System.out.println("Almost finished");
//					rs = cutByYear(rs,2018,18,35);//age range 18 - 35
//					File outfile = new File(thirdFolder[j].getAbsolutePath()+"\\cutlist.csv");
//					try {
//						BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile),"UTF-8"));
//						for(String keys:rs.keySet()) {
//							writer.write(keys+","+rs.get(keys)+"\r\n");
//						}
//						writer.close();
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} 
//				}
//			}else if(folderlist[i].contains("hk")) {
//				File[] thirdFolder = new File[2];
//				String[] thirdlist = secFolder.list();
//				String thirdvar = "";
//				for(int j=0;j<thirdlist.length;j++) {
//					if(thirdlist[j].contains("networkFilter")) {
//						thirdFolder[Integer.parseInt(thirdlist[j].replace("networkFilter2", "1").replace("networkFilter", "0"))]
//								= new File(secFolder.getAbsolutePath()+"\\"+thirdlist[j]);
//					}
//					if(thirdlist[j].contains("record")) {
//						thirdvar = thirdlist[j];
//					}
//				}
//				for(int j=0;j<2;j++) {
//					System.out.println("read folder:"+thirdFolder[j].getAbsolutePath());
//					fileAttribute varOne = new fileAttribute(thirdFolder[j].getAbsolutePath()+"\\cmpls.csv",",");
//					fileAttribute varTwo = new fileAttribute(secFolder.getAbsolutePath()+"\\nameList.csv",",");
//					fileAttribute varThr = new fileAttribute(secFolder.getAbsolutePath()+"\\"+thirdvar,",");
//					Map<String,String> rs = new HashMap<String,String>();
//					Map<String,String> info = new HashMap<String,String>();
//					rs = getBackInFile(varOne, varTwo, 0, 1, 0, false);//prlist id,namelist id,namelist need id
//					//System.out.println(rs.size());
//					info = getBackInFile(rs, varThr, 1, 3);
//					info = cutByAge(info,18,35);
//					File outfile = new File(thirdFolder[j].getAbsolutePath()+"\\cutlist.csv");
//					try {
//						BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile),"UTF-8"));
//						for(String keys:info.keySet()) {
//							writer.write(keys+","+info.get(keys)+"\r\n");
//						}
//						writer.close();
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} 
//				}
//			}
//		}
	}

}
