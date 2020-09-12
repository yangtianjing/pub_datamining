package network;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import CrossData.combInFile;
import CrossData.fileAttribute;

public class getNetworkData {
	public static Map<String,Integer> getTLNetData(fileAttribute[] f, Map<String,Integer> nameList, String outname,int[] attr){
		File outFolder = new File(f[0].getFolder()+"\\getNetData");
		File fnls = new File(outFolder.getAbsolutePath()+"\\namelist_"+outname.split("_")[0]+".csv");
		if(!outFolder.exists()) {
			outFolder.mkdir();
		}
		File outfile = new File(outFolder.getAbsolutePath()+"\\"+outname+".csv");
		//String errstr = "";
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile),"UTF-8"));
			BufferedWriter fnlsw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fnls),"UTF-8"));
			for(int i=0;i<f.length;i++) {
				//errstr = f[i].getFilePath()+i+"\r\n";
				File infile = new File(f[i].getFilePath());
				//System.out.println("  get From:"+infile.getAbsolutePath());
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(infile),"UTF-8"));
				String str = "";
				while((str = reader.readLine())!=null) {
					String[] temp = str.split(",");
					if(temp.length<=7) {
						continue;
					}
					if(nameList.containsKey(temp[attr[0]])) {
						writer.write(temp[attr[1]]+","+temp[attr[2]]+","+nameList.get(temp[attr[0]])+","+temp[attr[3]]+","+temp[attr[4]]+","+temp[attr[5]]+"\r\n");
						fnlsw.write(temp[attr[0]]+","+nameList.get(temp[attr[0]])+"\r\n");
					}else {
						nameList.put(temp[attr[0]], nameList.get("cnt"));
						fnlsw.write(temp[attr[0]]+","+nameList.get("cnt")+"\r\n");
						writer.write(temp[attr[1]]+","+temp[attr[2]]+","+nameList.get("cnt")+","+temp[attr[3]]+","+temp[attr[4]]+","+temp[attr[5]]+"\r\n");
						nameList.put("cnt", nameList.get("cnt")+1);
					}
				}
				reader.close();
			}
			fnlsw.close();
			writer.close();
		}catch(Exception e) {
			//System.err.println(errstr);
			e.printStackTrace();
		}
		return nameList;
	}
	public static Map<String,Integer> getNetData(fileAttribute[] f, Map<String,Integer> nameList, String outname) {
		File outFolder = new File(f[0].getFolder()+"\\getNetData");
		if(!outFolder.exists()) {
			outFolder.mkdir();
		}
		File outfile = new File(outFolder.getAbsolutePath()+"\\"+outname+".csv");
		//String errstr = "";
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile),"UTF-8"));
			for(int i=0;i<f.length;i++) {
				//errstr = f[i].getFilePath()+i+"\r\n";
				File infile = new File(f[i].getFilePath());
				//System.out.println("  get From:"+infile.getAbsolutePath());
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(infile),"UTF-8"));
				String str = "";
				while((str = reader.readLine())!=null) {
					String[] temp = str.split(",");
					if(temp.length<=10) {
						continue;
					}
					if(nameList.containsKey(temp[2])) {
						writer.write(temp[0]+","+temp[1]+","+nameList.get(temp[2])+","+temp[10]+"\r\n");
					}else {
						nameList.put(temp[2], nameList.get("cnt"));
						writer.write(temp[0]+","+temp[1]+","+nameList.get("cnt")+","+temp[10]+"\r\n");
						nameList.put("cnt", nameList.get("cnt")+1);
					}
				}
				reader.close();
			}
			writer.close();
		}catch(Exception e) {
			//System.err.println(errstr);
			e.printStackTrace();
		}
		return nameList;
	}
	
	public static void constructNet(fileAttribute[] f) {
		try {
			for(int i=0;i<f.length;i++) {
				File infile = new File(f[i].getFilePath());
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(infile),"UTF-8"));
				File outFolder = new File(f[i].getFolder()+"\\Network");
				if(!outFolder.exists()) {
					outFolder.mkdir();
				}
				File outfile = new File(outFolder.getAbsolutePath()+"\\edge_"+f[i].getFileName());
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile),"UTF-8"));
				writer.write("Label,Source,Target,Weight\r\n");
				System.out.println("construct edgelist:"+f[i].getFilePath());
				String[][] teamList = new String[100000][3];
				int teamCnt = 0;
				String tag = "";
				String str = reader.readLine();
				String[] temp = str.split(",");
				tag = temp[0];
				teamList[teamCnt][0] = temp[2];
				teamList[teamCnt][1] = temp[3];
				teamList[teamCnt][2] = temp[1];
				teamCnt++;
				while((str = reader.readLine())!=null) {
					temp = str.split(",");
					if(tag.equals(temp[0])) {
						//System.out.println(tag);
						teamList[teamCnt][0] = temp[2];
						teamList[teamCnt][1] = temp[3];
						teamList[teamCnt][2] = temp[1];
						teamCnt++;
					}else {
						//output construct network
						
						for(int j=0;j<teamCnt;j++) {
							for(int k=j+1;k<teamCnt;k++) {
								writer.write(teamList[j][2]+","+teamList[j][0]+","+teamList[k][0]+","+teamList[k][1]+"\r\n");
								/*
								writer.write(teamList[j][0]+";"+teamList[k][0]+";"+teamList[j][1]+";<["+
							teamList[j][2].split("-")[0]+"-"+teamList[j][2].split("-")[1]+"]>\r\n");
							*/
							}
						}
						tag = temp[0];
						teamCnt = 0;
						teamList[teamCnt][0] = temp[2];
						teamList[teamCnt][1] = temp[3];
						teamList[teamCnt][2] = temp[1];
						teamCnt++;
					}
				}
				reader.close();
				writer.close();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}/*
		try {
			for(int i=0;i<f.length;i++) {
				File infile = new File(f[i].getFilePath());
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(infile),"UTF-8"));
				File outFolder = new File(f[i].getFolder()+"\\Network");
				if(!outFolder.exists()) {
					outFolder.mkdir();
				}
				File outfile = new File(outFolder.getAbsolutePath()+"\\nodes_"+f[i].getFileName());
				BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile),"UTF-8"));
				writer.write("Id;Label;Timestamp\r\n");
				Map<String,String> nodeList = new HashMap<String,String>();
				
				System.out.println("construct nodelist:"+f[i].getFilePath());
				String str = "";
				while((str = reader.readLine())!=null) {
					String[] temp = str.split(",");
					String times = temp[1].split("-")[0]+"."+temp[1].split("-")[1];
					if(nodeList.containsKey(temp[2])) {
						String[] timeStamp = nodeList.get(temp[2]).split(",");
						boolean sametag = true;
						for(int j=0;j<timeStamp.length;j++) {
							if(times.equals(timeStamp[j])) {
								sametag = false;
								break;
							}
						}
						if(sametag) {
							nodeList.put(temp[2], nodeList.get(temp[2])+times+",");
						}
					}else{
						nodeList.put(temp[2],times+",");
					}
				}
				reader.close();
				for(String key : nodeList.keySet()) {
					String[] temp = nodeList.get(key).split(",");
					writer.write(key+";"+key+";<[");
					for(int j=0;j<temp.length-1;j++) {
						writer.write(temp[j]+",");
					}
					writer.write(temp[temp.length-1]+"]>\r\n");
				}
				writer.close();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}*/
	}
	
	public static void getTLNet(String loc, String rootpath,String trange, int filey, int filem) {
		// get netdata from tl record
		File rootf = new File(rootpath);
		String[] tlrecord = rootf.list();
		for(int i=0;i<tlrecord.length;i++) {
			
			System.out.println("combInFile:"+rootpath+"\\"+tlrecord[i]);
			if(!tlrecord[i].contains(loc)||(!tlrecord[i].contains("csv"))) {
				continue;
			}
			fileAttribute f = new fileAttribute(rootpath+"\\"+tlrecord[i],",");
			int[] attr = {29,30};
			combInFile.combInFiles(f,attr);
		}
		rootpath +="\\Teaming";
		
		rootf = new File(rootpath);
		String[] rootlist = rootf.list();
		
		fileAttribute[][] f = new fileAttribute[4][12];
		
		try {
			for(int i=0;i<rootlist.length;i++) {
				String temppath = rootpath+"\\"+rootlist[i];
				File tempf = new File(temppath);
				if(!tempf.isFile()||(!rootlist[i].contains(loc))) {
					continue;
				}
				if(rootlist[i].contains(trange.split(",")[0])||rootlist[i].contains(trange.split(",")[1])) {
					continue;
				}
				int year = Integer.parseInt(rootlist[i].split("_")[filey].substring(0,4))-Integer.parseInt(trange.split(",")[0])-1;//@@@@
				int mon = Integer.parseInt(rootlist[i].split("_")[filey].substring(filem,filem+2))-1;
				f[year][mon] = new fileAttribute(temppath,",");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		Map<String,Integer> nameList = new HashMap<String,Integer>();
		nameList.put("cnt",0);
		for(int i=0;i<4;i++) {
			System.out.println("getNetworkData From:"+f[i][0].getFilePath());
			int[] attr = {3,0,1,7,4,8};
			nameList = getTLNetData(f[i], nameList, (Integer.parseInt(trange.split(",")[0])+i)+"_"+loc+"_"+loc+"_tl",attr);//@@@@@@
		}
		File nList = new File(rootpath+"\\getNetData\\nameList.csv");
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(nList),"UTF-8"));
			System.out.println("cnt:"+nameList.get("cnt"));
			nameList.remove("cnt");
			for(String key : nameList.keySet()) {
				writer.write(key+","+nameList.get(key)+"\r\n");
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		nameList.clear();
		
		rootpath = rootpath+"\\getNetData";
		rootf = new File(rootpath);
		rootlist = rootf.list();
		fileAttribute[] f2 = new fileAttribute[4];
		int fcnt = 0;
		try {
			for(int i=0;i<rootlist.length;i++) {
				String temppath = rootpath+"\\"+rootlist[i];
				File tempf = new File(temppath);
				if(!tempf.isFile()||(!rootlist[i].contains(loc))||(!rootlist[i].contains("csv"))) {
					continue;
				}
				f2[fcnt] = new fileAttribute(temppath,",");
				fcnt++;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		constructNet(f2);
		
	}
	
	public static void getHKNet(String loc, String rootpath,String trange, int filey, int filem) {
		// get netdata from tl record
		File rootf = new File(rootpath);
		String[] rootlist = rootf.list();
		
		fileAttribute[][] f = new fileAttribute[4][12];
		
		try {
			for(int i=0;i<rootlist.length;i++) {
				String temppath = rootpath+"\\"+rootlist[i];
				File tempf = new File(temppath);
				if(!tempf.isFile()||(!rootlist[i].contains(loc))) {
					continue;
				}
				if(rootlist[i].contains(trange.split(",")[0])||rootlist[i].contains(trange.split(",")[1])) {
					continue;
				}
				int year = Integer.parseInt(rootlist[i].split("_")[filey].substring(0,4))-Integer.parseInt(trange.split(",")[0])-1;//@@@@
				int mon = Integer.parseInt(rootlist[i].split("_")[filey].substring(filem,filem+2))-1;
				f[year][mon] = new fileAttribute(temppath,",");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		Map<String,Integer> nameList = new HashMap<String,Integer>();
		nameList.put("cnt",0);
		for(int i=0;i<4;i++) {
			System.out.println("getNetworkData From:"+f[i][0].getFilePath());
			int[] attr = {3,0,1,7,4,8};
			nameList = getTLNetData(f[i], nameList, (Integer.parseInt(trange.split(",")[0])+i)+"_"+loc+"_"+loc+"_hk",attr);//@@@@@@
		}
		File nList = new File(rootpath+"\\getNetData\\nameList.csv");
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(nList),"UTF-8"));
			System.out.println("cnt:"+nameList.get("cnt"));
			nameList.remove("cnt");
			for(String key : nameList.keySet()) {
				writer.write(key+","+nameList.get(key)+"\r\n");
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		nameList.clear();
		
		rootpath = rootpath+"\\getNetData";
		rootf = new File(rootpath);
		rootlist = rootf.list();
		fileAttribute[] f2 = new fileAttribute[4];
		int fcnt = 0;
		try {
			for(int i=0;i<rootlist.length;i++) {
				String temppath = rootpath+"\\"+rootlist[i];
				File tempf = new File(temppath);
				if(!tempf.isFile()||(!rootlist[i].contains(loc))||(!rootlist[i].contains("csv"))) {
					continue;
				}
				f2[fcnt] = new fileAttribute(temppath,",");
				fcnt++;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		constructNet(f2);
		
	}
	
	public static void getCoTravel(String fpath, String opath) {
		Map<String,String> seq = new HashMap<String,String>();
		File f = new File(fpath);
		File f2 = new File(opath);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF-8"));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f2),"UTF-8"));
			String str = "";
			int cnt = 0;
			while((str=br.readLine())!=null) {
				cnt++;
				if(cnt%10000 == 0) {
					System.out.println(cnt);
				}
				String[] temp = (str+"@").split(",");
				String tag = "";
				if(!temp[temp.length-2].equals("")) {
					tag = temp[temp.length-2];
				}else {
					if(!temp[temp.length-3].equals("")) {
						tag = temp[temp.length-3];
					}else {
						continue;
					}
				}
				//System.out.println(tag);
				if(seq.containsKey(tag)) {
					seq.put(tag,seq.get(tag)+"@@"+str);
				}else {
					seq.put(tag,str);
				}
			}
			for(String key:seq.keySet()) {
				bw.write(seq.get(key)+"\r\n");
			}
			br.close();
			bw.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		getCoTravel("E:\\dataManageWorkplace\\20191228\\TL\\total\\cutFile\\fixed\\fixed_2018_dy_raildata_md5.csv", 
				"E:\\dataManageWorkplace\\20191228\\TL\\total\\cutFile\\fixed\\net\\2018_dy.csv");
		//System.out.println("G:\\Train_Data-origin\\tl2015-2017\\deleteSymbol\\fileAdd\\md5\\cutFile\\cutFile");
		//getTLNet("dy","G:\\Train_Data-origin\\tl2015-2017\\deleteSymbol\\fileAdd\\md5\\cutFile\\cutFile","2014,2019",1,4);
		
		
		// TODO Auto-generated method stub
//		String loc = "mig";//@@@@
//		String rootpath = "E:\\dataManageWorkplace\\my_tl\\cutFile\\Teaming";//@@@
//		File rootFile = new File(rootpath);
//		String[] rootList = rootFile.list();
//		fileAttribute[][] f = new fileAttribute[4][12];
//		try {
//			
//			for(int i=0;i<rootList.length;i++) {
//				String temppath = rootpath+"\\"+rootList[i];
//				File tempf = new File(temppath);
//				if(rootList[i].contains("2014")||rootList[i].contains("2019")) {
//					continue;
//				}
//				if(!tempf.isFile()||(!rootList[i].contains(loc))) {
//					continue;
//				}
//				int year = Integer.parseInt(rootList[i].split("_")[1].substring(0,4))-2015;//@@@@
//				int mon = Integer.parseInt(rootList[i].split("_")[1].substring(4,6))-1;
//				
//				
//				f[year][mon] = new fileAttribute(temppath,",");
//				
//			}
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//		Map<String,Integer> nameList = new HashMap<String,Integer>();
//		nameList.put("cnt",0);
//		for(int i=0;i<4;i++) {
//			System.out.println("getNetworkData From:"+f[i][0].getFilePath());
//			int[] attr = {3,0,1,7,4,8};
//			nameList = getTLNetData(f[i], nameList, (2015+i)+"_mig_mig_tl",attr);//@@@@@@
//		}
//		File nList = new File(rootpath+"\\getNetData\\nameList.csv");
//		try {
//			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(nList),"UTF-8"));
//			System.out.println("cnt:"+nameList.get("cnt"));
//			nameList.remove("cnt");
//			for(String key : nameList.keySet()) {
//				writer.write(key+","+nameList.get(key)+"\r\n");
//			}
//			writer.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		nameList.clear();
//		
//		rootpath = rootpath+"\\getNetData";
//		rootFile = new File(rootpath);
//		rootList = rootFile.list();
//		fileAttribute[] f2 = new fileAttribute[4];
//		int fcnt = 0;
//		try {
//			for(int i=0;i<rootList.length;i++) {
//				String temppath = rootpath+"\\"+rootList[i];
//				File tempf = new File(temppath);
//				if(!tempf.isFile()||(!rootList[i].contains(loc))) {
//					continue;
//				}
//				f2[fcnt] = new fileAttribute(temppath,",");
//				fcnt++;
//			}
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//		constructNet(f2);
//	}
	}

}
