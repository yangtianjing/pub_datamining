package network;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import CrossData.combInFile;
import CrossData.fileAttribute;
import network.getNetworkData;
public class fixedNet {
	public static void netFilter(fileAttribute f, fileAttribute[] fs) {
		try {
			File t = new File(f.getFilePath());
			Map<String,String> ls = new HashMap<String,String>();
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(t),"UTF-8"));
			String str = "";
			while((str = br.readLine())!=null) {
				String[] temp = str.split(",");
				if(!ls.containsKey(temp[1])) {
					ls.put(temp[1],"");
				}
				if(!ls.containsKey(temp[2])) {
					ls.put(temp[1],"");
				}
			}
			br.close();
			File folder = new File(f.getFolder()+"\\netFilter");
			folder.mkdir();
			for(int i=0;i<fs.length;i++) {
				File tf = new File(fs[i].getFilePath());
				File outf = new File(f.getFolder()+"\\netFilter\\"+fs[i].getFileName());
				System.out.println(outf.getAbsolutePath());
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(tf),"UTF-8"));
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outf),"UTF-8"));
				while((str = reader.readLine())!=null) {
					String[] temp = str.split(",");
					if(ls.containsKey(temp[1])) {
						if(ls.containsKey(temp[2])) {
							bw.write(str+"\r\n");
						}
					}
				}
				bw.close();
				reader.close();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void easyTransToNet(fileAttribute[] fs, int attri, int[] attr) {
		try {
			Map<String,Integer> ls = new HashMap<String,Integer>();
			int cnt = 0;
			for(int i=0;i<fs.length;i++) {
				System.out.println("read: "+fs[i].getFilePath());
				File f = new File(fs[i].getFilePath());
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF-8"));
				String str = "";
				while((str = br.readLine())!=null) {
					if(!str.contains("@@")) {
						continue;
					}
					String[] strs = str.split("@@");
					for(int j=0;j<strs.length;j++) {
						String[] temp = strs[j].split(",");
						if(!ls.containsKey(temp[attri])) {
							ls.put(temp[attri],cnt);
							cnt++;
						}
					}
				}
				br.close();
			}
			for(int i=0;i<fs.length;i++) {
				System.out.println("write: "+fs[i].getFilePath());
				File f = new File(fs[i].getFilePath());
				File folder = new File(fs[i].getFolder()+"\\net");
				folder.mkdir();
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f),"UTF-8"));
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fs[i].getFolder()+"\\net\\"+fs[i].getFileName()),"UTF-8"));
				String str = "";
				while((str = br.readLine())!=null) {
					if(!str.contains("@@")) {
						continue;
					}
					String[] strs = str.split("@@");
					for(int j=0;j<strs.length;j++) {
						for(int k=j+1;k<strs.length;k++) {
							String tag = "";
							String[] tmp = strs[j].split(",");
							String[] temp = strs[k].split(",");
							tag+=tmp[attr[0]]+",";
							tag+=ls.get(tmp[attri])+",";
							tag+=ls.get(temp[attri])+",";
							tag+=tmp[attr[1]]+"\r\n";
							bw.write(tag);
						}
					}
				}
				br.close();
				bw.close();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File rootf = new File("E:\\dataManageWorkplace\\20191228\\TL\\total\\cutFile\\filterByFileAttr\\fixed");
		String[] rootls = rootf.list();
		fileAttribute[] fs = new fileAttribute[4];
		int cnt = 0;
		int[] attr = {16,26};
		String[] local = {"dy","mig","ctu"};
		for(int l=0;l<local.length;l++) {
			for(int i=0;i<rootls.length;i++) {
				File tf = new File(rootf.getAbsolutePath()+"\\"+rootls[i]);
				if(tf.isFile()&&!rootls[i].contains("name")&&rootls[i].contains(local[l])) {
					File tfs = new File(rootf.getAbsolutePath()+"\\coTravel\\"+rootls[i]);
					File tfold = new File(rootf.getAbsolutePath()+"\\coTravel");
					tfold.mkdir();
					getNetworkData.getCoTravel(tf.getAbsolutePath(), rootf.getAbsolutePath()+"\\coTravel\\"+rootls[i]);
					fs[cnt] = new fileAttribute(tfs.getAbsolutePath(),",");
					cnt++;
				}
			}
			easyTransToNet(fs, 6, attr);
			cnt=0;
		}
		
		
		/*
		File rootf = new File("E:\\dataManageWorkplace\\20191228\\TEMP");
		String[] rootls = rootf.list();
		fileAttribute f = new fileAttribute("E:\\dataManageWorkplace\\20191228\\TEMP\\edge_2015_dy_dy.csv",",");
		fileAttribute[] fs = new fileAttribute[4];
		int cnt = 0;
		for(int i=0;i<rootls.length;i++) {
			File tf = new File(rootf.getAbsolutePath()+"\\"+rootls[i]);
			if(tf.isFile()) {
				fs[cnt] = new fileAttribute(tf.getAbsolutePath(),",");
				cnt++;
			}
		}
		netFilter(f, fs);
		*/
	}

}
