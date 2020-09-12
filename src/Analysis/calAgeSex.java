package Analysis;
import java.io.*;
import java.util.*;
import CrossData.fileAttribute;

public class calAgeSex {

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
	public static String getAgeSexStr(String str) {
		String rs = new String();
		rs = str.replaceAll("\"","");
		return rs;
	}
	public static String getAgeSexStr(String dates,String str) {
		String rs = "";
		rs += String.valueOf(getAgeSexFromId(dates,str)[1])+";";
		rs += String.valueOf(getAgeSexFromId(dates,str)[0]);
		return rs;
	}
	public static void calWithoutGroup(fileAttribute f,int attr) {
		try {
			File infile = new File(f.getFilePath());
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(infile),"UTF-8"));
			String str = "";
			Map<String,Integer> rs = new HashMap<String,Integer>();
			
			while((str = reader.readLine())!=null) {
				String[] temp = str.split(f.getFileSeparator());
				if(rs.containsKey(getAgeSexStr(temp[attr]))) {
					rs.put(getAgeSexStr(temp[attr]),rs.get(getAgeSexStr(temp[attr]))+1);
				}else {
					rs.put(getAgeSexStr(temp[attr]),1);
				}
			}
			reader.close();
			File outFolder = new File(f.getFolder()+"\\ageSex");
			if(!outFolder.exists()) {
				outFolder.mkdir();
			}
			File outfile = new File(f.getFolder()+"\\ageSex\\agesex_"+f.getFileName());
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile),"UTF-8"));
			for(String key: rs.keySet()) {
				writer.write(key+","+rs.get(key)+"\r\n");
			}
			writer.close();
		}catch(Exception e) {
			e.printStackTrace();
			
		}
	}
	public static void calWithoutGroup(fileAttribute f,int[] attr) {
		try {
			File infile = new File(f.getFilePath());
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(infile),"UTF-8"));
			String str = "";
			Map<String,Integer> rs = new HashMap<String,Integer>();
			
			while((str = reader.readLine())!=null) {
				String[] temp = str.split(f.getFileSeparator());
				if(temp[attr[1]].length()!=18) {
					continue;
				}
				if(rs.containsKey(getAgeSexStr(temp[attr[0]],temp[attr[1]]))) {
					rs.put(getAgeSexStr(temp[attr[0]],temp[attr[1]]),rs.get(getAgeSexStr(temp[attr[0]],temp[attr[1]]))+1);
				}else {
					rs.put(getAgeSexStr(temp[attr[0]],temp[attr[1]]),1);
				}
			}
			reader.close();
			File outFolder = new File(f.getFolder()+"\\ageSex");
			if(!outFolder.exists()) {
				outFolder.mkdir();
			}
			File outfile = new File(f.getFolder()+"\\ageSex\\agesex_"+f.getFileName());
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile),"UTF-8"));
			for(String key: rs.keySet()) {
				writer.write(key+","+rs.get(key)+"\r\n");
			}
			writer.close();
		}catch(Exception e) {
			e.printStackTrace();
			
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		File rootpath = new File("G:\\CombHkTl\\cutFile");
		String[] rootlist = rootpath.list();
		
		for(int i=0;i<rootlist.length;i++) {
			File tempf = new File(rootpath.getAbsoluteFile()+"\\"+rootlist[i]);
			if(tempf.isFile()&&tempf.getName().contains(".csv")) {
				if(tempf.getName().contains("tl")||tempf.getName().contains("rail")) {
					System.out.println("calculate:"+tempf.getAbsolutePath());
					int[] attr = new int[2];
					attr[0]=0;
					attr[1]=6;
					fileAttribute f = new fileAttribute(tempf.getAbsolutePath(),",","STATISTICS_DATE,OFFICE_NO,WINDOW_NO,INNER_CODE,TICKET_NO,ID_NO,ID_NAME,ID_KIND,SEX,NATION,ADDRESS,SALE_TIME,TICKET_TYPE,TRAIN_NO,BOARD_TRAIN_CODE,TRAIN_DATE,START_TIME,TRAIN_CLASS_CODE,SEAT_FEATURE,COACH_NO,SEAT_NO,SEAT_TYPE_CODE,BED_LEVEL,FROM_STATION_NAME,TO_STATION_NAME,DISTANCE,TICKET_PRICE,SALE_MODE,SEQUENCE_NO,TRADE_NO,FIRST_SEAT_TYPE");
					f.createJson();
					calWithoutGroup(f,attr);
					System.out.println("Finished!!!");
				}else {//if(tempf.getName().contains("hk")){
					System.out.println("calculate:"+tempf.getAbsolutePath());
					int attr = 3;
					fileAttribute f = new fileAttribute(tempf.getAbsolutePath(),",","dep_date,id_jm,cabin,agesex,dep,arr,dis");
					f.createJson();
					calWithoutGroup(f,attr);
				}
			}
		}
		
		File rootpath = new File("G:\\hk2014-2018\\cutFile\\filterByList\\comb");//hk
		String[] rootlist = rootpath.list();
		for(int i=0;i<rootlist.length;i++) {
			File tempf = new File(rootpath.getAbsoluteFile()+"\\"+rootlist[i]);
			if(tempf.isFile()&&tempf.getName().contains(".csv")) {
				System.out.println("calculate:"+tempf.getAbsolutePath());
				int attr = 3;
				fileAttribute f = new fileAttribute(tempf.getAbsolutePath(),",");
				calWithoutGroup(f,attr);
			}
		}
		*/
		File rootpath = new File("G:\\Train_Data-origin\\tl2015-2017\\deleteSymbol\\fileAdd\\md5\\cutFile\\filterByList\\comb");//tl
		String[] rootlist = rootpath.list();
		for(int i=0;i<rootlist.length;i++) {
			File tempf = new File(rootpath.getAbsoluteFile()+"\\"+rootlist[i]);
			if(tempf.isFile()&&tempf.getName().contains(".csv")) {
				System.out.println("calculate:"+tempf.getAbsolutePath());
				int[] attr = new int[2];
				attr[0]=16;
				attr[1]=6;
				fileAttribute f = new fileAttribute(tempf.getAbsolutePath(),",");
				//f.createJson();
				calWithoutGroup(f,attr);
			}
		}
		
		rootpath = new File("G:\\CombHkTl\\comb\\cutFile\\filterByList\\comb");//tl
		rootlist = rootpath.list();
		for(int i=0;i<rootlist.length;i++) {
			File tempf = new File(rootpath.getAbsoluteFile()+"\\"+rootlist[i]);
			if(tempf.isFile()&&tempf.getName().contains(".csv")) {
				if(tempf.getName().contains("tl")) {
					System.out.println("calculate:"+tempf.getAbsolutePath());
					int[] attr = new int[2];
					attr[0]=16;
					attr[1]=6;
					fileAttribute f = new fileAttribute(tempf.getAbsolutePath(),",");
					//f.createJson();
					calWithoutGroup(f,attr);
					System.out.println("Finished!!!");
				}else {//if(tempf.getName().contains("hk")){
					System.out.println("calculate:"+tempf.getAbsolutePath());
					int attr = 3;
					fileAttribute f = new fileAttribute(tempf.getAbsolutePath(),",");
					//f.createJson();
					calWithoutGroup(f,attr);
				}
			}
		}
	}

}
