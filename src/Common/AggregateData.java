package Common;

import java.io.*;
import java.util.Map;
import java.util.HashMap;


public class AggregateData {
	
	public static Map<String,String> getAggreData(Map<String,String> rawData, String rawFile) {
		//Map<String,String> rs = new HashMap<String,String>();
		System.out.println("aggregate: "+rawFile);
		try {
			File rawf = new File(rawFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(rawf),"UTF-8"));
			String str = "";
			
			while((str = br.readLine())!=null) {
				String[] temp = str.split(",");
				String tkey = temp[1]+","+temp[2]+","+temp[3];//location,sex,age
				String[] tk = new String[4];
				for(int i=0;i<4;i++) {
					tk[i] = tkey+","+(2015+i);//year
					if(rawData.containsKey(tk[i])) {
						String[] value = rawData.get(tk[i]).split(",");
						String v = String.valueOf(Integer.parseInt(value[0])+Integer.parseInt(temp[4+i*3]))+","+
								String.valueOf(Integer.parseInt(value[1])+Integer.parseInt(temp[5+i*3]))+","+
								String.valueOf(Integer.parseInt(value[2])+Integer.parseInt(temp[6+i*3]))+","+
								String.valueOf(Integer.parseInt(value[3])+Integer.parseInt(temp[16+i*2]))+","+
								String.valueOf(Integer.parseInt(value[4])+Integer.parseInt(temp[17+i*2]))+","+
								String.valueOf(Integer.parseInt(value[5])+Integer.parseInt(temp[24+i]))+","+
								String.valueOf(Integer.parseInt(value[6])+Integer.parseInt(temp[28+i]))+","+
								String.valueOf(Integer.parseInt(value[7])+1);
						rawData.put(tk[i], v);
								
					}else {
						rawData.put(tk[i], temp[4+i*3]+","+temp[5+i*3]+","+temp[6+i*3]+","+temp[16+i*2]+","+temp[17+i*2]+","
					+temp[24+i]+","+temp[28+i]+",1");
						//freq  distance price highway normal degree weightdegree
					}
				}
			}
			br.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return rawData;
	}
	
	public static Map<String,String> standard(Map<String,String> data) {
		System.out.println("standard data");
		String[][] stan = new String[data.size()][data.get("mig,1,41,2016").split(",").length+1];
		int cnt = 0;
		for(String key:data.keySet()) {
			String[] temp = data.get(key).split(",");
			stan[cnt][0] = key;
			for(int i=1;i<temp.length;i++) {
				stan[cnt][i] = String.valueOf(Float.valueOf(temp[i-1])/Float.valueOf(temp[temp.length-1]));//除以人数
			}
			stan[cnt][temp.length] = temp[temp.length-1];
			cnt++;
		}
		float[] sumdata = new float[data.get("mig,1,41,2016").split(",").length];
		for(int i=0;i<sumdata.length;i++) {
			sumdata[i] = 0;
		}
		for(int i=0;i<data.size();i++) {
			for(int j=1;j<data.get("mig,1,41,2016").split(",").length+1;j++) {
				sumdata[j-1] += Float.valueOf(stan[i][j]);
			}
		}
		for(int i=0;i<sumdata.length;i++) {
			sumdata[i] = sumdata[i]/data.size();
		}//计算均值
		float[] stanD = new float[data.get("mig,1,41,2016").split(",").length];
		for(int i=0;i<stanD.length;i++) {
			stanD[i] = 0;
		}
		for(int i=0;i<data.size();i++) {
			for(int j=1;j<data.get("mig,1,41,2016").split(",").length+1;j++) {
				stanD[j-1] += (Float.valueOf(stan[i][j])-sumdata[j-1])*(Float.valueOf(stan[i][j])-sumdata[j-1]);
			}
		}
		for(int i=0;i<data.get("mig,1,41,2016").split(",").length;i++) {
			stanD[i] = (float) Math.sqrt(stanD[i]/data.size());
		}//计算标准差
		
		for(int i=0;i<data.size();i++) {
			for(int j=1;j<data.get("mig,1,41,2016").split(",").length+1;j++) {
				stan[i][j] = String.valueOf((Float.valueOf(stan[i][j])-sumdata[j-1])/stanD[j-1]);
			}
		}
		
		for(int i=0;i<stan.length;i++) {
			String str = "";
			for(int j=1;j<stan[0].length-2;j++) {
				str+=stan[i][j]+",";
			}
			str+=stan[i][stan[0].length-2];//记录人数去除
			data.put(stan[i][0],str);
		}
		return data;
	}
	
	public static void printMap(Map<String,String> data,String outpath) {
		System.out.println("printMap: to-->"+outpath);
		try {
			File outf = new File(outpath);
			File outfolder = new File(outf.getParent());
			outfolder.mkdir();
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outf),"UTF-8"));
			for(String key:data.keySet()) {
				if(Integer.parseInt(key.split(",")[2])<18) {
					continue;
				}
				bw.write(key+","+data.get(key)+"\r\n");
			}
			bw.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void printMap(String title,Map<String,String> data,String outpath) {
		System.out.println("printMap: to-->"+outpath);
		try {
			File outf = new File(outpath);
			File outfolder = new File(outf.getParent());
			outfolder.mkdir();
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outf),"UTF-8"));
			bw.write(title+"\r\n");
			for(String key:data.keySet()) {
				if(Integer.parseInt(key.split(",")[2])<18) {
					continue;
				}
				bw.write(key+","+data.get(key)+"\r\n");
			}
			bw.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String rootpath = "C:\\Users\\user\\Desktop\\20200903\\aggredata";
		
		File rootf = new File(rootpath);
		String[] rootls = rootf.list();
		
		Map<String,String> data = new HashMap<String,String>();//store result
		
		for(int i=0;i<rootls.length;i++){
			if(!rootls[i].contains("csv")) {
				continue;
			}
			String rawFile = rootpath+"\\"+rootls[i];
			
			getAggreData(data,rawFile);
			
		}
		String titles = "Location,sex,age,year,Frequency,Distance,Cost,Highway,Normal,Degree,WeightDegree";
		printMap(titles,standard(data), "C:\\Users\\user\\Desktop\\20200903\\aggredata\\rs\\aggredata.csv");
		//standard data to avoid err
		//print data to file(csv)
		
	}

}
