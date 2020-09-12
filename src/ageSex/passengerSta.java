package ageSex;
import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
public class passengerSta {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/gtjjx?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true";
     // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "yangtianjing";
    public static Map<String,String> getDateList(){
    	String mon="31,28,31,30,31,30,31,31,30,31,30,31,"
				+ "31,28,31,30,31,30,31,31,30,31,30,31,"
				+ "31,29,31,30,31,30,31,31,30,31,30,31,"
				+ "31,28,31,30,31,30,31,31,30,31,30,31";
		
		String[] monlist = mon.split(",");
		int counts = 3;
		Map<String,String> maplist = new HashMap<String,String>();
		for(int i=0;i<monlist.length;i++) {
			for(int j=0;j<Integer.parseInt(monlist[i]);j++) {
				String value="";
				String mons="";
				String days="";
				if(i%12<9) {
					mons="0"+(i%12+1);
				}else {
					mons=String.valueOf(i%12+1);
				}
				if(j<9) {
					days="0"+(j+1);
				}else {
					days=String.valueOf(j+1);
				}
				if(counts==7) {
					value=String.valueOf(counts);
					counts=0;
				}else {
					value=String.valueOf(counts);
				}
				//System.out.println("201"+(4+i/12)+mons+days+","+value+","+counts);
				maplist.put("201"+(4+i/12)+mons+days, value);
				counts++;
			}
		}
		String holiday="20140101,2;20140126,0;20140131,2;20140201-20140206,2;"
				+ "20140208,0;20140405-20140407,2;20140501-20140503,2;20140504,0;"
				+ "20140531,2;20140601,2;20140602,2;20140906-20140908,2;20140928,0;"
				+ "20141001-20141007,2;20141011,0;20150101-20150103,2;20150104,0;"
				+ "20150215,0;20150218-20150224,2;20150228,0;20150404-20150406,2;"
				+ "20150501-20150503,2;20150620-20150622,2;20150903-20150905,2;20150906,0;"
				+ "20150926-20150927,2;20151001-20151007,2;20151010,0;20160101-20160103,2;"
				+ "20160206,0;20160207-20160213,2;20160204,0;20160402-20160404,2;20160430,2;"
				+ "20160501-20160502,2;20160609-20160611,2;20160612,0;20160915-20160917,2;"
				+ "20160918,0;20161001-20161007,2;20161008-20161009,0;20161231,2;20170101-20170102,2;"
				+ "20170122,0;20170127-20170131,2;20170201-20170202,2;20170204,0;20170401,0;"
				+ "20170402-20170404,2;20170429-20170430,2;20170501,2;20170527,0;20170528-20170530,2;"
				+ "20170930,0;20171001-20171008,2;20171230-20171231,2";
		String[] holilist = holiday.split(";");
		for(int i=0;i<holilist.length;i++) {
			String[] temp=holilist[i].split(",");
			String[] holidate=temp[0].split("-");
			//System.out.println(holidate.length);
			int lentemp=0;
			if(holidate.length==1) {
				lentemp=1;
			}else {
				lentemp=Integer.parseInt(holidate[1].substring(6, 8))-Integer.parseInt(holidate[0].substring(6, 8))+1;
			}
			for(int k=0;k<lentemp;k++) {
				int dcount=Integer.parseInt(holidate[0].substring(6, 8))+k;
				String keys="";
				if(dcount<10) {
					keys=holidate[0].substring(0, 6)+"0"+dcount;
				}else {
					keys=holidate[0].substring(0, 6)+dcount;
				}
				if(temp[1].equals("2")) {
					maplist.put(keys, "8");
				}
			}
		}
		return maplist;
    }
    public static String strToDate(String datestr) {
    	String result ="";
    	String[] rst = datestr.split(" ");
    	String[] rs = new String[3];
    	int temp=0;
    	for(int i=0;i<rst.length;i++) {
    		if(!rst[i].equals("")) {
    			rs[temp]=rst[i];
    			temp++;
    			if(temp>=3) {
    				break;
    			}
    		}
    	}
    	result+=rs[2];
    	switch(rs[0]) {
    		case "Jan": result+="01"; break;
    		case "Feb": result+="02"; break;
    		case "Mar": result+="03"; break;
    		case "Apr": result+="04"; break;
    		case "May": result+="05"; break;
    		case "Jun": result+="06"; break;
    		case "Jul": result+="07"; break;
    		case "Aug": result+="08"; break;
    		case "Sep": result+="09"; break;
    		case "Oct": result+="10"; break;
    		case "Nov": result+="11"; break;
    		case "Dec": result+="12"; break;
    		default: break;
    	}
    	//System.out.println(rs[0]);
    	if(Integer.parseInt(rs[1])<10) {
    		result+="0"+rs[1];
    	}else {
    		result+=rs[1];
    	}
    	return result;
    }
    public static void cutRecordByDate(String citycode,File infile,File[] outfile) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(infile),"UTF-8"));
			BufferedWriter[] writer = new BufferedWriter[3];//(new OutputStreamWriter(new FileOutputStream(outfile),"UTF-8"));
			for(int i=0;i<3;i++) {
				writer[i] = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile[i]),"UTF-8"));
			}
			String str;
			while((str=reader.readLine())!=null) {
				String[] strs = str.split(",");
    			if(strs[5].length()!=18||(!strs[5].substring(2, 4).equals(citycode))) {
        			continue;
        		}
    			if(strs[11].equals("")||strs[11].split(" ").length<3) {
    				continue;
    			}
    			if(strToDate(strs[11]).length()!=8) {
    				continue;
    			}
    			int year = Integer.parseInt(strs[15].substring(0, 4))-2014;
    			if(Integer.parseInt(strs[15].substring(4, 6))-1<3) {
    				year--;
    			}
    			if(year>=3) {
    				continue;
    			}else {
    				writer[year].write(str+"\r\n");
    			}
			}
			reader.close();
			for(int i=0;i<3;i++) {
				writer[i].close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Map<String,String> records = new HashMap<String,String>();
			//cut files
			
			File infile = new File("G:\\Train_Data_highway\\refined\\dy_mig_origin_highway.csv");
			File[] outfile=new File[3];
			String citycode="06";
			outfile[0]=new File("G:\\Train_Data_highway\\refined\\datasource\\dy_mig_origin_highway_"+citycode+"_1.csv");
			outfile[1]=new File("G:\\Train_Data_highway\\refined\\datasource\\dy_mig_origin_highway_"+citycode+"_2.csv");
			outfile[2]=new File("G:\\Train_Data_highway\\refined\\datasource\\dy_mig_origin_highway_"+citycode+"_3.csv");
			System.out.println("read File...");
			cutRecordByDate(citycode,infile,outfile);
			citycode="07";
			outfile[0]=new File("G:\\Train_Data_highway\\refined\\datasource\\dy_mig_origin_highway_"+citycode+"_1.csv");
			outfile[1]=new File("G:\\Train_Data_highway\\refined\\datasource\\dy_mig_origin_highway_"+citycode+"_2.csv");
			outfile[2]=new File("G:\\Train_Data_highway\\refined\\datasource\\dy_mig_origin_highway_"+citycode+"_3.csv");
			System.out.println("read File...");
			cutRecordByDate(citycode,infile,outfile);
			
			
			//Frequence cal
			
			File filepath= new File("G:\\Train_Data_highway\\refined\\datasource");
			String[] filelist = filepath.list();
			
			for(int i=0;i<filelist.length;i++) {
				if(!filelist[i].contains("csv")) {
					continue;
				}
				System.out.println("read File "+filelist[i]+".....");
				records = new HashMap<String,String>();
				File thisFile = new File(filepath+"\\"+filelist[i]);
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(thisFile),"UTF-8"));
				String tempstr="";
				while((tempstr=reader.readLine())!=null) {
					String keys=tempstr.split(",")[5];
					if(records.containsKey(keys)) {
						records.put(keys, records.get(keys)+";"+tempstr);
					}else {
						records.put(keys, tempstr);
					}
				}
				reader.close();
				//System.out.println("read File "+filelist[i]+".....");
				String tempfile = filepath+"\\frequence\\"+filelist[i].split(".csv")[0]+"_Feq.csv";
				File pTravel = new File(tempfile);
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pTravel),"UTF-8"));
				Iterator keycount = records.keySet().iterator();
				int[] pTravelRs = new int[100];
				for(int k=0;k<100;k++) {
					pTravelRs[k]=0;
				}
				//System.out.println("read File "+filelist[i]+".....");
				while(keycount.hasNext()) {
					String keys = (String)keycount.next();
					int counts = records.get(keys).split(";").length;
					if(counts<100) {
						pTravelRs[counts-1]++;
					}else {
						pTravelRs[99]++;
					}
				}
				for(int k=0;k<100;k++) {
					writer.write((k+1)+","+pTravelRs[k]+"\r\n");
				}
				writer.close();
			}
			
			
			//passengers team
			
			
			for(int i=0;i<filelist.length;i++) {
				if(!filelist[i].contains("csv")) {
					continue;
				}
				System.out.println("read File "+filelist[i]+".....");
				records = new HashMap<String,String>();
				File thisFile = new File(filepath+"\\"+filelist[i]);
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(thisFile),"UTF-8"));
				String tempstr="";
				while((tempstr=reader.readLine())!=null) {
					String keys="";
					if(tempstr.split(",").length<29) {
						continue;
					}
					if(tempstr.split(",")[28].equals("")) {
						if(tempstr.split(",")[29].equals("")) {
							continue;
						}else {
							keys=tempstr.split(",")[29];
						}
					}else {
						keys=tempstr.split(",")[28];
					}
					if(records.containsKey(keys)) {
						records.put(keys, records.get(keys)+";"+tempstr);
					}else {
						records.put(keys, tempstr);
					}
				}
				reader.close();
				//System.out.println("read File "+filelist[i]+".....");
				String tempfile = filepath+"\\team\\"+filelist[i].split(".csv")[0]+"_Team.csv";
				File pTravel = new File(tempfile);
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pTravel),"UTF-8"));
				Iterator keycount = records.keySet().iterator();
				int[] pTravelRs = new int[100];
				for(int k=0;k<100;k++) {
					pTravelRs[k]=0;
				}
				//System.out.println("read File "+filelist[i]+".....");
				while(keycount.hasNext()) {
					String keys = (String)keycount.next();
					int counts = records.get(keys).split(";").length;
					if(counts<100) {
						pTravelRs[counts-1]++;
					}else {
						pTravelRs[99]++;
					}
				}
				for(int k=0;k<100;k++) {
					writer.write((k+1)+","+pTravelRs[k]+"\r\n");
				}
				writer.close();
			}
			
			
			
			//travel time
			
			//get Date list
			Map<String,String> maplist = getDateList();
			
			//get week count
			for(int i=0;i<filelist.length;i++) {
				if(!filelist[i].contains("csv")) {
					continue;
				}
				System.out.println("read File "+filelist[i]+".....");
				records = new HashMap<String,String>();
				File thisFile = new File(filepath+"\\"+filelist[i]);
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(thisFile),"UTF-8"));
				String tempstr="";
				while((tempstr=reader.readLine())!=null) {
					String keys="";
					if(tempstr.split(",").length<16) {
						continue;
					}
					keys=tempstr.split(",")[15];
					if(records.containsKey(keys)) {
						records.put(keys, records.get(keys)+";"+"r");
					}else {
						records.put(keys, "r");
					}
				}
				reader.close();
				//System.out.println("read File "+filelist[i]+".....");
				String tempfile = filepath+"\\travel\\"+filelist[i].split(".csv")[0]+"_Travel.csv";
				File pTravel = new File(tempfile);
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pTravel),"UTF-8"));
				Iterator keycount = records.keySet().iterator();
				int[] pTravelRs = new int[7];
				for(int k=0;k<7;k++) {
					pTravelRs[k]=0;
				}
				//System.out.println("read File "+filelist[i]+".....");
				while(keycount.hasNext()) {
					String keys = (String)keycount.next();
					int counts = Integer.parseInt(maplist.get(keys));
					if(counts>7) {
						continue;
					}
					int counts2 = records.get(keys).split(";").length;
					pTravelRs[counts-1]+=counts2;
				}
				for(int k=0;k<7;k++) {
					writer.write((k+1)+","+pTravelRs[k]+"\r\n");
				}
				writer.close();
			}
			
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
