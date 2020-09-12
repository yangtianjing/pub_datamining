 package ageSex;
import java.io.*;

public class ageSexSta {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
    static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/agesexdata?characterEncoding=utf8&useSSL=false&serverTimezone=UTC&rewriteBatchedStatements=true";
     // ���ݿ���û��������룬��Ҫ�����Լ�������
    static final String USER = "root";
    static final String PASS = "password";
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
    public static int[] getAgeSexFromId(String dates,String id) {
    	int[] ageSex=new int[2];
    	String age = (String) id.subSequence(6, 14);
    	String sex = (String) id.subSequence(16, 17);
    	if(Integer.parseInt(sex)%2==1) {
    		ageSex[0]=1;//����Ϊ��
    	}else {
    		ageSex[0]=0;
    	}
    	String x=(String)age.substring(0, 4);
    	String y=(String)age.substring(4, 6);
    	String z=(String)age.substring(6);
    	if(Integer.parseInt(x)>2018||Integer.parseInt(x)<1800||
    			Integer.parseInt(y)>12||Integer.parseInt(y)<=0||
    			Integer.parseInt(z)>31||Integer.parseInt(z)<=0)
    		age="19800101";
    	//System.out.println(age);
    	String ages = age.substring(0, 4);
    	String ages1 = age.substring(4, 6);
    	int year=Integer.parseInt(dates.substring(0, 4))-Integer.parseInt(ages);
    	//System.out.println(dates.toString().substring(0, 4));
    	int mon = Integer.parseInt(dates.substring(4, 6))-Integer.parseInt(ages1);
    	if(mon<0) year=year-1;
    	ageSex[1]=year;
    	if((ageSex[0]!=1&&ageSex[0]!=0)||ageSex[1]<=0||ageSex[1]>=120) {
    		ageSex[0]=0;
    		ageSex[1]=0;
    	}
    	return ageSex;
    }
    
	public static void calAgeSex(String inpath,String outpath,String citycode) {
		try {
        	int[][][] ageSex=new int[4][2][120];//�洢�����Ա���Ϣ
    		for(int i=0;i<120;i++) {//��ʼ��
    			for(int j=0;j<4;j++) {
        			ageSex[j][0][i]=0;
        			ageSex[j][1][i]=0;
    			}
    		}//0��Ϊ��������1��ΪŮ������n�б�ʾn�������
    		
    		
    		int[] temp = new int[2];
    		File infile = new File(inpath);
    		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(infile),"UTF-8"));
			String str = "";
					
    		while((str=reader.readLine())!=null){
    			String[] strs = str.split(",");
    			if(strs[5].length()!=18||(!strs[5].substring(2, 4).equals(citycode))) {
        			continue;
        		}
    			temp=getAgeSexFromId(strs[0],strs[5]);
        		ageSex[Integer.parseInt(strs[0].substring(0, 4))-2014][temp[0]][temp[1]]++;
    		}
    		File[] outfile = new File[4];
    		
