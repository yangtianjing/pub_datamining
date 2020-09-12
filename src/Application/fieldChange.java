package Application;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Color;
import javax.swing.JTextField;
import CrossData.fileAttribute;
import java.awt.Font;
import Common.readfilelines;

public class fieldChange extends JFrame {

	private JPanel contentPane;
	private JTable tb1;
	private JFileChooser jc;
	private JButton bt_fc_1;
	private JTextField tf_fc_3;
	private File selFile;
	private fileAttribute fileattr;
	private JTextField tf_fc_1;
	private JTextField tf_fc_2;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					fieldChange frame = new fieldChange();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public fieldChange() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 906, 609);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		jc = new JFileChooser();
		jc.setBounds(0, 0, 600, 600);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("csv Files","csv");
		jc.addChoosableFileFilter(filter);
		jc.setCurrentDirectory(new File("D:"));
		bt_fc_1 = new JButton("\u6D4F\u89C8");
		bt_fc_1.setFont(new Font("楷体", Font.BOLD, 16));
		bt_fc_1.setBackground(Color.LIGHT_GRAY);
		bt_fc_1.setBounds(750, 530, 68, 30);
		bt_fc_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int i = jc.showOpenDialog(getContentPane());
				if(i == JFileChooser.APPROVE_OPTION) {
					selFile = jc.getSelectedFile();
					try {
						fileattr = new fileAttribute(selFile.getParent()+"\\"+selFile.getName().replaceAll(".csv",".json"));
						//System.out.println("gotFile");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} 
					tf_fc_3.setText(selFile.getAbsolutePath());
				}
			}
		});
		contentPane.add(bt_fc_1);
		
		tf_fc_3 = new JTextField();
		tf_fc_3.setEditable(false);
		tf_fc_3.setBackground(Color.WHITE);
		tf_fc_3.setBounds(181, 535, 559, 21);
		contentPane.add(tf_fc_3);
		tf_fc_3.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("ReadLine:From");
		lblNewLabel.setEnabled(false);
		lblNewLabel.setBounds(181, 506, 96, 15);
		contentPane.add(lblNewLabel);
		
		tf_fc_1 = new JTextField();
		tf_fc_1.setBounds(266, 504, 66, 21);
		contentPane.add(tf_fc_1);
		tf_fc_1.setColumns(10);
		
		tf_fc_2 = new JTextField();
		tf_fc_2.setBounds(371, 504, 66, 21);
		contentPane.add(tf_fc_2);
		tf_fc_2.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("to");
		lblNewLabel_1.setBounds(344, 506, 24, 15);
		contentPane.add(lblNewLabel_1);
		
		JButton bt_fc_2 = new JButton("\u8BFB\u53D6");
		bt_fc_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(tf_fc_3.getText().equals("")) {
						JOptionPane.showMessageDialog(null, "请先选择文件");
						return;
					}
					
					String[][] tbdata;
					if(tf_fc_1.getText().equals("")&&tf_fc_2.getText().equals("")) {
						BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(selFile),fileattr.getEncode()));
						int counts=0;
						tbdata = new String[10][fileattr.getFieldList().length];
						String str="";
						while((str=br.readLine())!=null) {
							if(counts==10) {
								break;
							}
							tbdata[counts] = str.split(fileattr.getFileSeparator());
							counts++;
						}
						br.close();
					}else if(tf_fc_2.getText().equals("")){
						BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(selFile),fileattr.getEncode()));
						int counts=0;
						tbdata = new String[Integer.parseInt(tf_fc_1.getText())][fileattr.getFieldList().length];
						String str="";
						while((str=br.readLine())!=null) {
							if(counts==Integer.parseInt(tf_fc_1.getText())) {
								break;
							}
							tbdata[counts] = str.split(fileattr.getFileSeparator());
							counts++;
						}
						br.close();
					}else if(tf_fc_1.getText().equals("")) {
						if(Integer.parseInt(tf_fc_2.getText())<0||0>Integer.parseInt(tf_fc_2.getText())){
							JOptionPane.showMessageDialog(null, "不可为负数且起始行要小于结束行");
							return;
						}
						if(Integer.parseInt(tf_fc_2.getText())>15) {
							JOptionPane.showMessageDialog(null, "请不要显示超过15行数据");
							return;
						}
						BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(selFile),fileattr.getEncode()));
						int counts=0;
						tbdata = new String[Integer.parseInt(tf_fc_2.getText())][fileattr.getFieldList().length];
						String str="";
						while((str=br.readLine())!=null) {
							if(counts==Integer.parseInt(tf_fc_2.getText())) {
								break;
							}
							tbdata[counts] = str.split(fileattr.getFileSeparator());
							counts++;
						}
						br.close();
					}else {
						if(Integer.parseInt(tf_fc_2.getText())<0||Integer.parseInt(tf_fc_1.getText())<0||Integer.parseInt(tf_fc_1.getText())>Integer.parseInt(tf_fc_2.getText())){
							JOptionPane.showMessageDialog(null, "不可为负数且起始行要小于结束行");
							return;
						}
						if(Integer.parseInt(tf_fc_2.getText())-Integer.parseInt(tf_fc_1.getText())>15) {
							JOptionPane.showMessageDialog(null, "请不要显示超过15行数据");
							return;
						}
						BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(selFile),fileattr.getEncode()));
						int counts=0;
						tbdata = new String[Integer.parseInt(tf_fc_2.getText())-Integer.parseInt(tf_fc_1.getText())][fileattr.getFieldList().length];
						String str="";
						while((str=br.readLine())!=null) {
							if(counts>Integer.parseInt(tf_fc_2.getText())) {
								break;
							}
							if(counts<Integer.parseInt(tf_fc_1.getText()))
								continue;
							if(counts>=Integer.parseInt(tf_fc_1.getText()))
								tbdata[counts] = str.split(fileattr.getFileSeparator());
							counts++;
						}
						br.close();
					}
					Object[] tbhead = fileattr.getFieldList();
					Object[][] tbd = tbdata;
					tb1 = new JTable(tbd,tbhead);
					tb1.setBounds(100, 100, 800, 500);
					tb1.setBackground(Color.white);
					contentPane.add(tb1);
					contentPane.repaint();
				}catch(Exception e1) {
					e1.printStackTrace();
					return;
				}
				
			}
		});
		bt_fc_2.setFont(new Font("宋体", Font.BOLD, 12));
		bt_fc_2.setBounds(457, 503, 57, 23);
		contentPane.add(bt_fc_2);
	}
}
