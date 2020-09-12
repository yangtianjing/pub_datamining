package ageSex;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class TimeSta {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/agesexdata?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true";
     // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "password";
    
    static String httpUrl = "http://api.goseek.cn/Tools/holiday";
    static String apikey = "y0B8au4OgP3ZANqbjNCdPqIrLn8GyUwZ";
    
    public static String getHolidays(String httpUrl, String httpArg) {

        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            //connection.setRequestProperty("apikey", apikey);
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
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
    
    public static void calDateTravel(Map<String,Integer> holiList,String inpath,String outpath,String citycode) {
    	String str = "";
    	try {
    		int[][][][][] ageSex=new int[4][12][31][2][3];//存储年龄性别信息 year,mon,day,sex,holiday
    		for(int mon=0;mon<12;mon++) {
    			for(int day=0;day<31;day++)//初始化
    				for(int j=0;j<4;j++) 
        				for(int k=0;k<3;k++){
        					ageSex[j][mon][day][0][k]=0;
        					ageSex[j][mon][day][1][k]=0;
        				}
    		}
    		
    		int[] temp = new int[2];
    		File infile = new File(inpath);
    		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(infile),"UTF-8"));

    		System.out.println("read:"+inpath);
			str=reader.readLine();
			while((str=reader.readLine())!=null){
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
    			//System.out.println(strToDate(strs[11]));
    			//System.out.println(str);
    			temp=getAgeSexFromId(strs[0],strs[5]);
    			
        		ageSex[Integer.parseInt(strs[0].substring(0, 4))-2014]
        				[Integer.parseInt(strToDate(strs[11]).substring(4, 6))-1]
        						[Integer.parseInt(strToDate(strs[11]).substring(6, 8))-1]
        								[temp[0]][holiList.get(strToDate(strs[11]))]++;
    		}
			File outfile;
			double daycount=0,totalm=0,totalf=0,mave=0,fave=0;
			try {
				for(int year=0;year<4;year++) {
					for(int mon=0;mon<12;mon++) {
		    			for(int day=0;day<31;day++) {
		    				for(int holi=0;holi<3;holi++) {
			    				if(ageSex[year][mon][day][0][holi]!=0&&ageSex[year][mon][day][1][holi]!=0) {
			    					daycount++;
			    					totalm+=ageSex[year][mon][day][0][holi];
			    					totalf+=ageSex[year][mon][day][1][holi];
			    				}
		    				}
		    			}
		    		}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			
    		mave=totalm/daycount;
    		fave=totalf/daycount;
			for(int i=0;i<4;i++) {
				for(int j=0;j<12;j++) {
					outfile = new File(outpath+"_201"+(i+4)+"_"+(j+1)+".csv");
	    			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile),"UTF-8"));
	    			
	        		double male1=0,male2=0,male3=0,female1=0,female2=0,female3=0;
	        		for(int day=0;day<31;day++) {
	        			boolean signals = false;
	        			for(int holi=0;holi<3;holi++) {
	        				if(ageSex[i][j][day][0][holi]!=0||ageSex[i][j][day][1][holi]!=0) {
		    					signals = true;
		    				}
	        			}
	        			if(signals) {
	        				for(int holi=0;holi<3;holi++) {
		        				writer.write(ageSex[i][j][day][1][holi]/fave+",");
		        			}
	        				for(int holi=0;holi<3;holi++) {
		        				writer.write(ageSex[i][j][day][0][holi]/mave+",");
		        			}
	        				writer.write((day+1)+"\r\n");
	        			}
	        		}
	        		writer.close();
				}
			}
    		reader.close();
    	}catch(Exception e) {
    		e.printStackTrace();
    		System.out.println(str);
    		System.out.println(getHolidays(httpUrl, "d="+strToDate(str.split(",")[11])));
    	}
    }
    
    public static void calDateDis(Map<String,Integer> holiList,String inpath,String outpath,String citycode) {
    	String str = "";
    	try {
    		int[][][][][] ageSex=new int[4][12][31][2][3];//存储年龄性别信息 year,mon,day,sex,holiday
    		for(int mon=0;mon<12;mon++) {
    			for(int day=0;day<31;day++)//初始化
    				for(int j=0;j<4;j++) 
        				for(int k=0;k<3;k++){
        					ageSex[j][mon][day][0][k]=0;
        					ageSex[j][mon][day][1][k]=0;
        				}
    		}
    		
    		int[] temp = new int[2];
    		File infile = new File(inpath);
    		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(infile),"UTF-8"));

    		System.out.println("read:"+inpath);
			str=reader.readLine();
			while((str=reader.readLine())!=null){
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
    			//System.out.println(strToDate(strs[11]));
    			//System.out.println(str);
    			temp=getAgeSexFromId(strs[0],strs[5]);
    			
        		ageSex[Integer.parseInt(strs[0].substring(0, 4))-2014]
        				[Integer.parseInt(strToDate(strs[11]).substring(4, 6))-1]
        						[Integer.parseInt(strToDate(strs[11]).substring(6, 8))-1]
        								[temp[0]][holiList.get(strToDate(strs[11]))]+=Integer.parseInt(strs[25]);
    		}
			File outfile;
			double daycount=0,totalm=0,totalf=0,mave=0,fave=0;
			try {
				for(int year=0;year<3;year++) {
					for(int mon=0;mon<12;mon++) {
		    			for(int day=0;day<31;day++) {
		    				for(int holi=0;holi<3;holi++) {
			    				if(ageSex[year][mon][day][0][holi]!=0&&ageSex[year][mon][day][1][holi]!=0) {
			    					daycount++;
			    					totalm+=ageSex[year][mon][day][0][holi];
			    					totalf+=ageSex[year][mon][day][1][holi];
			    				}
		    				}
		    			}
		    		}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			
    		mave=totalm/daycount;
    		fave=totalf/daycount;
			for(int i=0;i<4;i++) {
				for(int j=0;j<12;j++) {
					outfile = new File(outpath+"_201"+(i+4)+"_"+(j+1)+".csv");
	    			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile),"UTF-8"));
	    			
	        		double male1=0,male2=0,male3=0,female1=0,female2=0,female3=0;
	        		for(int day=0;day<31;day++) {
	        			boolean signals = false;
	        			for(int holi=0;holi<3;holi++) {
	        				if(ageSex[i][j][day][0][holi]!=0||ageSex[i][j][day][1][holi]!=0) {
		    					signals = true;
		    				}
	        			}
	        			if(signals) {
	        				for(int holi=0;holi<3;holi++) {
		        				writer.write(ageSex[i][j][day][1][holi]/fave+",");
		        			}
	        				for(int holi=0;holi<3;holi++) {
		        				writer.write(ageSex[i][j][day][0][holi]/mave+",");
		        			}
	        				writer.write((day+1)+"\r\n");
	        			}
	        		}
	        		writer.close();
				}
			}
    		reader.close();
    	}catch(Exception e) {
    		e.printStackTrace();
    		System.out.println(str);
    		System.out.println(getHolidays(httpUrl, "d="+strToDate(str.split(",")[11])));
    	}
    }
    public static void calDatePrice(Map<String,Integer> holiList,String inpath,String outpath,String citycode) {
    	String str = "";
    	try {
    		int[][][][][] ageSex=new int[4][12][31][2][3];//存储年龄性别信息 year,mon,day,sex,holiday
    		for(int mon=0;mon<12;mon++) {
    			for(int day=0;day<31;day++)//初始化
    				for(int j=0;j<4;j++) 
        				for(int k=0;k<3;k++){
        					ageSex[j][mon][day][0][k]=0;
        					ageSex[j][mon][day][1][k]=0;
        				}
    		}
    		
    		int[] temp = new int[2];
    		File infile = new File(inpath);
    		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(infile),"UTF-8"));
    		System.out.println("read:"+inpath);
			str=reader.readLine();
			while((str=reader.readLine())!=null){
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
    			//System.out.println(strToDate(strs[11]));
    			//System.out.println(str);
    			temp=getAgeSexFromId(strs[0],strs[5]);
    			
        		ageSex[Integer.parseInt(strs[0].substring(0, 4))-2014]
        				[Integer.parseInt(strToDate(strs[11]).substring(4, 6))-1]
        						[Integer.parseInt(strToDate(strs[11]).substring(6, 8))-1]
        								[temp[0]][holiList.get(strToDate(strs[11]))]+=Integer.parseInt(strs[26]);
    		}
			File outfile;
			double daycount=0,totalm=0,totalf=0,mave=0,fave=0;
			try {
				for(int year=0;year<4;year++) {
					for(int mon=0;mon<12;mon++) {
		    			for(int day=0;day<31;day++) {
		    				for(int holi=0;holi<3;holi++) {
			    				if(ageSex[year][mon][day][0][holi]!=0&&ageSex[year][mon][day][1][holi]!=0) {
			    					daycount++;
			    					totalm+=ageSex[year][mon][day][0][holi];
			    					totalf+=ageSex[year][mon][day][1][holi];
			    				}
		    				}
		    			}
		    		}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			
    		mave=totalm/daycount;
    		fave=totalf/daycount;
			for(int i=0;i<4;i++) {
				for(int j=0;j<12;j++) {
					outfile = new File(outpath+"_201"+(i+4)+"_"+(j+1)+".csv");
	    			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile),"UTF-8"));
	    			
	        		double male1=0,male2=0,male3=0,female1=0,female2=0,female3=0;
	        		for(int day=0;day<31;day++) {
	        			boolean signals = false;
	        			for(int holi=0;holi<3;holi++) {
	        				if(ageSex[i][j][day][0][holi]!=0||ageSex[i][j][day][1][holi]!=0) {
		    					signals = true;
		    				}
	        			}
	        			if(signals) {
	        				for(int holi=0;holi<3;holi++) {
		        				writer.write(ageSex[i][j][day][1][holi]/fave+",");
		        			}
	        				for(int holi=0;holi<3;holi++) {
		        				writer.write(ageSex[i][j][day][0][holi]/mave+",");
		        			}
	        				writer.write((day+1)+"\r\n");
	        			}
	        		}
	        		writer.close();
				}
			}
    		reader.close();
    	}catch(Exception e) {
    		e.printStackTrace();
    		System.out.println(str);
    		System.out.println(getHolidays(httpUrl, "d="+strToDate(str.split(",")[11])));
    	}
    }
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Map<String,Integer> holiList = new HashMap<String,Integer>();
		try {
			File holiday = new File("G:\\HolidayList.csv");
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(holiday),"UTF-8"));
			String str= reader.readLine();
			holiList.put(str.split(",")[0], Integer.parseInt(str.split(",")[1]));
			while((str=reader.readLine())!=null) {
				holiList.put(str.split(",")[0], Integer.parseInt(str.split(",")[1]));
			}
			reader.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		//System.out.println("step1");
		calDateTravel(holiList,"G:\\Train_Data_highway\\refined\\dy_mig_origin_highway.csv","G:\\Train_Data_highway\\result2\\dy_mig_origin_highway_time_06","06");
		calDateTravel(holiList,"G:\\Train_Data_highway\\refined\\dy_mig_origin_highway.csv","G:\\Train_Data_highway\\result2\\dy_mig_origin_highway_time_07","07");
		calDateDis(holiList,"G:\\Train_Data_highway\\refined\\dy_mig_origin_highway.csv","G:\\Train_Data_highway\\result2\\dy_mig_origin_highway_dis_06","06");
		calDateDis(holiList,"G:\\Train_Data_highway\\refined\\dy_mig_origin_highway.csv","G:\\Train_Data_highway\\result2\\dy_mig_origin_highway_dis_07","07");
		calDatePrice(holiList,"G:\\Train_Data_highway\\refined\\dy_mig_origin_highway.csv","G:\\Train_Data_highway\\result2\\dy_mig_origin_highway_price_06","06");
		calDatePrice(holiList,"G:\\Train_Data_highway\\refined\\dy_mig_origin_highway.csv","G:\\Train_Data_highway\\result2\\dy_mig_origin_highway_price_07","07");
	}

}
