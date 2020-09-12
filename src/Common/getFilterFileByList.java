package Common;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import CrossData.fileAttribute;

public class getFilterFileByList {
	
	public static int[] getAgeSexFromId(String dates,String id) {
    	int[] ageSex=new int[2];
    	String age = (String) id.subSequence(6, 14);
    	String sex = (String) id.subSequence(16, 17);
    	if(Integer.parseInt(sex)%2==1) {
    		ageSex[0]=1;//奇数为男
    	}else {
    		ageSex[0]=0;
    	}
    	String x=(String)age.substring(0, 4);
    	String y=(String)age.substring(4, 6);
    	String z=(String)age.substring(6);
    	if(Integer.parseInt(x)>2018||Integer.parseInt(x)<1800||
    			Integer.parseInt(y)>12||Integer.parseInt(y)<=0||
    			Integer.parseInt(z)>31||Integer.parseInt(z)<=0)
    		age="19800101";
    	//System.out.println(age);
    	String ages = age.substring(0, 4);
    	String ages1 = age.substring(4, 6);
    	int year=Integer.parseInt(dates.substring(0, 4))-Integer.parseInt(ages);
    	//System.out.println(dates.toString().substring(0, 4));
    	int mon = Integer.parseInt(dates.substring(4, 6))-Integer.parseInt(ages1);
    	if(mon<0) year=year-1;
    	ageSex[1]=year;
    	if((ageSex[0]!=1&&ageSex[0]!=0)||ageSex[1]<=0||ageSex[1]>=120) {
    		ageSex[0]=0;
    		ageSex[1]=0;
    	}
    	return ageSex;
    }
	
	public static void filterFileByCondition(fileAttribute f, int attr, int trans, int up, int down) {
		try {
			File tf = new File(f.getFilePath());
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(tf),"UTF-8"));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tf.getParent()+"\\filterByCondition\\"+tf.getName()),"UTF-8"));
			String str = br.readLine();
			while((str = br.readLine())!=null) {
				String[] temp = str.split(f.getFileSeparator());
				String tag = temp[attr];
				if(tag.length()<18&&trans==1) {
					continue;
				}
				if(tag.contains("null")) {
					continue;
				}
				int tags = 0;
				if(trans == 1) {
//					System.out.println(tag);
					tags = getAgeSexFromId("20181231",tag)[1];
				}else if(trans ==2 ){
					tags = Integer.parseInt(tag.split(";")[0].replaceAll("\"", ""));
				}else {
					tags = Integer.parseInt(tag);
				}
				if(tags >=down&&tags<=up) {
					bw.write(str+"\r\n");
				}
			}
			br.close();
			bw.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void filterByFileAttr(String lspath, fileAttribute f, int lsattr, int fattr) {
		try {
			File tf = new File(lspath);
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(tf),"UTF-8"));
			String str = "";
			Map<String,String> namels = new HashMap<String,String>();
			while((str = br.readLine())!=null) {
				String[] temp = str.split(",");
				if(!namels.containsKey(temp[lsattr])) {
					namels.put(temp[lsattr],"");
				}
			}
			br.close();
			File fs = new File(f.getFilePath());
			File folder = new File(f.getFolder()+"\\filterByFileAttr");
			folder.mkdir();
			File outf = new File(f.getFolder()+"\\filterByFileAttr\\"+f.getFileName());
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fs),"UTF-8"));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outf),"UTF-8"));
			while((str = reader.readLine())!=null) {
				String[] temp = str.split(",");
				if(namels.containsKey(temp[fattr])) {
					writer.write(str+"\r\n");
				}
			}
			reader.close();
			writer.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void filterFileByList(String listpath,fileAttribute[] f,int attr) {
		Map<String,String> lists = new HashMap<String,String>();
		
		try{
			File listfile = new File(listpath);
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(listfile),"UTF-8"));
			String str = "";
			System.out.println("read file:"+listpath);
			while((str = reader.readLine()) != null) {//读入匹配列表
				if(lists.containsKey(str)) {
					System.err.println("出现重复值："+str);
					continue;
				}
				lists.put(str, "");
			}
			reader.close();
			for(int i=0;i<f.length;i++) {
				System.out.println("comb file:"+f[i].getFilePath());
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(f[i].getFilePath())),"UTF-8"));
				File outfolder = new File(f[i].getFolder()+"\\filterByList\\");
				if(!outfolder.exists()) { 
					outfolder.mkdir();
					File folders = new File(outfolder.getAbsoluteFile()+"\\comb");
					folders.mkdir();
					folders = new File(outfolder.getAbsoluteFile()+"\\anti");
					folders.mkdir();
				}
				File outfile = new File(outfolder.getAbsoluteFile()+"\\comb\\"+f[i].getFileName());
				File outfile2 = new File(outfolder.getAbsoluteFile()+"\\anti\\"+f[i].getFileName());
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile),"UTF-8"));
				BufferedWriter writer2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile2),"UTF-8"));
				while((str = reader.readLine())!=null) {
					String[] temp = str.split(",");
					if(lists.containsKey(temp[attr])) {
						writer.write(str+"\r\n");
					}else {
						writer2.write(str+"\r\n");
					}
				}
				writer.close();
				writer2.write(str+"\r\n");
				reader.close();
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		File rootf = new File("E:\\dataManageWorkplace\\20191228\\HK\\total\\cutFile");
		String[] rootls = rootf.list();
		String[] local = {"dy","mig","ctu"};
		int lsattr = 0,fattr = 1;
		for(int i=0;i<local.length;i++) {
			File namef = new File("E:\\dataManageWorkplace\\20191228");
			String[] namefls = namef.list();
			String lspath = "";
			for(int k = 0;k<namefls.length;k++) {
				if(namefls[k].contains(local[i])&&namefls[k].contains("csv")) {
					lspath = namef.getAbsolutePath()+"\\"+namefls[k];
					break;
				}
			}
			for(int j=0;j<rootls.length;j++) {
				File tf = new File(rootf.getAbsolutePath()+"\\"+rootls[j]);
				if(tf.isFile()&&rootls[j].contains("csv")&&rootls[j].contains(local[i])) {
					System.out.println(tf.getAbsolutePath());
					fileAttribute f = new fileAttribute(rootf.getAbsolutePath()+"\\"+rootls[j],",");
					filterByFileAttr(lspath, f, lsattr, fattr);
				}
			}
		}
		for(int i=0;i<rootls.length;i++) {
			
		}
		/*
		File rootf = new File("G:\\hk2014-2018");
		File folder = new File(rootf.getAbsolutePath()+"\\filterByCondition");
		if(!folder.exists()) {
			folder.mkdir();
		}
		String[] rootls = rootf.list();
		for(int i=0;i<rootls.length;i++) {
			File tf = new File(rootf.getAbsolutePath()+"\\"+rootls[i]);
			if(tf.isFile()&&rootls[i].contains("csv")) {
				System.out.println(tf.getAbsolutePath());
				fileAttribute f = new fileAttribute(rootf.getAbsolutePath()+"\\"+rootls[i],",");
				filterFileByCondition(f, 3, 2, 36, 21);
			}
		}
		*/
