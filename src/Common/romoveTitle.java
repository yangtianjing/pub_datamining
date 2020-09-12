package Common;
import java.io.*;
public class romoveTitle {
	public static void removeTitles(String file,String title) {
		File infile = new File(file);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(infile),"UTF-8"));
			File outpath = new File(infile.getParent()+"\\removetitles");
			if(!outpath.exists()) {
				outpath.mkdir();
			}
			System.out.println("read:"+infile.getAbsolutePath());
			File outfile = new File(outpath.getAbsolutePath()+"\\"+infile.getName());
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile),"UTF-8"));
			String str="";
			br.readLine();
			while((str=br.readLine())!=null) {
				if(!str.contains(title)) {
					bw.write(str+"\r\n");
				}else {
					System.out.println("one record deleted");
				}
			}
			br.close();
			bw.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File rootpath = new File("G:\\Train_Data-origin\\tl2015-2017\\deleteSymbol\\fileAdd\\fileAdd\\removetitles");
		String[] filelist = rootpath.list();
		for(int i=0;i<filelist.length;i++) {
			File temp = new File(rootpath.getAbsolutePath()+"\\"+filelist[i]);
			if(temp.isFile()) {
				removeTitles(temp.getAbsolutePath(),"statistics_date");
			}
		}
		
	}

}
