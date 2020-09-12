package DIDInfo;
import java.io.*;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;


public class getRecordInfo {
	
	public static int getSexFromID(String id) {
		int rs = Integer.parseInt(id.substring(16, 17)) % 2;
		return rs;
	}
	public static void staRecordNumByTag(String fpath,int id,int dates,int attr,String[] tag) {
		try {
			File f = new File(fpath);
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF-8"));
			Map<String,Integer> malerecord = new HashMap<String,Integer>();
			Map<String,Integer> femalerecord = new HashMap<String,Integer>();
			String str = "";
			while((str = br.readLine())!=null) {
				String[] temp = str.split(",");
				if(temp[id].length()!=18) {
					continue;
				}
				boolean p = false;
				for(int i=0;i<tag.length;i++) {
					if(temp[attr].contains(tag[i])) {
						p = true;
						break;
					}
				}
				if(!p) continue;
				if(getSexFromID(temp[id])==0) {
					if(femalerecord.containsKey(temp[dates])) {
						femalerecord.put(temp[dates],femalerecord.get(temp[dates])+1);
					}else{
						femalerecord.put(temp[dates],1);
					}
				}else {
					if(malerecord.containsKey(temp[dates])) {
						malerecord.put(temp[dates],malerecord.get(temp[dates])+1);
					}else{
						malerecord.put(temp[dates],1);
					}
				}
			}
			br.close();
			File outf = new File(f.getParent()+"\\staRecordNum\\statag_"+f.getName());
			File folder = new File(f.getParent()+"\\staRecordNum");
			if(!folder.exists()) {
				folder.mkdir();
			}
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outf),"UTF-8"));
			Set<String> allKey = new HashSet<String>();
			
			for(String k:malerecord.keySet()) {
				allKey.add(k);
			}
			for(String k:femalerecord.keySet()) {
				allKey.add(k);
			}
			for(String k:allKey) {
				if(k.length()!=8) {
					continue;
				}
				bw.write(k.substring(0,4)+"-"+k.substring(4,6)+"-"+k.substring(6,8)+",");
				if(malerecord.containsKey(k)) {
					bw.write(malerecord.get(k)+",");
				}else {
					bw.write(0+",");
				}
				if(femalerecord.containsKey(k)) {
					bw.write(femalerecord.get(k)+"\r\n");
				}else {
					bw.write(0+"\r\n");
				}
			}
			bw.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void staRecordNum(String fpath,int id,int dates) {
		try {
			File f = new File(fpath);
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF-8"));
			Map<String,Integer> malerecord = new HashMap<String,Integer>();
			Map<String,Integer> femalerecord = new HashMap<String,Integer>();
			String str = "";
			while((str = br.readLine())!=null) {
				String[] temp = str.split(",");
				if(temp[id].length()!=18) {
					continue;
				}
				if(getSexFromID(temp[id])==0) {
					if(femalerecord.containsKey(temp[dates])) {
						femalerecord.put(temp[dates],femalerecord.get(temp[dates])+1);
					}else{
						femalerecord.put(temp[dates],1);
					}
				}else {
					if(malerecord.containsKey(temp[dates])) {
						malerecord.put(temp[dates],malerecord.get(temp[dates])+1);
					}else{
						malerecord.put(temp[dates],1);
					}
				}
			}
			br.close();
			File outf = new File(f.getParent()+"\\staRecordNum\\sta_"+f.getName());
			File folder = new File(f.getParent()+"\\staRecordNum");
			if(!folder.exists()) {
				folder.mkdir();
			}
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outf),"UTF-8"));
			Set<String> allKey = new HashSet<String>();
			
			for(String k:malerecord.keySet()) {
				allKey.add(k);
			}
			for(String k:femalerecord.keySet()) {
				allKey.add(k);
			}
			for(String k:allKey) {
				if(k.length()!=8) {
					continue;
				}
				bw.write(k.substring(0,4)+"-"+k.substring(4,6)+"-"+k.substring(6,8)+",");
				if(malerecord.containsKey(k)) {
					bw.write(malerecord.get(k)+",");
				}else {
					bw.write(0+",");
				}
				if(femalerecord.containsKey(k)) {
					bw.write(femalerecord.get(k)+"\r\n");
				}else {
					bw.write(0+"\r\n");
				}
			}
			bw.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void staRecordNumHK(String fpath,int id,int dates) {
		try {
			File f = new File(fpath);
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF-8"));
			Map<String,Integer> malerecord = new HashMap<String,Integer>();
			Map<String,Integer> femalerecord = new HashMap<String,Integer>();
			String str = "";
			while((str = br.readLine())!=null) {
				String[] temp = str.split(",");
				if(temp[id].split(";")[1].replace("\"","").length()!=1) {
					continue;
				}
				if(temp[id].split(";")[1].replace("\"","").equals("0")) {
					if(femalerecord.containsKey(temp[dates])) {
						femalerecord.put(temp[dates],femalerecord.get(temp[dates])+1);
					}else{
						femalerecord.put(temp[dates],1);
					}
				}else if(temp[id].split(";")[1].replace("\"","").equals("1")){
					if(malerecord.containsKey(temp[dates])) {
						malerecord.put(temp[dates],malerecord.get(temp[dates])+1);
					}else{
						malerecord.put(temp[dates],1);
					}
				}
			}
			br.close();
			File outf = new File(f.getParent()+"\\staRecordNum\\sta_"+f.getName());
			File folder = new File(f.getParent()+"\\staRecordNum");
			if(!folder.exists()) {
				folder.mkdir();
			}
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outf),"UTF-8"));
			Set<String> allKey = new HashSet<String>();
			
			for(String k:malerecord.keySet()) {
				allKey.add(k);
			}
			for(String k:femalerecord.keySet()) {
				allKey.add(k);
			}
			for(String k:allKey) {
				if(k.length()!=10) {
					continue;
				}
				bw.write(k+",");
				if(malerecord.containsKey(k)) {
					bw.write(malerecord.get(k)+",");
				}else {
					bw.write(0+",");
				}
				if(femalerecord.containsKey(k)) {
					bw.write(femalerecord.get(k)+"\r\n");
				}else {
					bw.write(0+"\r\n");
				}
			}
			bw.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String rootpath = "E:\\dataManageWorkplace\\20191228\\TL\\total";
		File rootf = new File(rootpath);
		String[] rootls = rootf.list();
		for(int i=0;i<rootls.length;i++) {
			String fpath = rootpath+"\\"+rootls[i];
			File tf = new File(fpath);
			if(tf.isDirectory()||(!rootls[i].contains("csv"))) {
				continue;
			}
			System.out.println("sta: "+fpath);
			String[] tag = {"G","D"};
			staRecordNumByTag(fpath,6,16,15,tag);
		}
		
		/*
		String rootpath = "E:\\dataManageWorkplace\\20191228\\HK\\total";
		File rootf = new File(rootpath);
		String[] rootls = rootf.list();
		for(int i=0;i<rootls.length;i++) {
			String fpath = rootpath+"\\"+rootls[i];
			File tf = new File(fpath);
			if(tf.isDirectory()||(!rootls[i].contains("csv"))) {
				continue;
			}
			System.out.println("sta: "+fpath);
			staRecordNumHK(fpath,3,0);
		}
		*/
	}

}
