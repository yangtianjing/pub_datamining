package ageSex;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
public class TravelTimeSta {
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
    			if(strs[15].length()!=8) {
    				continue;
    			}
    			//System.out.println(strToDate(strs[11]));
    			//System.out.println(str);
    			temp=getAgeSexFromId(strs[0],strs[5]);
    			
        		ageSex[Integer.parseInt(strs[15].substring(0, 4))-2014]
        				[Integer.parseInt(strs[15].substring(4, 6))-1]
        						[Integer.parseInt(strs[15].substring(6, 8))-1]
        								[temp[0]][holiList.get(strs[15])]+=Integer.parseInt(strs[26]);
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
    			if(strs[0].length()!=8) {
    				continue;
    			}
    			//System.out.println(strToDate(strs[11]));
    			//System.out.println(str);
    			temp=getAgeSexFromId(strs[0],strs[5]);
    			
        		ageSex[Integer.parseInt(strs[15].substring(0, 4))-2014]
        				[Integer.parseInt(strs[15].substring(4, 6))-1]
        						[Integer.parseInt(strs[15].substring(6, 8))-1]
        								[temp[0]][holiList.get(strs[15])]+=Integer.parseInt(strs[25]);
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
			//System.out.println(daycount);
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
    	}
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
    			if(strs[0].length()!=8) {
    				continue;
    			}
    			//System.out.println(strToDate(strs[11]));
    			//System.out.println(str);
    			temp=getAgeSexFromId(strs[0],strs[5]);
    			
        		ageSex[Integer.parseInt(strs[15].substring(0, 4))-2014]
        				[Integer.parseInt(strs[15].substring(4, 6))-1]
        						[Integer.parseInt(strs[15].substring(6, 8))-1]
        								[temp[0]][holiList.get(strs[15])]++;
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
		calDateTravel(holiList,"G:\\Train_Data_highway\\refined\\dy_mig_origin_highway.csv","G:\\Train_Data_highway\\result3\\dy_mig_origin_highway_time_06","06");
		calDateTravel(holiList,"G:\\Train_Data_highway\\refined\\dy_mig_origin_highway.csv","G:\\Train_Data_highway\\result3\\dy_mig_origin_highway_time_07","07");
		calDateDis(holiList,"G:\\Train_Data_highway\\refined\\dy_mig_origin_highway.csv","G:\\Train_Data_highway\\result3\\dy_mig_origin_highway_dis_06","06");
		calDateDis(holiList,"G:\\Train_Data_highway\\refined\\dy_mig_origin_highway.csv","G:\\Train_Data_highway\\result3\\dy_mig_origin_highway_dis_07","07");
		calDatePrice(holiList,"G:\\Train_Data_highway\\refined\\dy_mig_origin_highway.csv","G:\\Train_Data_highway\\result3\\dy_mig_origin_highway_price_06","06");
		calDatePrice(holiList,"G:\\Train_Data_highway\\refined\\dy_mig_origin_highway.csv","G:\\Train_Data_highway\\result3\\dy_mig_origin_highway_price_07","07");
	
	}
}
