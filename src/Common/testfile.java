package Common;
import java.io.*;
public class testfile {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File f = new File("G:\\rs.csv");
		File f1 = new File("G:\\ds_rs.csv");
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f),"utf-8"));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f1),"utf-8"));
			String str = "";
			String tag = "",tag1="";
			double tag2 = 1.0;
			br.readLine();
			while((str = br.readLine())!=null) {
				String[] temp = str.split(",");
				//System.out.println(temp.length);
				if(tag.equals(temp[0].trim())) {
					bw.write(temp[0]+","+temp[1]+","+temp[2]+","+temp[3]+","+tag2*(1.0+Double.valueOf(temp[4]))+"\r\n");
					tag2 = tag2*(1.0+Double.valueOf(temp[4]));
				}else {
					System.out.println(temp[0]);
					tag=temp[0];
					tag2 = Double.valueOf(temp[2]);
					bw.write(temp[0]+","+temp[1]+","+temp[2]+","+temp[3]+","+tag2*(1.0+Double.valueOf(temp[4]))+"\r\n");
					tag2 = tag2*(1.0+Double.valueOf(temp[4]));
				}
			}
			br.close();
			bw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
