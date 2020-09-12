package Common;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import CrossData.fileAttribute;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.Map;
import java.util.jar.Attributes.Name;
import java.util.HashMap;

public class readfilelines {
	public static void readFileLines(String filepath,int len,String encode) {
		File f = new File(filepath);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f),encode));
			int counts=0;
			String str="";
			while((str=br.readLine())!=null) {
//				if (!str.contains("20190101")) {
//
//					// continue;
//				}
				//System.out.println(str.split(",").length);
				counts++;
				//System.out.println(str);
				
//				if(counts>len) {
//					break;
//				}
			}
			System.out.println(counts);
			br.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void readFileLines(String filepath,int start,int end,String encode) {
		File f = new File(filepath);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f),encode));
			int counts=0;
			String str="";
			while((str=br.readLine())!=null) {
				counts++;
				if(counts>start)
					System.out.println(str);
				if(counts>end) {
					break;
				}
			}
			System.out.println(counts);
			br.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void countFile(String filepath) {
		File f = new File(filepath);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF-8"));
			int counts=0;
			String str="";
			while((str=br.readLine())!=null) {
				counts++;
			}
			System.out.println(f.getName()+" : "+counts);
			br.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void getFileByField(String fpath,String fpath2) {
		File f = new File(fpath);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF-8"));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fpath2),"UTF-8"));
			String str = "";
			while((str=br.readLine())!=null) {
				if(str.contains(",成都")) {
					if(str.contains(",青城山,")||str.contains(",都江堰,")) {
						bw.write(str+"\r\n");
					}
				}
			}
			br.close();
			bw.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void countDistinct(String[] filepath, int attr) {
		Map<String,Integer> list = new HashMap<String, Integer>();
		int counts=0;
		for(int i=0;i<filepath.length;i++) {
			if(filepath[i].equals("unknown")) {
				continue;
			}
			File f = new File(filepath[i]);
			counts = list.size();
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF-8"));
				
				String str="";
				while((str=br.readLine())!=null) {
					String temp = str.split(",")[attr];
					if(!list.containsKey(temp)) {
						list.put(temp,1);
					}
				}
				
				System.out.println(f.getName()+ "  " + String.valueOf(list.size()-counts));
				br.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("total:  " + String.valueOf(list.size()));
	}
	public static void main(String[] args) {
		
		//String filepath="G:\\Train_Data-origin\\id_sale_201404.csv";
		//readFileLines(filepath,0,10000000,"UTF-8");
		//System.out.println("statistics_date,office_no,window_no,ticket_no,inner_code,id_kind,id_no,id_name,transmit_flag,sex,nation,birth,address,subscribe,valid_start_date,valid_stop_date,statist_date,others,operater_no,group_no,day_night,sale_time,succeed_printed,ticket_state,statistics_flag,relay_ticket_type,ticket_type,train_no,board_train_code,train_date,start_time,train_class_code,seat_feature,coach_no,seat_no,seat_type_code,bed_level,from_station_name,from_tele_code,from_bureau_code,from_subbureau_code,relay_station_name,relay_tele_code,to_station_name,to_tele_code,to_segment_code,to_bureau_code,to_subbureau_code,route_telecode,distance,ticket_price,belong_line1,distance1,ticket_price1,belong_line2,distance2,ticket_price2,belong_line3,distance3,ticket_price3,belong_line4,distance4,ticket_price4,belong_line5,distance5,ticket_price5,relay_distance,relay_price,addition_cost,bed_reserve_cost,service_cost,air_condition_price,id_number,reserve_unit,ticket_source,source_code,train_air_condition,agent_fee,discount,check_sum,corporation_code,original_ticket_type,first_station_name,first_tele_code,first_bureau_code,first_subbureau_code,last_station_name,last_tele_code,last_bureau_code,last_subbureau_code,total_distance,total_base_price,one_date,one_distance,one_train_code,transmit_flag,statist_date,flag1,flag2,basic_price,sale_mode,sequence_no,trade_no,ticket_price_obtain,preference_style,preference_rate,preference_price,inner_code,first_seat_type,team_id,purpose_code,location_code,area_center_code".toUpperCase());
		//fileAttribute f = new fileAttribute("G:\\CombHkTl\\dy_anti_hk.csv",",","dep_date,id_jm,cabin,age&sex,dep,arr,dis");
		//f.createJson();
		/* 
		File rootp = new File("G:\\Train_Data-origin\\tl2015-2017\\deleteSymbol\\fileAdd\\md5\\cutFile");
		String[] rootf = rootp.list();
		for(int i=0;i<rootf.length;i++) {
			File tf = new File(rootp.getAbsolutePath()+"\\"+rootf[i]);
			if(tf.isFile()) {
				countFile(tf.getAbsolutePath());
				//System.out.println(tf.getAbsolutePath());
				//getFileByField(tf.getAbsolutePath(),rootp.getAbsolutePath()+"\\temp\\"+rootf[i]);
			}
		}
		*/
		String names = "李爱彩,张炬,何梦琪,黄晨";//杨添靖,王国飞,赵一览,张泽慧,宋浩男,蒋莹,吴明正,张梦捷
		Map<Long,String> random = new HashMap<Long,String>();
		for(int i=0;i<names.split(",").length;i++) {
			long k=Math.round(Math.random()*7);
			if(random.containsKey(k)) {
				i--;
				continue;
			}else {
				random.put(k, names.split(",")[i]);
			}
		}
		for(long i:random.keySet()) {
			System.out.println(random.get(i));
		}
		
		/*
		File rootp = new File("G:\\Train_Data-origin\\tl2015-2017\\deleteSymbol\\fileAdd\\md5\\cutFile");
		String[] loc = {"ctu","mig","dy"};
		String[] rootf = rootp.list();
		for(String l:loc) {
			int cnt = 0;
			String[] flist = new String[10];
			for(int i=0;i<flist.length;i++) {
				flist[i] = "unknown";
			}
			for(int i=0;i<rootf.length;i++) {
				File tf = new File(rootp.getAbsolutePath()+"\\"+rootf[i]);
				if(tf.isFile()&&rootf[i].contains(l)&&(!rootf[i].contains("2014"))&&(!rootf[i].contains("2019"))) {
					flist[cnt] = tf.getAbsolutePath();
					cnt++;
				}
			}
			countDistinct(flist, 6);
		}
		*/
	}

}
