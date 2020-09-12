package CrossData;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import CrossData.fileAttribute;

public class fileAttrDelete {
	public static void deleteAttr(fileAttribute f,int[] attr) {
		try {
			File rspath = new File(f.getFolder()+"\\deleteRs");
			if(!rspath.exists()) {
				rspath.mkdir();
			}
			System.out.println("read:"+f.getFilePath());
			File outfile =new File(f.getFolder()+"\\deleteRs\\"+f.getFileName());
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(f.getFilePath())),"UTF-8"));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile),"UTF-8"));
			String str="";
			while((str=reader.readLine())!=null) {
				String[] temp = str.split(f.getFileSeparator());
				boolean b = true;
				for(int i=0;i<temp.length;i++) {
					for(int j=0;j<attr.length;j++) {
						if(i==attr[j]) {
							b=false;
							break;
						}
					}
					if(b) {
						writer.write(temp[i]);
						if(i!=temp.length-1) {
							writer.write(",");
						}
					}
					b=true;
				}
				writer.write("\r\n");
			}
			reader.close();
			writer.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public static String getnum(int i) {
    	String str="";
    	if(i<10) {
    		str="20140"+i;
    	}else if(i<13) {
    		str="2014"+i;
    	}else if(i<16) {
    		str="20150"+(i-12);
    	}
    	return str;
    }
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] attr = {0,1,2,3,9,10,24,29};
		for(int i=4;i<16;i++) {
			fileAttribute f = new fileAttribute("G:\\Train_Data-origin\\id_sale_"+getnum(i)+".csv",",");
			deleteAttr(f,attr);
		}
		
	}

}
