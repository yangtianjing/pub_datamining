package CrossData;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import CrossData.fileAttribute;

public class fileFilter {
	public static void fileFilterByPlaceContain(fileAttribute files,int attr,String containStr,String encode,int fromP,int toP) {
		File f = new File(files.getFilePath());
		File fFolder = new File(files.getFolder()+"\\fileFileter");
		if(!fFolder.exists()) {
			fFolder.mkdir();
		}
		String outfile = fFolder.getAbsolutePath()+"\\"+files.getFileName();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f),encode));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(outfile)),encode));
			String str=reader.readLine();
			String[] tag=containStr.split(",");
			boolean signals =false;
			while((str=reader.readLine())!=null) {
				String[] temp = str.split(files.getFileSeparator());
				if(temp.length<files.getRowLength()-1) {
					continue;
				}
				if(temp[attr].length()!=18) {
					continue;
				}
				for(int i=0;i<tag.length;i++) {
					if(temp[attr].substring(fromP,toP).equals(tag[i])) {
						signals=true;
						break;
					}
				}
				if(signals) {
					writer.write(str+"\r\n");
				}
				signals=false;
			}
			reader.close();
			writer.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		fileAttribute[] files = new fileAttribute[12];
		File rootpath = new File("G:\\Train_Data-origin\\tl2014-2015");
		String outfile = "G:\\tl2014-2015\\";
		String[] filelist = rootpath.list();
		int counts=0;
		for(int i=0;i<filelist.length;i++) {
			if(filelist[i].contains("id_no_")&&(!filelist[i].contains("_err"))) {
				String filepath = rootpath.getAbsolutePath()+"\\"+filelist[i];
				System.out.println("read:"+filepath);
				files[counts] = new fileAttribute(filepath,"UTF-8",";","statistics_date;inner_code;office_no;window_no;ticket_no;id_kind;id_no;id_name;transmit_flag;sex;nation;birth;address;subscribe;valid_start_date;valid_stop_date;statist_date;others");
				fileFilterByPlaceContain(files[counts],outfile+filelist[i],6,"5101,5106,5107","UTF-8",0,4);
				counts++;
			}
		}
		*/
		
		fileAttribute[] files = new fileAttribute[12];
		File rootpath = new File("G:\\Train_Data-origin\\fileAdd");
		String[] filelist = rootpath.list();
		int counts=0;
		for(int i=0;i<filelist.length;i++) {
			if(filelist[i].contains(".csv")) {
				String filepath = rootpath.getAbsolutePath()+"\\"+filelist[i];
				System.out.println("read:"+filepath);
				files[counts] = new fileAttribute(filepath,"UTF-8",",","");
				fileFilterByPlaceContain(files[counts],6,"5107","UTF-8",0,4);
				counts++;
			}
		}
	}
}
