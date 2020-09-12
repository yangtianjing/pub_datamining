package CrossData;
import java.io.*;
import java.util.Map;
import java.util.HashMap;
import CrossData.fileAttribute;

public class combInFile {
	public static int findTag(String str, char Tag) {
		int cnt = 0;
		char[] ch = str.toCharArray();
		for(int i=0;i<ch.length;i++) {
			if(ch[i] == Tag) {
				cnt++;
			}
		}
		
		return cnt;
	}
	
	public static String outputTeam(String tstr,int cnt) {
		
		String rs = "";
		String[] temp = tstr.split("@");
		for(int i=0;i<temp.length;i++) {
			String[] s1 = temp[i].split(",");
			rs+=cnt+","+s1[16]+","+s1[5]+","+s1[6]+","+s1[7]+","+s1[24]+","+s1[25]+","+s1[26]+","+s1[27]+"\r\n";
		}
		
		return rs;
	}
	public static void combInFiles(fileAttribute f, int[] attr) {//29 30
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f.getFilePath()),"UTF-8"));
			File outfolder = new File(f.getFolder()+"\\Teaming");
			if(!outfolder.exists()) {
				outfolder.mkdir();
			}
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfolder.getAbsolutePath()+"\\teaming_"+f.getFileName()),"UTF-8"));
			String str = "";
			Map<String,String> teamlist = new HashMap<String,String>();
			System.out.println("comb:"+f.getFilePath());
			while((str = reader.readLine())!=null) {
				str = str +"@";
				String k = "";
				String[] temp = str.split(",");
				for(int i=0;i<attr.length-1;i++) {
					k+=temp[attr[i]]+",";
				}
				k+=temp[attr[attr.length-1]];
				if(k.replaceAll(",", "").equals("")) {
					continue;
				}
				
				if(teamlist.containsKey(k)) {
					teamlist.put(k,teamlist.get(k)+str);
				}else {
					teamlist.put(k,str);
				}
			}
			reader.close();
			int cnt = 0;
			
			for(String keys : teamlist.keySet()) {
				String teamstr = teamlist.get(keys);
				if(findTag(teamstr,'@')>1) {
					writer.write(outputTeam(teamstr,cnt));
					cnt++;
				}
				
			}
			writer.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File rootfile = new File("E:\\dataManageWorkplace\\my_tl\\cutFile");
		String[] rootlist = rootfile.list();
		int[] attr = {29,30};
		
		for(int i=0;i<rootlist.length;i++) {
			if(!rootlist[i].contains("mig")||(!rootlist[i].contains("csv"))) {
				continue;
			}
			//System.out.println("start");
			fileAttribute f = new fileAttribute(rootfile.getAbsolutePath()+"\\"+rootlist[i],",");
			//System.out.println(f.getFilePath());
			combInFiles(f,attr);
		}
	}

}
