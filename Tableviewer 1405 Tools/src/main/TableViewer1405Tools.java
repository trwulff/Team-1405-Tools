package main;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.Preferences;
import java.awt.*;

import edu.wpi.first.wpilibj.networktables.*;
import edu.wpi.first.wpilibj.tables.*;

import javax.swing.*;

import cpi.NetBoolean;
import cpi.NetString;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.LineBorder;



public class TableViewer1405Tools extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) { 
		if(args.length>0){
			if(args[0].equals("help")){
				System.out.println("TableViewer 1405 only supports one running instance.");
				System.out.println("\n-reset  Resets the path to TableViewer to <none>\n        and the isRuning flag to false.");
				System.out.println("\n-rf  Resets  the isRuning flag to false.");
				
			}
			if(args[0].equals("-reset")){
				tvPref.putBoolean("isrunning", false);
            	tvPref.put("tableviewerlocation", "<none>");	
			}
			if(args[0].equals("-rf")){
				tvPref.putBoolean("isrunning", false);
			}
			return;
		}
		if(tvPref.getBoolean("isrunning", false)){
			System.out.println("TableVieer1405 is currently runnung");
			return;
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				System.out.println("run()");
				try {
					frame = new TableViewer1405Tools();
				} catch (Exception e) {
					e.printStackTrace();
				}
				tvPref.putBoolean("isrunning", true);
			}
		});
		  
	}

	/**
	 * Create the frame.
	 */
	public TableViewer1405Tools() {
		setTitle("Tableviewer 1405 Tools");
		setVisible(true);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				exit();
			}
			@Override
			public void windowClosing(WindowEvent arg0) {
				exit();
			}
		});
		String tableviewerLocation=tvPref.get("tableviewerlocation", "<none>");
		if(!(new File(tableviewerLocation)).exists()){
			JFileChooser fc = new JFileChooser();
			fc.setDialogTitle("Link TableViewer");
			fc.setApproveButtonText("Link");
			String defaultDir=tvPref.get("tableviewerlocationdirectory", ".");
			fc.setCurrentDirectory(new File(defaultDir));
	        fc.setMultiSelectionEnabled(false);
	        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
	        int retVal = fc.showOpenDialog(frame);
            if (retVal == JFileChooser.APPROVE_OPTION) {
            	File file=fc.getSelectedFile();
            	tableviewerLocation= file.getAbsolutePath() ;

            	tvPref.put("tableviewerlocation", tableviewerLocation);	
            	tvPref.put("tableviewerlocationDirectory", (fc.getCurrentDirectory()).toString());
            	
            }
            else
            {
            	exit();
            }
		}
/*		
		try{
			//process=Runtime.getRuntime().exec("java -jar "+tableviewerLocation);
			process=Runtime.getRuntime().exec("java -jar "+ tableviewerLocation);
//			process=Runtime.getRuntime().exec("java -jar C:/Users/Team 1405 - 005/sunspotfrcsdk/tools/TableViewer-ce9796aa332842b8b05fa7f8796c82b38420010c.jar");
			System.out.println(process.isAlive());
			
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		*/
		timer = new Timer();
		timer.schedule(new TimerTask(){
			public void run(){
//				if(!process.isAlive())exit();
				
				// Network Table Setup

				Preferences tempPref=Preferences.userRoot().node("/edu/wpi/first/table_viewer");
				//End Network Table Setup
				String urlName=tempPref.get("host", "<none>");
				if((!isTableSet)&&(!urlName.equals("<none>"))){
					

					try{
					URL u = new URL("http://"+urlName+"/"); 
				    HttpURLConnection huc =  (HttpURLConnection)  u.openConnection(); 
				    huc.setRequestMethod("GET"); 
				    huc.connect(); 
				    huc.getResponseCode();
				    String IP=u.getHost();
				    try{
				    	initializationCount++;
				    NetworkTable.setClientMode();
				    NetworkTable.setIPAddress(IP);
				    }
				    catch(Exception e){
				    	System.out.println("Network Table Initialization Error ("+initializationCount+")\n"+e);
				    }
				    isTableSet=true;
	        		hcManager= new tools.HardCodeManager(prefix, tvPref);
					}
					catch (MalformedURLException e) { 
						System.out.println("Malformed URL string");
					} 
					catch (IOException e) {   
						System.out.println("Connection failed");
					}
					
				}
					
			}
			
		},100, 100);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setState(ICONIFIED);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}
	
	void exit(){
		tvPref.putBoolean("isrunning", false);
//		process.destroy();
		System.exit(0);
	}

	
//	 Process process;
	 Timer timer;
	 boolean isTableSet=false;
	 boolean isTFirstSet=true;
	 static TableViewer1405Tools frame;
	 static Preferences tvPref=Preferences.userRoot().node("/1405/tableviewer1405");
	 String prefix="/Tools/";
	 int initializationCount=0;
	 tools.HardCodeManager hcManager;
}
