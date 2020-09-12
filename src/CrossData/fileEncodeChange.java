package CrossData;
import java.io.*;
import CrossData.fileAttribute;
public class fileEncodeChange {
	public static void encodeChange(fileAttribute f ,String outfile,String[] encode,String[] separator,String fieldsStr) {
		try {
			File f1 = new File(f.getFilePath());
			File f2 = new File(outfile);
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f1),encode[0]));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f2),encode[1]));
			String str="";
			int counts=0;
			while((str=reader.readLine())!=null) {
				if(counts==0) {
					str= str.replaceAll(fieldsStr, "");
				}
				
				if(str.split(f.getFileSeparator()).length!=f.getRowLength()) {
					continue;
				}
				writer.write(str.replaceAll(separator[0], separator[1])+"\r\n");
			}
			reader.close();
			writer.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	public static void deleteSymbol(fileAttribute f,String symbol) {
		try {
			File f1 = new File(f.getFilePath());
			File f2 = new File(f.getFolder()+"\\deleteSymbol");
			if(!f2.exists()) {
				f2.mkdir();
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f1),f.getEncode()));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f2.getAbsolutePath()+"\\"+f.getFileName()),f.getEncode()));
			String str="";
			int counts=0;
			while((str=reader.readLine())!=null) {
				String[] temp = str.split(f.getFileSeparator());
				if(temp.length!=f.getRowLength()) {
					continue;
				}
				for(int i=0;i<temp.length;i++) {
					temp[i]=temp[i].replaceAll(f.getFileSeparator(),"").replaceAll(symbol, "").trim();
					//System.out.println(temp[i]);
					writer.write(temp[i]);
					if(i!=temp.length-1) {
						writer.write(f.getFileSeparator());
					}else {
						writer.write("\r\n");
					}
				}
			}
			reader.close();
			writer.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		String fieldsStr = "statistics_date;inner_code;office_no;window_no;ticket_no;id_kind;id_no;id_name;transmit_flag;sex;nation;birth;address;subscribe;valid_start_date;valid_stop_date;statist_date;others";
		String[] encode = new String[2];
		encode[0]="GBK";
		encode[1]="UTF-8";
		String[] separator = new String[2];
		separator[0] = ",";
		separator[1] = ";";
		fileAttribute f =  new fileAttribute("G:\\workplace\\id_no_201409.csv","GBK",",",fieldsStr);
		encodeChange(f,"G:\\workplace\\id_no_201409_change.csv",encode,separator,fieldsStr);
		*/
		File rootpath = new File("G:\\Train_Data-origin\\tl2015-2017");
		String[] filelist = rootpath.list();
		for(int i=0;i<filelist.length;i++) {
			if(filelist[i].contains("dy_mig")) {
				continue;
			}
			File temp = new File(rootpath.getAbsolutePath()+"\\"+filelist[i]);
			if(temp.isDirectory()){
				continue;
			}
			String files =rootpath+"\\"+filelist[i];
			System.out.println(files);
			fileAttribute f = new fileAttribute(files,",");
			deleteSymbol(f,"\"");
		}
	}

}