//		String loc = "dy";
//		for(int i=0;i<3;i++) {
//			if(i==1) {
//				loc = "mig";
//			}else if(i==2) {
//				loc = "ctu";
//			}
//			File rootfile = new File("G:\\CombHkTl\\cutFile\\distinctData");
//			//
//			String[] rootlist = rootfile.list();
//			String listpath = "";
//			String listname="";
//			for(int j=0;j<rootlist.length;j++) {
//				if(!rootlist[j].contains(loc)) {
//					continue;	
//				}
//				listpath = rootfile.getAbsolutePath()+"\\"+rootlist[j];
//				listname = rootlist[j].replaceAll("attrDistinct_2015_", "");
//				int counts = 0;
//				File rootfile2 = new File("G:\\CombHkTl\\cutFile");
//				String[] rootlist2 = rootfile2.list();
//				for(int k=0;k<rootlist2.length;k++) {
//					if(!rootlist2[k].contains(listname)) {
//						continue;	
//					}
//					counts++;
//				}
//				fileAttribute[] f = new fileAttribute[counts];
//				int cnt = 0;
//				for(int k=0;k<rootlist2.length;k++) {
//					if(!rootlist2[k].contains(listname)) {
//						continue;	
//					}
//					f[cnt] = new fileAttribute(rootfile2.getAbsolutePath()+"\\"+rootlist2[k],",");
//					cnt++;
//					if(cnt>counts) {
//						System.err.println("非系统错误");
//					}
//				}
//				if(listname.contains("hk")) {
//					filterFileByList(listpath,f,1);
//				}else {
//					filterFileByList(listpath,f,5);
//				}
//			}
//		}
	}

}
