package CrossData;
import java.io.*;
import java.util.*;
import CrossData.fileCut;
public class addTeamNum {
	public static void addTNum(fileAttribute f,int[][] attr) {
		try {
			File infile = new File(f.getFilePath());
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(infile),"UTF-8"));
			String str = "";
			Map<String,String> hsm = new HashMap<String,String>();
			while((str = reader.readLine())!=null) {
				String left= "";
				String right = "";
				String[] temp = (str+"end").split(f.getFileSeparator());
				for(int i=0;i<attr[0].length;i++) {
					
					//System.out.print(attr[0][i]+",");
					if(i!=attr[0].length-1) {
						left+=temp[attr[0][i]];
						left+=",";
					}else {
						left+=temp[attr[0][i]].replaceAll("end", "");
						left+="\r\n";
					}
				}
				//System.out.print(left+"  ");
				for(int i=0;i<attr[1].length;i++) {
					
					//System.out.print(attr[1][i]+",");
					if(i!=attr[1].length-1) {
						right+=temp[attr[1][i]];
						right+=",";
					}else {
						right+=temp[attr[1][i]].replaceAll("end", "");
						right+="\r\n";
					}
				}
				//System.out.println(right);
				if(hsm.containsKey(left)) {
					hsm.put(left,hsm.get(left)+"#"+right);
					//System.out.println(hsm.get(left));
				}else {
					hsm.put(left, right);
				}
				if(hsm.containsKey(right)) {
					hsm.put(right,hsm.get(right)+"#"+left);
				}else {
					hsm.put(right, left);
				}
			}
			reader.close();
			//output
			File outpath = new File(f.getFolder()+"\\addTeamNum");
			if(!outpath.exists()) {
				outpath.mkdir();
			}
			File outfile = new File(outpath.getAbsoluteFile()+"\\"+f.getFileName());
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile),"UTF-8"));
			int teamcnt=0;
			Map<String,String> hsm2 = new HashMap<String,String>();
			while(!hsm.isEmpty()) {
				String tempv = "";
				for(String key:hsm.keySet()) {
					hsm2.put(key,"y");
					tempv = hsm.get(key);
					//System.out.println(tempv);
					break;
				}
				String[] temp = tempv.split("#");
				for(int i=0;i<temp.length;i++) {
					hsm2.put(temp[i],"f");
					//System.out.println(temp[i]+"  "+i);
				}
				//System.out.println(temp.length);
				for(String key:hsm2.keySet()) {
					if(hsm2.get(key).equals("y")) {
						writer.write(teamcnt+f.getFileSeparator()+key);
						hsm.remove(key);
						//System.out.println(teamcnt+f.getFileSeparator()+key);
					}else {
						writer.write(teamcnt+f.getFileSeparator()+key);
						hsm.remove(key);
						//System.out.println(teamcnt+f.getFileSeparator()+key);
					}
				}
				teamcnt++;
				hsm2.clear();
			}
			writer.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//step 1 divide the file with team cnt and plane cnt
		File rootpath = new File("H:\\HK\\home\\hadoop\\fantasy\\xnjd");
		String[] filelist = rootpath.list();/*
		for(int i=0;i<filelist.length;i++) {
			File tempf = new File(rootpath.getAbsoluteFile()+"\\"+filelist[i]);
			if(tempf.isFile()&&filelist[i].contains(".csv")) {
				fileAttribute f = new fileAttribute(tempf.getAbsolutePath(),",");
				System.out.println("cut:"+tempf.getAbsolutePath());
				fileCut.cutFileByAttr(f, 0, 0, 7);
			}
		}
		*/
		
		File cutpath = new File(rootpath.getAbsoluteFile()+"\\cutFile");
		String[] cutlist = cutpath.list();
		
		for(int i=0;i<cutlist.length;i++) {
			File tempf = new File(cutpath.getAbsoluteFile()+"\\"+cutlist[i]);
			if(tempf.isFile()&&cutlist[i].contains(".csv")) {
				fileAttribute f = new fileAttribute(tempf.getAbsolutePath(),",");
				System.out.println("cut:"+tempf.getAbsolutePath());
				fileCut.cutFileByAttr(f, 13, 0, 1);
			}
		}
		//step 2 use function to divide team and add team num
		
		/*
		for(int i=0;i<cutlist.length;i++) {
			File tempf = new File(cutpath.getAbsoluteFile()+"\\"+cutlist[i]);
			if(tempf.isFile()&&cutlist[i].contains(".csv")) {
				fileAttribute f = new fileAttribute(tempf.getAbsolutePath(),",");
				System.out.println("add Team sequence:"+tempf.getAbsolutePath());
				int[][] attr = {{0,5,6,7,11,12,13,14,15},{0,8,9,10,11,12,13,14,15}};
				addTNum(f,attr);
			}
		}*/
		
		cutpath = new File(rootpath.getAbsoluteFile()+"\\cutFile\\cutFile");
		cutlist = cutpath.list();
		for(int i=0;i<cutlist.length;i++) {
			File tempf = new File(cutpath.getAbsoluteFile()+"\\"+cutlist[i]);
			if(tempf.isFile()&&cutlist[i].contains(".csv")) {
				fileAttribute f = new fileAttribute(tempf.getAbsolutePath(),",");
				System.out.println("add Team sequence:"+tempf.getAbsolutePath());
				int[][] attr = {{0,5,6,7,8,13,14,15,16,17},{0,9,10,11,12,13,14,15,16,17}};
				addTNum(f,attr);
			}
		}
		//step 3 comb files to one total file 
	}

}
