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

public class ComparaTSP {
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
	public static int saleAndTravel(String travel,String sale) {
		if(sale.equals(travel)) {
			return 0;
		}else if(Integer.parseInt(sale.substring(4,6))==Integer.parseInt(travel.substring(4,6))) {
			return Math.abs(Integer.parseInt(travel.substring(6,8))-Integer.parseInt(sale.substring(6,8)));
		}else if(Integer.parseInt(sale.substring(4,6))<Integer.parseInt(travel.substring(4,6))){
			int daycount=31;
			switch(sale.substring(4,6)) {
				case "01": daycount=31; break;
				case "02": if(Integer.parseInt(sale.substring(0,4))%4==0) {
					daycount=29;
				}else {
					daycount=28;
				}			
				break;
				case "03": daycount=31; break;
				case "04": daycount=30; break;
				case "05": daycount=31; break;
				case "06": daycount=30; break;
				case "07": daycount=31; break;
				case "08": daycount=31; break;
				case "09": daycount=30; break;
				case "10": daycount=31; break;
				case "11": daycount=30; break;
				case "12": daycount=31; break;
				
			}
			return Integer.parseInt(travel.substring(6,8))+(daycount-Integer.parseInt(sale.substring(6,8)));
		}else if(Integer.parseInt(sale.substring(4,6))>Integer.parseInt(travel.substring(4,6))) {
			String temp="";
			temp=sale;
			sale = travel;
			travel=temp;
			int daycount=31;
			switch(sale.substring(4,6)) {
				case "01": daycount=31; break;
				case "02": if(Integer.parseInt(sale.substring(0,4))%4==0) {
					daycount=29;
				}else {
					daycount=28;
				}			
				break;
				case "03": daycount=31; break;
				case "04": daycount=30; break;
				case "05": daycount=31; break;
				case "06": daycount=30; break;
				case "07": daycount=31; break;
				case "08": daycount=31; break;
				case "09": daycount=30; break;
				case "10": daycount=31; break;
				case "11": daycount=30; break;
				case "12": daycount=31; break;
				
			}
			return Integer.parseInt(travel.substring(6,8))+(daycount-Integer.parseInt(sale.substring(6,8)));
		}
		return 65;
	}
    
	public static void calDateDif(Map<String,Integer> holiList,String inpath,String outpath,String citycode) {
    	String str = "";
    	int linelen=0;
    	try {
    		int[][] difference = new int[4][65];
    		for(int i=0;i<4;i++) {
    			for(int j=0;j<65;j++) {
    				difference[i][j]=0;
    			}
    		}
    		
    		int[] temp = new int[2];
    		File infile = new File(inpath);
    		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(infile),"UTF-8"));
    		System.out.println("read:"+inpath);
			str=reader.readLine();
			str=reader.readLine();
			linelen=str.split(",").length;
			int defaults=0;
			while((str=reader.readLine())!=null){
    			String[] strs = str.split(",");
    			if(strs.length!=linelen) {
    				continue;
    			}
    			if(strs[5].length()!=18||(!strs[5].substring(2, 4).equals(citycode))) {
        			continue;
        		}
    			if(strs[11].equals("")||strs[11].split(" ").length<3) {
    				continue;
    			}
    			if(strToDate(strs[11]).length()!=8) {
    				continue;
    			}
    			if(Integer.parseInt(strs[15].substring(4, 6))<4) {
    				defaults=1;
    			}else {
    				defaults=0;
    			}
    			difference[Integer.parseInt(strs[15].substring(0, 4))-2014-defaults][saleAndTravel(strs[15],strToDate(strs[11]))]++;
        		
    		}
			File outfile;
			for(int i=0;i<4;i++) {
				outfile = new File(outpath+"_201"+(i+4)+".csv");
    			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile),"UTF-8"));
        		for(int dif=0;dif<65;dif++) {
        			writer.write(dif+","+difference[i][dif]+"\r\n");
        		}
        		writer.close();
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
		calDateDif(holiList,"G:\\Train_Data_highway\\refined\\dy_mig_origin_highway.csv","G:\\Train_Data_highway\\result4\\dy_mig_origin_highway_price_06","06");
		calDateDif(holiList,"G:\\Train_Data_highway\\refined\\dy_mig_origin_highway.csv","G:\\Train_Data_highway\\result4\\dy_mig_origin_highway_price_07","07");
	}
}