    		for(int j=0;j<4;j++) {
    			outfile[j] = new File(outpath+"_201"+(j+4)+".csv");
    			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile[j]),"UTF-8"));
        		double male=0,female=0;
        		for(int i=0;i<120;i++) {
                	male = male + ageSex[j][1][i];
                	female = female + ageSex[j][0][i];
                }
        		for(int i=0;i<120;i++) {
        			double m=0,f=0;
        			if(ageSex[j][1][i]!=0&&male!=0) {
        				m=ageSex[j][1][i]/male;
        			}
        			if(ageSex[j][0][i]!=0&&female!=0) {
        				f=ageSex[j][0][i]/female;
        			}
                	writer.write(i+","+f+","+m+"\r\n");
                }
        		writer.close();
    		}
    		
    		reader.close();
		}catch(Exception se){
            se.printStackTrace();
        }finally{
            
        }	
	}
	public static void calDis(String inpath,String outpath,String citycode) {
		try {
        	int[][][] ageSex=new int[4][2][120];//�洢�����Ա���Ϣ
    		for(int i=0;i<120;i++) {//��ʼ��
    			for(int j=0;j<4;j++) {
        			ageSex[j][0][i]=0;
        			ageSex[j][1][i]=0;
    			}
    		}//0��Ϊ��������1��ΪŮ������n�б�ʾn�������
    		
    		
    		int[] temp = new int[2];
    		File infile = new File(inpath);
    		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(infile),"UTF-8"));
			String str = "";
					
    		while((str=reader.readLine())!=null){
    			String[] strs = str.split(",");
    			if(strs[5].length()!=18||(!strs[5].substring(2, 4).equals(citycode))) {
        			continue;
        		}
    			temp=getAgeSexFromId(strs[0],strs[5]);
        		ageSex[Integer.parseInt(strs[0].substring(0, 4))-2014][temp[0]][temp[1]]+=Integer.parseInt(strs[25]);
    		}
    		File[] outfile = new File[4];
    		
    		for(int j=0;j<4;j++) {
    			outfile[j] = new File(outpath+"_201"+(j+4)+".csv");
    			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile[j]),"UTF-8"));
        		double male=0,female=0;
        		for(int i=0;i<120;i++) {
                	male = male + ageSex[j][1][i];
                	female = female + ageSex[j][0][i];
                }
        		for(int i=0;i<120;i++) {
        			double m=0,f=0;
        			if(ageSex[j][1][i]!=0&&male!=0) {
        				m=ageSex[j][1][i]/male;
        			}
        			if(ageSex[j][0][i]!=0&&female!=0) {
        				f=ageSex[j][0][i]/female;
        			}
                	writer.write(i+","+f+","+m+"\r\n");
                }
        		writer.close();
    		}
    		
    		reader.close();
		}catch(Exception se){
            se.printStackTrace();
        }finally{
            
        }	
	}
	public static void calPrice(String inpath,String outpath,String citycode) {
		try {
        	int[][][] ageSex=new int[4][2][120];//�洢�����Ա���Ϣ
    		for(int i=0;i<120;i++) {//��ʼ��
    			for(int j=0;j<4;j++) {
        			ageSex[j][0][i]=0;
        			ageSex[j][1][i]=0;
    			}
    		}//0��Ϊ��������1��ΪŮ������n�б�ʾn�������
    		
    		
    		int[] temp = new int[2];
    		File infile = new File(inpath);
    		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(infile),"UTF-8"));
			String str = "";
					
    		while((str=reader.readLine())!=null){
    			String[] strs = str.split(",");
    			if(strs[5].length()!=18||(!strs[5].substring(2, 4).equals(citycode))) {
        			continue;
        		}
    			temp=getAgeSexFromId(strs[0],strs[5]);
        		ageSex[Integer.parseInt(strs[0].substring(0, 4))-2014][temp[0]][temp[1]]+=Integer.parseInt(strs[26]);
    		}
    		File[] outfile = new File[4];
    		
    		for(int j=0;j<4;j++) {
    			outfile[j] = new File(outpath+"_201"+(j+4)+".csv");
    			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outfile[j]),"UTF-8"));
        		double male=0,female=0;
        		for(int i=0;i<120;i++) {
                	male = male + ageSex[j][1][i];
                	female = female + ageSex[j][0][i];
                }
        		for(int i=0;i<120;i++) {
        			double m=0,f=0;
        			if(ageSex[j][1][i]!=0&&male!=0) {
        				m=ageSex[j][1][i]/male;
        			}
        			if(ageSex[j][0][i]!=0&&female!=0) {
        				f=ageSex[j][0][i]/female;
        			}
                	writer.write(i+","+f+","+m+"\r\n");
                }
        		writer.close();
    		}
    		
    		reader.close();
		}catch(Exception se){
            se.printStackTrace();
        }finally{
            
        }	
	}
	public static void main(String[] args) {
		calAgeSex("G:\\Train_Data_highway\\refined\\dy_mig_origin_highway.csv","G:\\Train_Data_highway\\result\\dy_mig_origin_highway_time_06","06");
		calAgeSex("G:\\Train_Data_highway\\refined\\dy_mig_origin_highway.csv","G:\\Train_Data_highway\\result\\dy_mig_origin_highway_time_07","07");
		calDis("G:\\Train_Data_highway\\refined\\dy_mig_origin_highway.csv","G:\\Train_Data_highway\\result\\dy_mig_origin_highway_dis_06","06");
		calDis("G:\\Train_Data_highway\\refined\\dy_mig_origin_highway.csv","G:\\Train_Data_highway\\result\\dy_mig_origin_highway_dis_07","07");
		calPrice("G:\\Train_Data_highway\\refined\\dy_mig_origin_highway.csv","G:\\Train_Data_highway\\result\\dy_mig_origin_highway_price_06","06");
		calPrice("G:\\Train_Data_highway\\refined\\dy_mig_origin_highway.csv","G:\\Train_Data_highway\\result\\dy_mig_origin_highway_price_07","07");
	}
}
