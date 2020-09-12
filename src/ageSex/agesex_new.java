package ageSex;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

public class agesex_new {
	public static int[] getAgeSexFromId(String dates,String id) {
    	int[] ageSex=new int[2];//第一个值存储性别，第二个值存储年龄
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
    	if(Integer.parseInt(x)>2020||Integer.parseInt(x)<1800||
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
    	if((ageSex[0]!=1&&ageSex[0]!=0)||ageSex[1]<0||ageSex[1]>=120) {
    		ageSex[0]=0;
    		ageSex[1]=0;
    	}
    	return ageSex;
    }
	
	public static Map<String,Map<String,Integer>> GenderAge(Map<String,Map<String,Integer>> Month,String filepath, int attr, int dateAttr) {
		//Map<String,Map<String,Integer>> Month = new HashMap<String,Map<String,Integer>>();
		try {
			File f = new File(filepath);
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF-8"));
			String str = "";
			while((str = br.readLine())!=null) {
				String[] temp = str.split(",");
				if(temp.length<=attr+1) {
					continue;
				}
				String id = temp[attr];
				String dates = temp[dateAttr];
				dates.replaceAll("-", "");
				if(id.length()!=18||dates.length()!=8) {
					continue;
				}
				int[] agesex = getAgeSexFromId(dates,id);
				String GenderAge = agesex[0] + "," +agesex[1];
				String m = dates.substring(0,6);
				if(Month.containsKey(m)) {
					Month.get(m).put(GenderAge,Month.get(m).get(GenderAge)+1);
				}else {
					Map<String, Integer> t = new HashMap<String, Integer>();
					for(int i=0;i<=120;i++) {
						t.put(0+","+i,0);
						t.put(1+","+i,0);
					}
					Month.put(m,t);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return Month;
	}
	
	public static void PrintListWithkey(String outf, Map<String,String> ls) {
		try {
			File f = new File(outf);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f),"UTF-8"));
			
			for(String key:ls.keySet()) {
				bw.write(key+","+ls.get(key)+"\r\n");
			}
			bw.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void PrintList(String outf,Map<String,Map<String,Integer>> Month) {
		try {
			File f = new File(outf);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f),"UTF-8"));
			String[] Monthlist = new String[Month.size()];//月份排序
			int cnt = 0;
			for(String k:Month.keySet()) {
				Monthlist[cnt] = k;
				cnt++;
			}
			for(int i=0;i<Month.size();i++) {
				for(int j=0;j<Month.size()-i-1;j++) {
					if(Monthlist[j].compareTo(Monthlist[j+1])>0) {
						String t = Monthlist[j];
						Monthlist[j] = Monthlist[j+1];
						Monthlist[j+1] = t;
					}
				}
			}
			for(int k=0;k<Month.size();k++) {//按月份去除性别年龄列表
				Map<String,Integer> agesex = Month.get(Monthlist[k]);
				String[] aslist = new String[agesex.size()];
				cnt=0;
				for(String key:agesex.keySet()) {
					aslist[cnt] = key;
					cnt++;
				}
				for(int i=0;i<agesex.size();i++) {//性别年龄排序
					for(int j=0;j<agesex.size()-i-1;j++) {
						if(Integer.parseInt(aslist[j].split(",")[0])>Integer.parseInt(aslist[j+1].split(",")[0])) {
							String t = aslist[j];
							aslist[j] = aslist[j+1];
							aslist[j+1] = t;
						}else if(Integer.parseInt(aslist[j].split(",")[0])==Integer.parseInt(aslist[j+1].split(",")[0])&&
								Integer.parseInt(aslist[j].split(",")[1])>Integer.parseInt(aslist[j+1].split(",")[1])) {
							String t = aslist[j];
							aslist[j] = aslist[j+1];
							aslist[j+1] = t;
						}
					}
				}
				for(int i=0;i<aslist.length;i++) {//输出
					bw.write(Monthlist[k]+","+aslist[i]+","+agesex.get(aslist[i])+"\r\n");
				}
			}
			bw.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static boolean isHighway(String type) {
		if(type.toUpperCase().contains("D")||type.toUpperCase().contains("G")||type.toUpperCase().contains("C")) {
			return true;
		}else {
			return false;
		}
	}
	
	public static String InfoOfPassenger(String str, String oldstr, int id, int dates, int type, int dist, int cost) {
		String rs = "";
		try {
			
			if(oldstr.equals("")) {
				oldstr = "-,0,0,"+"0,0,0,0,0,0,0,0,0,0,0,0"+",0,0,0,0,0,0,0,0";
				//location,age,sex,2015 frequency\distance\cost,2016 freq,2017 frequency,2018frequency,2015 highway\normal,2016,2017,2018
			}
			//age and sex according to 2018
			String[] temp = str.split(",");
			String[] temp2 = oldstr.split(",");
			if(temp[cost].equals("")||temp[dist].equals("")||temp[dates].equals("")||temp[type].equals("")) {
				return oldstr;
			}
			//location   age   sex
			if(temp[id].substring(0,4).equals("5101")) {
				rs += "ctu,";
			}else if(temp[id].substring(0,4).equals("5106")) {
				rs += "dy,";
			}else if(temp[id].substring(0,4).equals("5107")) {
				rs += "mig,";
			}else {
				return oldstr;
			}
			int[] agesex = getAgeSexFromId("20180509",temp[id]);
			rs += agesex[0]+","+agesex[1]+",";
			
			//frequency distance cost type
			String y = temp[dates].substring(0,4);
			if(y.equals("2015")) {
				rs+=(Integer.parseInt(temp2[3])+1) + "," +(Integer.parseInt(temp2[4])+Integer.parseInt(temp[dist])) +"," +
						(Integer.parseInt(temp2[5])+Integer.parseInt(temp[cost])) + ",";
				rs+=temp2[6]+","+temp2[7]+","+temp2[8]+","+temp2[9]+","+temp2[10]+","+temp2[11]+","+temp2[12]+","+temp2[13]+","+temp2[14]+",";
				if(isHighway(temp[type])) {
					rs+=(Integer.parseInt(temp2[15])+1)+",";
					rs+=temp2[16]+","+temp2[17]+","+temp2[18]+","+temp2[19]+","+temp2[20]+","+temp2[21]+","+temp2[22];
				}else {
					rs+=temp2[15]+",";
					rs+=(Integer.parseInt(temp2[16])+1)+",";
					rs+=temp2[17]+","+temp2[18]+","+temp2[19]+","+temp2[20]+","+temp2[21]+","+temp2[22];
				}
			}else if(y.equals("2016")){
				rs+=temp2[3]+","+temp2[4]+","+temp2[5]+",";
				rs+=(Integer.parseInt(temp2[6])+1) + "," +(Integer.parseInt(temp2[7])+Integer.parseInt(temp[dist])) +"," +
						(Integer.parseInt(temp2[8])+Integer.parseInt(temp[cost])) + ",";
				rs+=temp2[9]+","+temp2[10]+","+temp2[11]+","+temp2[12]+","+temp2[13]+","+temp2[14]+",";
				if(isHighway(temp[type])) {
					rs+=temp2[15]+","+temp2[16]+",";
					rs+=(Integer.parseInt(temp2[17])+1)+",";
					rs+=temp2[18]+","+temp2[19]+","+temp2[20]+","+temp2[21]+","+temp2[22];
				}else {
					rs+=temp2[15]+","+temp2[16]+","+temp2[17]+",";
					rs+=(Integer.parseInt(temp2[18])+1)+",";
					rs+=temp2[19]+","+temp2[20]+","+temp2[21]+","+temp2[22];
				}
			}else if(y.equals("2017")){
				rs+=temp2[3]+","+temp2[4]+","+temp2[5]+","+temp2[6]+","+temp2[7]+","+temp2[8]+",";
				rs+=(Integer.parseInt(temp2[9])+1) + "," +(Integer.parseInt(temp2[10])+Integer.parseInt(temp[dist])) +"," +
						(Integer.parseInt(temp2[11])+Integer.parseInt(temp[cost])) + ",";
				rs+=temp2[12]+","+temp2[13]+","+temp2[14]+",";
				if(isHighway(temp[type])) {
					rs+=temp2[15]+","+temp2[16]+","+temp2[17]+","+temp2[18]+",";
					rs+=(Integer.parseInt(temp2[19])+1)+",";
					rs+=temp2[20]+","+temp2[21]+","+temp2[22];
				}else {
					rs+=temp2[15]+","+temp2[16]+","+temp2[17]+","+temp2[18]+","+temp2[19]+",";
					rs+=(Integer.parseInt(temp2[20])+1)+",";
					rs+=temp2[21]+","+temp2[22];
				}
			}else if(y.equals("2018")){
				rs+=temp2[3]+","+temp2[4]+","+temp2[5]+","+temp2[6]+","+temp2[7]+","+temp2[8]+","+temp2[9]+","+temp2[10]+","+temp2[11]+",";
				rs+=(Integer.parseInt(temp2[12])+1) + "," +(Integer.parseInt(temp2[13])+Integer.parseInt(temp[dist])) +"," +
						(Integer.parseInt(temp2[14])+Integer.parseInt(temp[cost])) + ",";
				if(isHighway(temp[type])) {
					rs+=temp2[15]+","+temp2[16]+","+temp2[17]+","+temp2[18]+","+temp2[19]+","+temp2[20]+",";
					rs+=(Integer.parseInt(temp2[21])+1)+",";
					rs+=temp2[22];
				}else {
					rs+=temp2[15]+","+temp2[16]+","+temp2[17]+","+temp2[18]+","+temp2[19]+","+temp2[20]+","+temp2[21]+",";
					rs+=(Integer.parseInt(temp2[22])+1);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(str);
			System.out.println(oldstr);
			System.out.println(rs);
		}
		
		//System.out.println(rs);
		return rs;
	}
	public static Map<String,String> GetPeopleRecord(Map<String,String>rs,String[] fs,int id,int dates,int type,int dist,int cost) {
		//Map<String,String> rs = new HashMap<String,String>();//store the info of passengers
		
		try{
			for(int i=0;i<fs.length;i++) {
				System.out.println(" "+fs[i]);
				File f = new File(fs[i]);
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF-8"));
				String str = "";
				while((str = br.readLine())!=null) {
					String[] temp = str.split(",");
					if(temp.length<27) {
						continue;
					}
					if(temp[id].length()<18) {
						continue;
					}
					if(Integer.parseInt(temp[dist])<=100) {
						continue;
					}//threshold
					if(rs.containsKey(temp[id])) {
						rs.put(temp[id],InfoOfPassenger(str, rs.get(temp[id]),id,dates,type,dist,cost));
					}else {
						rs.put(temp[id],InfoOfPassenger(str, "",id,dates,type,dist,cost));
					}
				}
				br.close();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return rs;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//numsta
		/*
		Map<String,Map<String,Integer>> Month = new HashMap<String,Map<String,Integer>>();
		File rootp = new File("G:\\Train_Data-origin\\tl2015-2017\\deleteSymbol\\fileAdd\\md5\\cutFile");
		String[] loc = {"dy","ctu","mig"};
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
			String outf = rootp+"\\agesex\\" + l + "_agesex.csv";
			File folder = new File(rootp+"\\agesex");
			if(!folder.exists()) {
				folder.mkdir();
			}
			System.out.println(outf);
			for(int i=0;i<cnt;i++) {
				System.out.println(" "+flist[i]);
				Month = GenderAge(Month,flist[i],6,16);
			}
			PrintList(outf,Month);
		}
		*/
		
		//peoplesta
		
		File rootp = new File("G:\\Train_Data-origin\\tl2015-2017\\deleteSymbol\\fileAdd\\md5\\cutFile");
		String[] loc = {"dy","mig","ctu"};//
		String[] rootf = rootp.list();
		for(String l:loc) {
			Map<String,String> Record = new HashMap<String,String>();
			int cnt = 0;
			String[] flist = new String[4];
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
			String outf = rootp+"\\recordInfo_threshold\\" + l + "_recordInfo.csv";
			File folder = new File(rootp+"\\recordInfo_threshold");
			if(!folder.exists()) {
				folder.mkdir();
			}
			System.out.println(outf);
			
			Record = GetPeopleRecord(Record,flist,6,16,15,26,27);
			//
			PrintListWithkey(outf, Record);
			//print lists when the 
		}
	}

}
