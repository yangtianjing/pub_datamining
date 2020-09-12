package network;

import java.util.HashMap;
import java.util.Map;
import CrossData.fileAttribute;
import java.io.*;

/*
function for filter members from the network
step:
1. create a original network
2. select members' id field, find members' record in different network base on id field 
3. after getting same members in different network, find the member that not in these network
4. delete member find in step 3 in original network

step1 ==> original member list, original network list
step2 ==> same members in other network that appear in original network once
step3/step4 ==> network that we need!!!!!
 */
public class networkFilter {
	
	public static Map<String,String> getMemberList(fileAttribute f, int[] tags, boolean ignoreLine1) {
		Map<String,String> mls = new HashMap<String,String>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(f.getFilePath())),"UTF-8"));
			String str = "";
			if(ignoreLine1) {
				str = br.readLine();
			}
			while((str=br.readLine())!=null) {
				String[] temp = str.split(",");
				for(int i=0;i<tags.length;i++) {
					if(!mls.containsKey(temp[tags[i]])) {
						mls.put(temp[tags[i]], "");
					}
				}
			}
			br.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return mls;
	}
	
	public static Map<String,String> cmpList(Map<String,String> ls1, Map<String,String> ls2){
		Map<String,String> mls = new HashMap<String,String>();
		for(String keys:ls1.keySet()) {
			if(ls2.containsKey(keys)) {
				mls.put(keys, "");
				//System.out.println(keys);
				ls2.remove(keys);
			}
		}
		
		return mls;
	}
	
	public static void writeList(fileAttribute inf, File outf, Map<String,String> mls, int[] tags) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inf.getFilePath())),"UTF-8"));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outf.getAbsolutePath())),"UTF-8"));
			String str = "";
			
			while((str = br.readLine())!=null) {
				String[] temp = str.split(",");
				for(int i=0;i<tags.length;i++) {
					if(mls.containsKey(temp[tags[i]])) {
						bw.write(str+"\r\n");
						break;
					}
				}
			}
			bw.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void writeList2(fileAttribute inf, File outf, Map<String,String> mls, int[] tags) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inf.getFilePath())),"UTF-8"));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outf.getAbsolutePath())),"UTF-8"));
			String str = "";
			Map<String,String> rs = new HashMap<String,String>();
			while((str = br.readLine())!=null) {
				if(!rs.containsKey(str)) {
					rs.put(str, "");
				}
			}
			for(String keys:rs.keySet()) {
				String[] temp = keys.split(",");
				for(int i=0;i<tags.length;i++) {
					if(mls.containsKey(temp[tags[i]])) {
						bw.write(keys+"\r\n");
						break;
					}
				}
			}
			bw.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void writeCmpList(Map<String,String> cmpls, File outf) {
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outf.getAbsolutePath())),"UTF-8"));
			
			for(String keys:cmpls.keySet()) {
				//System.out.println(keys);
				bw.write(keys+"\r\n");
			}
			bw.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static Map<String,String> distinctList(Map<String,String> ls){
		Map<String,String> rs = new HashMap<String,String>();
		for(String keys:ls.keySet()) {
			if(!rs.containsKey(ls.get(keys))) {
				rs.put(ls.get(keys), "");
			}
		}
		return rs;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		String rootpath = "E:\\dataManageWorkplace\\my_tl\\cutFile\\Teaming\\getNetData\\Network";
		File rootf = new File(rootpath);
		String[] rootls = rootf.list();
		Map<Integer, fileAttribute> fls = new HashMap<Integer, fileAttribute>();
		
		for(int i=0;i<rootls.length;i++) {
			if(!rootls[i].contains("edge")||(!rootls[i].contains("csv"))) {
				continue;	
			}
			fileAttribute f = new fileAttribute(rootf.getAbsolutePath()+"\\"+rootls[i],",");
			fls.put(fls.size(),f);
		}
		System.out.println("Step1");
		//Step1
		fileAttribute originf;
		Map<String,String> ols = new HashMap<String,String>();
		String starty = "2015";
		for(int i=0;i<fls.size();i++) {
			if(fls.get(i).getFileName().contains(starty)) {
				originf = fls.get(i);
				int[] tags = {1,2};
				ols = getMemberList(originf, tags, true);
				break;
			}
		}
		Map<Integer,Map<String, String>> dls = new HashMap<Integer,Map<String, String>>();
		for(int i=0;i<fls.size();i++) {
			if(fls.get(i).getFileName().contains(starty)) {
				continue;
			}
			Map<String,String> templs = new HashMap<String,String>();
			int[] tags = {1,2};
			templs = getMemberList(fls.get(i), tags, true);
			dls.put(dls.size(),templs);
		}
		System.out.println("Step2");
		//Step2
		Map<String, String> cmpls = new HashMap<String, String>();
		cmpls = ols;
		for(int i=0;i<dls.size();i++) {
			cmpls = cmpList(cmpls, dls.get(i));
		}
		System.out.println("Step3,4");
		//step3,step4
		String rsFolder = rootpath+"\\networkFilter";
		File rsf = new File(rsFolder);
		if(!rsf.exists()) {
			rsf.mkdir();
		}
		//System.out.println(cmpls.size());
		File outcmp = new File(rsFolder+"\\cmpls.csv");
		if(!outcmp.exists()) {
			try {
				outcmp.createNewFile();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		writeCmpList(cmpls, outcmp);
		for(int i=0;i<fls.size();i++) {
			int[] tags = {1,2};
			File outfile = new File(rsFolder+"\\filter_"+fls.get(i).getFileName());
			if(!outfile.exists()) {
				try {
					outfile.createNewFile();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println(fls.get(i).getFilePath());
			writeList(fls.get(i),outfile,cmpls,tags);
		}
		*/
		/*
		String rootpath = "E:\\dataManageWorkplace\\dy_tl";
		File rootf = new File(rootpath);
		String[] rootls = rootf.list();
		Map<Integer, fileAttribute> fls = new HashMap<Integer, fileAttribute>();
		
		for(int i=0;i<rootls.length;i++) {
			if(!rootls[i].contains("edge")||(!rootls[i].contains("csv"))) {
				continue;	
			}
			fileAttribute f = new fileAttribute(rootf.getAbsolutePath()+"\\"+rootls[i],",");
			fls.put(fls.size(),f);
		}
		System.out.println("Step1");
		//Step1
		fileAttribute originf;
		Map<String,String> ols = new HashMap<String,String>();
		String starty = "2015";
		for(int i=0;i<fls.size();i++) {
			if(fls.get(i).getFileName().contains(starty)) {
				originf = fls.get(i);
				int[] tags = {1,2};
				ols = getMemberList(originf, tags, true);
				break;
			}
		}
		Map<Integer,Map<String, String>> dls = new HashMap<Integer,Map<String, String>>();
		for(int i=0;i<fls.size();i++) {
			if(fls.get(i).getFileName().contains(starty)) {
				continue;
			}
			Map<String,String> templs = new HashMap<String,String>();
			int[] tags = {1,2};
			templs = getMemberList(fls.get(i), tags, true);
			dls.put(dls.size(),templs);
		}
		System.out.println("Step2");
		//Step2
		Map<String, String> cmpls = new HashMap<String, String>();
		cmpls = ols;
		Map<Integer,Map<String, String>> cmptemp = new HashMap<Integer,Map<String, String>>();
		
		for(int i=0;i<dls.size();i++) {
			cmptemp.put(cmptemp.size(),cmpList(ols, dls.get(i)));
		}
		for(int i=0;i<cmptemp.size();i++) {
			for(String keys:cmptemp.get(i).keySet()) {
				if(!cmpls.containsKey(keys)) {
					cmpls.put(keys,"");
				}
			}
		}
		System.out.println("Step3,4");
		//step3,step4
		String rsFolder = rootpath+"\\networkFilter2";
		File rsf = new File(rsFolder);
		if(!rsf.exists()) {
			rsf.mkdir();
		}
		//System.out.println(cmpls.size());
		File outcmp = new File(rsFolder+"\\cmpls.csv");
		if(!outcmp.exists()) {
			try {
				outcmp.createNewFile();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		writeCmpList(cmpls, outcmp);
		for(int i=0;i<fls.size();i++) {
			int[] tags = {1,2};
			File outfile = new File(rsFolder+"\\filter_"+fls.get(i).getFileName());
			if(!outfile.exists()) {
				try {
					outfile.createNewFile();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			System.out.println(fls.get(i).getFilePath());
			writeList(fls.get(i),outfile,cmpls,tags);
		}
		*/
		
		File firstFolder = new File("E:\\dataManageWorkplace");
		String[] firstlist = firstFolder.list();
		for(int i=0;i<firstlist.length;i++) {
			File secFolder = new File(firstFolder.getAbsolutePath()+"\\"+firstlist[i]);
			if(!secFolder.isDirectory()||(!firstlist[i].contains("_"))) {
				continue;
			}
			String[] seclist = secFolder.list();
			for(int j=0;j<seclist.length;j++) {
				if(!seclist[j].contains("networkFilter")) {
					continue;
				}
				//System.out.println(secFolder.getAbsolutePath()+"\\"+seclist[j]);
				File thirdFolder = new File(secFolder.getAbsolutePath()+"\\"+seclist[j]);
				String[] thirdlist = thirdFolder.list();
				fileAttribute cutlist = new fileAttribute();
				Map<Integer,fileAttribute> f = new HashMap<Integer,fileAttribute>();
				for(int k=0;k<thirdlist.length;k++) {
					if(thirdlist[k].contains("cutlist")) {
						cutlist = new fileAttribute(thirdFolder.getAbsolutePath()+"\\"+thirdlist[k],",");
					}
					if(thirdlist[k].contains("filter")) {
						fileAttribute tempf = new fileAttribute(thirdFolder.getAbsolutePath()+"\\"+thirdlist[k],",");
						f.put(f.size(),tempf);
					}
				}
				Map<String,String> templs = new HashMap<String,String>();
				int[] tags = {0};
				templs = getMemberList(cutlist, tags, false);
				
				for(int k=0;k<f.size();k++) {
					int[] tags2 = {1,2};
					System.out.println(thirdFolder.getAbsolutePath()+"\\getCutFile\\cut_"+f.get(k).getFileName());
					File outfile = new File(thirdFolder.getAbsolutePath()+"\\getCutFile\\cut_"+f.get(k).getFileName());
					if(!outfile.getParentFile().exists()) {
						try {
							outfile.getParentFile().mkdir();
						}catch(Exception e) {
							e.printStackTrace();
						}
					}
					if(!outfile.exists()) {
						try {
							outfile.createNewFile();
						}catch(Exception e) {
							e.printStackTrace();
						}
					}
					writeList2(f.get(k),outfile,templs,tags2);
				}
			}
		}
		
	}

}
