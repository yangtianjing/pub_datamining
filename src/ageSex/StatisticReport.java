package ageSex;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import CrossData.fileAttribute;


public class StatisticReport {
	public static int[] getAgeSexFromId(String dates,String id) {
    	int[] ageSex=new int[2];
    	String age = (String) id.subSequence(6, 14);
    	String sex = (String) id.subSequence(16, 17);
    	if(Integer.parseInt(sex)%2==1) {
    		ageSex[0]=1;//ÆæÊýÎªÄÐ
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
	public static void agesexReport(fileAttribute f, int attr, int trans, int down, int up, String dates) {
		try {
			Map<String,Integer> agesex = new HashMap<String,Integer>();
			for(int i=down;i<=up;i++) {
				String t = i+",0";
				agesex.put(t,0);
				t = i+",1";
				agesex.put(t,0);
			}
			File infile = new File(f.getFilePath());
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(infile),"UTF-8"));
			String str = "";
			while((str = br.readLine())!=null) {
				String[] temp = str.split(f.getFileSeparator());
				String tag = temp[attr];
				if(tag.length()<18&&trans==1) {
					continue;
				}
				if(tag.contains("null")) {
					continue;
				}
				String tags = "";
				if(trans == 1) {
//					System.out.println(tag);
					int[] t =  getAgeSexFromId(dates,tag);
					tags = t[1]+","+t[0];
				}else if(trans ==2 ){
					tags = tag.replaceAll("\"", "").replaceAll(";", ",");
					int age = Integer.parseInt(tags.split(",")[0])-Integer.parseInt(dates);
					tags = age + "," +tags.split(",")[1];
					
				}else {
					tags = tag;
				}
				if(Integer.parseInt(tags.split(",")[0])>=down&&Integer.parseInt(tags.split(",")[0])<=up) {
					if(agesex.containsKey(tags)) {
						agesex.put(tags,agesex.get(tags)+1);
					}
				}
			}
			br.close();
			File outf = new File(f.getFolder()+"\\reports\\agesexReport_"+f.getFileName());
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outf),"UTF-8"));
			for(int i=down;i<=up;i++) {
				bw.write(i+":"+agesex.get(i+","+"1")+","+agesex.get(i+","+"0")+"\r\n");
			}
			bw.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void frequencyReport(fileAttribute f, int attr, int down, int up) {
		try {
			Map<Integer,Integer> freq = new HashMap<Integer,Integer>();
			Map<String,Integer> namels = new HashMap<String,Integer>();
			for(int i=down;i<=up;i++) {
				freq.put(i,0);
//				System.out.println(i+","+freq.get(i));
			}
			File infile = new File(f.getFilePath());
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(infile),"UTF-8"));
			String str = "";
			while((str = br.readLine())!=null) {
				String[] temp = str.split(f.getFileSeparator());
				String tag = temp[attr];
				if(tag.length()<18) {
					continue;
				}
				if(tag.contains("null")) {
					continue;
				}
				if(namels.containsKey(tag)) {
					namels.put(tag,namels.get(tag)+1);
				}else {
					namels.put(tag,1);
				}
			}
			br.close();
			File outf = new File(f.getFolder()+"\\reports\\freqReport_"+f.getFileName());
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outf),"UTF-8"));
			for(String key:namels.keySet()) {
				if(freq.containsKey(namels.get(key))) {
					freq.put(namels.get(key),freq.get(namels.get(key))+1);
				}else {
					if(namels.get(key)>up) {
						freq.put(up,freq.get(up)+1);
					}
				}
			}
			for(int i=down;i<=up;i++) {
				bw.write(i+":"+freq.get(i)+"\r\n");
			}
			bw.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int attr = 6;//agesex
		int attr2 = 6;//id
		int trans = 1;//1 is TL, 2 is HK
		int datepos = 1;//-1 is HK, 1 is TL
		int datepos2 = 2;
		
		File rootf = new File("E:\\dataManageWorkplace\\20191228\\TL\\total\\cutFile\\filterByFileAttr\\fixed");
		String[] rootls = rootf.list();
		File folder = new File(rootf.getAbsolutePath()+"\\reports");
		folder.mkdir();
		for(int i=0;i<rootls.length;i++) {
			File tf = new File(rootf.getAbsolutePath()+"\\"+rootls[i]);
			if(!tf.isFile()||(!rootls[i].contains("csv"))||rootls[i].contains("nameList")) {
				continue;
			}
			System.out.println(rootf.getAbsolutePath()+"\\"+rootls[i]);
			fileAttribute f = new fileAttribute(rootf.getAbsolutePath()+"\\"+rootls[i],",");
			String dates = "";
			if(datepos == -1) {
				dates = ""+ (-Integer.parseInt(rootls[i].split("_")[datepos2]) + 2018);
			}else {
				dates = rootls[i].split("_")[datepos]+"1231";
			}
			agesexReport(f, attr, trans, 0, 100, dates);//
			frequencyReport(f, attr2, 1, 100);
		}
	}

}
