package CrossData;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import CrossData.fileAttribute;

public class getCombId {
	public static String insertIntoRecord(String str,String insertStr,int attr,String sep) {
		String rs="";
		String[] temp = str.split("\r\n");
		for(int i=0;i<temp.length;i++) {
			String[] temp2 = temp[i].split(sep);
			temp[i]=temp[i].replaceAll(sep+temp2[attr]+sep, sep+temp2[attr]+sep+insertStr+sep);
		}
		for(int i=0;i<temp.length-1;i++) {
			rs+=temp[i]+"\r\n";
		}
		rs+=temp[temp.length-1];
		return rs;
	}
	public static void combFileByAttr(fileAttribute[] files,int[] attr,String[] outfile) {
		File f1 = new File(files[0].getFilePath());
		File f2 = new File(files[1].getFilePath());
		try {
			BufferedReader reader1 = new BufferedReader(new InputStreamReader(new FileInputStream(f1),"UTF-8"));
			BufferedReader reader2 = new BufferedReader(new InputStreamReader(new FileInputStream(f2),"UTF-8"));
			Map<String,String> combs = new HashMap<String,String>();
			File[] outf = new File[5];
			BufferedWriter[] writer = new BufferedWriter[5];
			for(int i=0;i<5;i++) {
				outf[i] = new File(outfile[i]);
				writer[i] = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outf[i]),"UTF-8"));
			}
			//f1 reader
			String str="";
			int cnt=0;
			//read two file
			System.out.println("read file: "+files[0].getFilePath());
			while((str=reader1.readLine())!=null) {
				String[] temp=str.split(files[0].getFileSeparator());
				if(combs.containsKey(temp[attr[0]])) {
					combs.put(temp[attr[0]], combs.get(temp[attr[0]])+"\r\n"+str);
				}else {
					combs.put(temp[attr[0]], str);
				}
			}
			for(String key: combs.keySet()) {
				combs.put(key,combs.get(key)+"@1");
			}
			System.out.println("read file: "+files[1].getFilePath());
			while((str=reader2.readLine())!=null) {
				cnt++;
				if(cnt%1000000==0) {
					System.out.println(cnt);
				}
				String[] temp=str.split(files[1].getFileSeparator());
				if(combs.containsKey(temp[attr[1]])) {
					combs.put(temp[attr[1]], combs.get(temp[attr[1]])+"\r\n"+str);
					//System.out.println("yes");
				}else {
					writer[3].write(str+"\r\n");
				}
			}
			reader1.close();
			reader2.close();
			System.out.println("begin to comb");
			
			Map<String,String> combset1 = new HashMap<String,String>();
			Map<String,String> combset2 = new HashMap<String,String>();
			Map<String,String> antiset1 = new HashMap<String,String>();
			
			for(String key : combs.keySet()) {
				if(combs.get(key).contains("@1")) {//if no @1 means not in comb set
					String[] temp = combs.get(key).split("@");
					if(temp[1].equals("1")) {//if splited by @, the second one is 1 means not in comb set
						antiset1.put(key,temp[0]);
						//combs.remove(key); can not read key and remove key at same time
					}else {
						//System.out.println(combs.get(key).split("@1")[1]);
						combset1.put(key,
								insertIntoRecord(
										combs.get(key).split("@1")[0],
										combs.get(key).split("@1")[1].split(files[1].getFileSeparator())[attr[1]+1],
										attr[0],
										files[0].getFileSeparator()
										)
								);
						combset2.put(key,combs.get(key).split("@1")[1]);
						//combs.remove(key);
					}
				}else {
					writer[3].write(combs.get(key)+"\r\n");
					//combs.remove(key);
				}
			}
			combs.clear();
			
			for(String key : combset1.keySet()) {
				writer[0].write(combset1.get(key)+"\r\n");
			}
			for(String key : combset2.keySet()) {
				writer[1].write(combset2.get(key)+"\r\n");
			}
			for(String key : antiset1.keySet()) {
				writer[2].write(antiset1.get(key)+"\r\n");
			}
			for(String key : combset2.keySet()) {
				
				writer[4].write(key+","+
							combset2.get(key).split(files[1].getFileSeparator())[attr[1]+1]+"\r\n");
			}
			combset1.clear();
			combset2.clear();
			antiset1.clear();
			
			for(int i=0;i<5;i++) {
				writer[i].close();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void combFileByAttrForSpace(fileAttribute[] files,int[] attr,String[] outfile) {
		File f1 = new File(files[0].getFilePath());
		File f2 = new File(files[1].getFilePath());
		try {
			BufferedReader reader1 = new BufferedReader(new InputStreamReader(new FileInputStream(f1),"UTF-8"));
			BufferedReader reader2 = new BufferedReader(new InputStreamReader(new FileInputStream(f2),"UTF-8"));
			Map<String,String> combs = new HashMap<String,String>();
			Map<String,String> combs2 = new HashMap<String,String>();
			File[] outf = new File[5];
			BufferedWriter[] writer = new BufferedWriter[5];
			for(int i=0;i<5;i++) {
				outf[i] = new File(outfile[i]);
				writer[i] = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outf[i]),"UTF-8"));
			}
			Map<String,String> combset1 = new HashMap<String,String>();
			Map<String,String> combset2 = new HashMap<String,String>();
			//f1 reader
			String str="";
			int cnt=0;
			//read two file
			System.out.println("read file: "+files[0].getFilePath());
			while((str=reader1.readLine())!=null) {
				String[] temp=str.split(files[0].getFileSeparator());
				if(combs.containsKey(temp[attr[0]])) {
					combs.put(temp[attr[0]], combs.get(temp[attr[0]])+"\r\n"+str);
				}else {
					combs.put(temp[attr[0]], str);
				}
			}
			//get backup for file1(file1.size<file2.size)
			for(String key: combs.keySet()) {
				combs.put(key,combs.get(key)+"@1");
				combs2.put(key,combs.get(key));
			}
			System.out.println("read file: "+files[1].getFilePath());
			while((str=reader2.readLine())!=null) {
				cnt++;
				if(cnt%1000000==0) {
					System.out.println(cnt);
				}
				String[] temp=str.split(files[1].getFileSeparator());
				//use mirror 
				if(combs2.containsKey(temp[attr[1]])) {
					combs2.put(temp[attr[1]], combs2.get(temp[attr[1]])+str+"\r\n");
				}else {
					writer[3].write(str+"\r\n");
				}
				if(cnt%20000000==0) {
					for(String key : combs2.keySet()) {
						if(combs2.get(key).contains("@1")) {//if no @1 means not in comb set
							String[] temps = combs2.get(key).split("@");
							if(!temps[1].equals("1")) {//means have two side
								//set1 for files1
								combset1.put(key,
										insertIntoRecord(
												combs2.get(key).split("@1")[0],
												combs2.get(key).split("@1")[1].split(files[1].getFileSeparator())[attr[1]+1],
												attr[0],
												files[0].getFileSeparator()
												)
										);
								combset2.put(key,combs2.get(key).split("@1")[1]);
								//combs.remove(key);
							}
						}else {
							writer[3].write(combs2.get(key)+"\r\n");
							//write anticomb part
						}
					}
					combs2.clear();
					for(String key: combs.keySet()) {
						combs2.put(key,combs.get(key));
					}
				}
			}
			reader1.close();
			reader2.close();
			System.out.println("begin to comb");
			
			for(String key : combs2.keySet()) {
				if(combs2.get(key).contains("@1")) {//if no @1 means not in comb set
					String[] temps = combs2.get(key).split("@");
					if(!temps[1].equals("1")) {//means have two side
						//set1 for files1
						combset1.put(key,
								insertIntoRecord(
										combs2.get(key).split("@1")[0],
										combs2.get(key).split("@1")[1].split(files[1].getFileSeparator())[attr[1]+1],
										attr[0],
										files[0].getFileSeparator()
										)
								);
						combset2.put(key,combs2.get(key).split("@1")[1]);
						//combs.remove(key);
					}
				}else {
					writer[3].write(combs2.get(key)+"\r\n");
					//write anticomb part
				}
			}
			combs2.clear();
			
			for(String key : combset1.keySet()) {
				writer[0].write(combset1.get(key)+"\r\n");
			}
			for(String key : combset2.keySet()) {
				writer[1].write(combset2.get(key));
			}
			for(String key : combset2.keySet()) {//id
				writer[4].write(key+","+
							combset2.get(key).split(files[1].getFileSeparator())[attr[1]+1]+"\r\n");
			}
			combset2.clear();
			for(String key: combs.keySet()) {
				if(!combset1.containsKey(key)) {
					writer[2].write(combs.get(key).replaceAll("@1", "")+"\r\n");
				}
			}
			combset1.clear();
			combs.clear();
			for(int i=0;i<5;i++) {
				writer[i].close();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void combFileByAttrForSpaceNoID(fileAttribute[] files,int[] attr,String[] outfile) {//不存入ID入航空数据
		File f1 = new File(files[0].getFilePath());//hk
		File f2 = new File(files[1].getFilePath());//tl
		try {
			BufferedReader reader1 = new BufferedReader(new InputStreamReader(new FileInputStream(f1),"UTF-8"));//hk reader
			BufferedReader reader2 = new BufferedReader(new InputStreamReader(new FileInputStream(f2),"UTF-8"));//tl reader
			Map<String,String> combs = new HashMap<String,String>();
			Map<String,String> combs2 = new HashMap<String,String>();
			File[] outf = new File[5];
			BufferedWriter[] writer = new BufferedWriter[5];
			for(int i=0;i<5;i++) {
				outf[i] = new File(outfile[i]);
				writer[i] = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outf[i]),"UTF-8"));
			}
			Map<String,String> combset1 = new HashMap<String,String>();
			Map<String,String> combset2 = new HashMap<String,String>();
			//f1 reader
			String str="";
			int cnt=0;
			//read two file
			System.out.println("read file: "+files[0].getFilePath());
			while((str=reader1.readLine())!=null) {//hk data input into combs
				String[] temp=str.split(files[0].getFileSeparator());
				if(combs.containsKey(temp[attr[0]])) {
					combs.put(temp[attr[0]], combs.get(temp[attr[0]])+"\r\n"+str);
				}else {
					combs.put(temp[attr[0]], str);
				}
			}
			//get backup for file1(file1.size<file2.size)
			for(String key: combs.keySet()) {//读入航空数据后combs作为备份
				combs.put(key,combs.get(key)+"@1");//combs加入@1作为分割符
				combs2.put(key,combs.get(key));//combs拷贝航空数据
			}
			System.out.println("read file: "+files[1].getFilePath());
			while((str=reader2.readLine())!=null) {//开始读入铁路数据
				cnt++;
				if(cnt%1000000==0) {//一百万条输出进度
					System.out.println(cnt);
				}
				String[] temp=str.split(files[1].getFileSeparator());
				//use mirror 
				if(combs2.containsKey(temp[attr[1]])) {//如果航空、铁路数据id匹配
					combs2.put(temp[attr[1]], combs2.get(temp[attr[1]])+str+"\r\n");
				}else {//不匹配说明属于铁路独有数据
					writer[3].write(str+"\r\n");
				}
				if(cnt%10000000==0) {//一千万条数据一次输出
					for(String key : combs2.keySet()) {//在combs2里
						if(combs2.get(key).contains("@1")) {//是否存在分隔符@1
							String[] temps = combs2.get(key).split("@");//存在则通过@分割字符串
							if(!temps[1].equals("1")) {//如果在分割的右半边为1说明没有与铁路匹配的航空数据
								//set1 for files1
								combset1.put(key,combs2.get(key).split("@1")[0]);//不为1则是匹配成功
								combset2.put(key,combs2.get(key).split("@1")[1]);//航空存入combset1，铁路存入combset2
								//combs.remove(key);
							}
						}else {
							writer[3].write(combs2.get(key)+"\r\n");
							//write anticomb part
						}
					}
					combs2.clear();//清空combs2释放内存
					for(String key: combs.keySet()) {//将备份数据导入
						combs2.put(key,combs.get(key));
					}
					//输出comb tl 释放内存
					for(String key : combset2.keySet()) {
						writer[1].write(combset2.get(key));
					}
					combset2.clear();
				}
			}
			reader1.close();
			reader2.close();
			System.out.println("begin to comb");
			
			for(String key : combs2.keySet()) {//剩余的combs2中数据释放
				if(combs2.get(key).contains("@1")) {//if no @1 means not in comb set
					String[] temps = combs2.get(key).split("@");
					if(!temps[1].equals("1")) {//means have two side
						//set1 for files1
						combset1.put(key,combs2.get(key).split("@1")[0]);
						combset2.put(key,combs2.get(key).split("@1")[1]);
						//combs.remove(key);
					}
				}else {
					writer[3].write(combs2.get(key)+"\r\n");
					//write anticomb part
				}
			}
			combs2.clear();
			
			//输出
			for(String key : combset1.keySet()) {
				writer[0].write(combset1.get(key)+"\r\n");
			}
			for(String key : combset2.keySet()) {
				writer[1].write(combset2.get(key));
			}
			for(String key : combset2.keySet()) {//id
				writer[4].write(key+","+
							combset2.get(key).split(files[1].getFileSeparator())[attr[1]+1]+"\r\n");
			}
			combset2.clear();
			//航空 anti输出
			for(String key: combs.keySet()) {
				if(!combset1.containsKey(key)) {
					writer[2].write(combs.get(key).replaceAll("@1", "")+"\r\n");
				}
			}
			combset1.clear();
			combs.clear();
			for(int i=0;i<5;i++) {
				writer[i].close();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void combFileByAttrComb(fileAttribute[] files,int[][] attr,String modes,String[] outfile) {
		File f1 = new File(files[0].getFilePath());
		File f2 = new File(files[1].getFilePath());
		try {
			BufferedReader reader1 = new BufferedReader(new InputStreamReader(new FileInputStream(f1),"UTF-8"));
			BufferedReader reader2 = new BufferedReader(new InputStreamReader(new FileInputStream(f2),"UTF-8"));
			Map<String,String> combs = new HashMap<String,String>();
			//f1 reader
			String str="";
			//read two file
			System.out.println("read file: "+files[0].getFilePath());
			while((str=reader1.readLine())!=null) {
				String[] temp=str.split(files[0].getFileSeparator());
				String tempkey="";
				for(int k=0;k<attr[0].length;k++) {
					tempkey+=temp[attr[0][k]]+",";
				}
				if(combs.containsKey(tempkey)) {
					combs.put(tempkey, combs.get(tempkey)+"\r\n"+str);
				}else {
					combs.put(tempkey, str);
				}
			}
			for(String key: combs.keySet()) {
				combs.put(key,combs.get(key)+"@1");
			}
			System.out.println("read file: "+files[1].getFilePath());
			while((str=reader2.readLine())!=null) {
				String[] temp=str.split(files[1].getFileSeparator());
				String tempkey="";
				for(int k=0;k<attr[1].length;k++) {
					tempkey+=temp[attr[1][k]]+",";
				}
				if(combs.containsKey(tempkey)) {
					combs.put(tempkey, combs.get(tempkey)+"\r\n"+str);
				}else {
					//combs.put(tempkey, str);
				}
			}
			reader1.close();
			reader2.close();
			System.out.println("begin to comb");
			
			Map<String,String> combset1 = new HashMap<String,String>();
			Map<String,String> combset2 = new HashMap<String,String>();
			Map<String,String> antiset1 = new HashMap<String,String>();
			Map<String,String> antiset2 = new HashMap<String,String>();
			for(String key : combs.keySet()) {
				if(combs.get(key).contains("@1")) {
					String[] temp = combs.get(key).split("@");
					if(!temp[1].equals("1")) {
						combset1.put(key,combs.get(key));
						//combs.remove(key);
					}
				}
			}
			combs.clear();
			File[] outf = new File[1];
			BufferedWriter[] writer = new BufferedWriter[1];
			for(int i=0;i<1;i++) {
				outf[i] = new File(outfile[i]);
				writer[i] = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outf[i]),"UTF-8"));
			}
			for(String key : combset1.keySet()) {
				String temps = "";
				String[] temps2 = combset1.get(key).split("@1")[0].split(files[0].getFileSeparator());
				for(int k=0;k<temps2.length;k++) {
					boolean b = true;
					for(int j=0;j<attr[0].length;j++) {
						if(k==attr[0][j]) {
							b=false;
						}
					}
					if(b) {
						temps+=temps2[k]+",";
					}
				}
				temps2 = combset1.get(key).split("@1")[1].split(files[1].getFileSeparator());
				for(int k=0;k<temps2.length;k++) {
					boolean b = true;
					for(int j=0;j<attr[1].length;j++) {
						if(k==attr[1][j]) {
							b=false;
						}
					}
					if(b) {
						temps+=temps2[k];
						if(k!=temps2.length-1) {
							temps+=",";
						}
					}
				}
				writer[0].write(key+temps+"\r\n");
			}
			combset1.clear();
			
			
			for(int i=0;i<1;i++) {
				writer[i].close();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static String getnum(int i) {
    	String str="";
    	if(i<10) {
    		str="20140"+i;
    	}else if(i<13) {
    		str="2014"+i;
    	}else if(i<16) {
    		str="20150"+(i-12);
    	}
    	return str;
    }
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		/*
		fileAttribute[] files = new fileAttribute[2];
		int[][] attr = new int[2][4];
		attr[0][0] = 0;
		attr[0][1] = 2;
		attr[0][2] = 3;
		attr[0][3] = 4;
		attr[1][0] = 94;
		attr[1][1] = 0;
		attr[1][2] = 1;
		attr[1][3] = 6;
		String modes="";
		String[] outfile = new String[1];
		outfile[0]="G:\\Train_Data-origin\\";
		for(int i=5;i<16;i++) {
			try {
				outfile[0]="G:\\Train_Data-origin\\id_sale_"+getnum(i)+".csv";
				String f1="G:\\tl2014-2015\\id_no\\id_no_"+getnum(i)+".csv";
				String f2="G:\\Train_Data-origin\\tl2014-2015\\sale_record_"+getnum(i)+".csv";
				files[0] = new fileAttribute(f1,"UTF-8",";","statistics_date;inner_code;office_no;window_no;ticket_no;id_kind;id_no;id_name;transmit_flag;sex;nation;birth;address;subscribe;valid_start_date;valid_stop_date;statist_date;others");
				files[1] = new fileAttribute(f2,"UTF-8",";","office_no;window_no;operater_no;group_no;day_night;sale_time;ticket_no;succeed_printed;ticket_state;statistics_flag;relay_ticket_type;ticket_type;train_no;board_train_code;train_date;start_time;train_class_code;seat_feature;coach_no;seat_no;seat_type_code;bed_level;from_station_name;from_tele_code;from_bureau_code;from_subbureau_code;relay_station_name;relay_tele_code;to_station_name;to_tele_code;to_segment_code;to_bureau_code;to_subbureau_code;route_telecode;distance;ticket_price;belong_line1;distance1;ticket_price1;belong_line2;distance2;ticket_price2;belong_line3;distance3;ticket_price3;belong_line4;distance4;ticket_price4;belong_line5;distance5;ticket_price5;relay_distance;relay_price;addition_cost;bed_reserve_cost;service_cost;air_condition_price;id_number;reserve_unit;ticket_source;source_code;train_air_condition;agent_fee;discount;check_sum;corporation_code;original_ticket_type;first_station_name;first_tele_code;first_bureau_code;first_subbureau_code;last_station_name;last_tele_code;last_bureau_code;last_subbureau_code;total_distance;total_base_price;one_date;one_distance;one_train_code;transmit_flag;statist_date;flag1;flag2;basic_price;sale_mode;sequence_no;trade_no;ticket_price_obtain;preference_style;preference_rate;preference_price;inner_code;first_seat_type;statistics_date;team_id;purpose_code;location_code;area_center_code");
				//System.out.println(files[0].getFilePath());
				combFileByAttrComb(files,attr,modes,outfile);
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}
		*/
		String[] citycode = {"dy","mig","ctu"};
		File hkpath = new File("G:\\hk2014-2018");
		File tlpath = new File("G:\\Train_Data-origin\\tl2015-2017\\deleteSymbol\\fileAdd\\md5");
		String rspath = "G:\\CombHkTl";
		String[] outfile=new String[5];
		int[] attr =new int[2];
		attr[0]=1;
		attr[1]=5;
		fileAttribute[] files = new fileAttribute[2];
		for(int i=0;i<citycode.length;i++) {
			String[] hklist = hkpath.list();
			String[] tllist = tlpath.list();
			outfile[0] = rspath+"\\"+citycode[i]+"_comb_hk.csv";
			outfile[1] = rspath+"\\"+citycode[i]+"_comb_tl.csv";
			outfile[2] = rspath+"\\"+citycode[i]+"_anti_hk.csv";
			outfile[3] = rspath+"\\"+citycode[i]+"_anti_tl.csv";
			outfile[4] = rspath+"\\"+citycode[i]+"_comb_id.csv";
			//System.out.println(citycode[i]);
			for(int k=0;k<hklist.length;k++) {
				if(hklist[k].contains(citycode[i])) {
					files[0] = new fileAttribute(hkpath.getAbsolutePath()+"\\"+hklist[k],",");
				}
			}
			for(int k=0;k<tllist.length;k++) {
				if(tllist[k].contains(citycode[i])) {
					files[1] = new fileAttribute(tlpath.getAbsolutePath()+"\\"+tllist[k],",");
				}
			}
			System.out.println("comb:"+files[0].getFilePath()+"\r\n"+files[1].getFilePath());
			combFileByAttrForSpaceNoID(files,attr,outfile);
		}
	}

}
