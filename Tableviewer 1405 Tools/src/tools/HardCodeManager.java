package tools;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import cpi.NetBoolean;
import cpi.NetString;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.Preferences;
import edu.wpi.first.wpilibj.tables.ITableListener;

public class HardCodeManager {
	
	public HardCodeManager(String pPrefix,Preferences pref){
		prefix=pPrefix;
		tvPref=pref;
		isRefreshKeylist=new NetBoolean("/cpi.Preferences","Refresh Key List",false);
		keylist =new NetString("/cpi.Preferences","Key List","");
		hcCopyPaste= new cpi.NetBoolean(prefix+"Hard Code Manager", "Copy-Paste",false);
		hcCopyPaste.addActionListner(new ITableListener(){
    	    public void valueChanged(ITable source, String key, Object pvalue, boolean isNew){
    	    	if((Boolean)pvalue)hcCopyPaste();
	    }
			
		});
		hcDestination=new cpi.NetString(prefix+"Hard Code Manager", "Destination File", tvPref.get("hardcodelocation", "<none>"));
		hcDestination.lock();
		hcGetDestination=new cpi.NetBoolean(prefix+"Hard Code Manager", "Get Destination File", false);
		hcGetDestination.addActionListner(new ITableListener(){
    	    public void valueChanged(ITable source, String key, Object pvalue, boolean isNew){
    	    	if((Boolean)pvalue)hcGetDestinationAction();
	    }
		});
		hcGeneateCode=new cpi.NetBoolean(prefix+"Hard Code Manager", "Generate Hard Code", false);
		hcGeneateCode.Value(false);
		hcGeneateCode.addActionListner(new ITableListener(){
    	    public void valueChanged(ITable source, String key, Object pvalue, boolean isNew){
    	    	if((Boolean)pvalue)hcGeneateCodeAction();
	    }
		});		
		timer = new Timer();
		timer.schedule(new TimerTask(){
			public void run(){
				
				if(hcGeneateCodeActionFlag&&(!isRefreshKeylist.Value())){
					hcGeneateCodeAction2();
				}
					
			}
			
		},0, 100);
	}

	void hcGeneateCodeAction(){
		hcframe.setVisible(true);
		hcframe.setBounds(500, 200, 100,60);
		JLabel label=new JLabel("Generating Hard Code");
		label.setHorizontalAlignment(0);
		hcframe.getContentPane().add(label);
		hcframe.setAlwaysOnTop(true);
		//GEN-FIRST:event_SaveToFileActionPerformed
        
        // Open Destination file 
		String filePath=tvPref.get("hardcodelocation", "<none>");
		if(filePath.equals("<none>"))return;
		hcBackup();
        File file=new File(filePath);
        // Overwrite file
        try{
            bufferedWriter= new BufferedWriter(new FileWriter(file));
        }
        catch (Exception ex){
            JOptionPane.showMessageDialog(hcframe, "Bufferd Writer Exception", "Save Settings to File Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        

        String temp;
        String temp2="";
        
        // Write HeaderDate dNow = new Date( );
        Date dNow = new Date( );
        SimpleDateFormat ft = 
        	      new SimpleDateFormat ("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
        	temp2=" Generated by Hard Code Manager\n//"+ ft.format(dNow);
        try{
        	bufferedWriter.write("//"+temp2+"\n");
        	bufferedWriter.write("package cpi.Hard.Code;\n\n");
        	bufferedWriter.write("import java.util.Hashtable;\n");
        	bufferedWriter.write("public class ConstantArray {\n\n");
        	bufferedWriter.write("public static void initialize(){\n");
        	bufferedWriter.write("PrintDate();\n");
        	bufferedWriter.write("//Map.put(key, value); key and value are both strings\n");
        }
        catch (Exception ex){
            JOptionPane.showMessageDialog(hcframe, "Bufferd Writer Exception", "Save Header to File Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
       
        isRefreshKeylist.Value(true);
        hcGeneateCodeActionFlag=true;
	}
	void hcGeneateCodeAction2(){
        hcGeneateCodeActionFlag=false;
        String temp;
        String temp2="";
        int initialIndex=0;
        int nextIndex=0;
        String ptemp="";
        ptemp=keylist.Value();
        System.out.println(ptemp);
        // load connected IDs and ID List
        NetworkTable prefTable=NetworkTable.getTable("Preferences");
        int xtmp;
        xtmp=ptemp.indexOf("\n",initialIndex);
        while((nextIndex=ptemp.indexOf("\n",initialIndex))>0){
            //Get key
            temp=ptemp.substring(initialIndex, nextIndex);
            if(temp.indexOf(" ")<0){ // Skip if spaces are in key
            // save Key\n to file
            try{
                bufferedWriter.write("Map.put(\""+temp+"\",\"");
            }
            catch (Exception ex){
                System.out.println(ex);
            }
            // get Value from Preferences
                String value=prefTable.getString(temp);
            try{
                bufferedWriter.write(value+"\");\n");
            }
            catch (Exception ex){
                System.out.println(ex);
            }
            }  

            initialIndex= nextIndex+1;
        }
        // Write footer

    	try{
    		bufferedWriter.write("\n}\n\npublic static void PrintDate(){\n");
    		bufferedWriter.write("	System.out.println(\" Hard Settings "+temp2+"\");\n}\n");
    		bufferedWriter.write("\npublic static Hashtable<java.lang.String,java.lang.String> Map=new Hashtable<java.lang.String,java.lang.String>();\n}");
    	}
        catch (Exception ex){
            JOptionPane.showMessageDialog(hcframe, "Bufferd Writer Exception", "Save Footer to File Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        //flush and close file
            try{
                bufferedWriter.flush();
            bufferedWriter.close();
            }
            catch (java.io.IOException ex){
                System.out.println(ex);
            }
        
    //GEN-LAST:event_SaveToFileActionPerformed
            hcframe.dispose();
    		hcGeneateCode.Value(false);

	}
	void hcGetDestinationAction(){
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Open Hard Code File");
		String defaultDir=tvPref.get("hardcodelocationdirectory", ".");
		fc.setCurrentDirectory(new File(defaultDir));
        fc.setMultiSelectionEnabled(false);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int retVal = fc.showOpenDialog(hcframe);
        if (retVal == JFileChooser.APPROVE_OPTION) {
        	File file=fc.getSelectedFile();
        	String hardCodeLocation= file.getAbsolutePath() ;

        	tvPref.put("hardcodelocation", hardCodeLocation);	
        	tvPref.put("hardcodelocationdirectory", (fc.getCurrentDirectory()).toString());
        	
        }
        //frame.dispose();
        hcDestination.Value(tvPref.get("hardcodelocation", "<none>"));
        hcGetDestination.Value(false);
        }
	
	void hcCopyPaste(){
		String filePath=tvPref.get("hardcodelocation", "<none>");
		if(filePath.equals("<none>"))return;
		hcBackup();
		
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Select Copy File");
		String defaultDir=tvPref.get("hardcodelocationdirectory", ".");
		fc.setCurrentDirectory(new File(defaultDir));
        fc.setMultiSelectionEnabled(false);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int retVal = fc.showOpenDialog(hcframe);
        String sourceFile;
        if (retVal == JFileChooser.APPROVE_OPTION) {
        	File srcfile=fc.getSelectedFile();
        	sourceFile= srcfile.getAbsolutePath() ;
            File file=new File(filePath);
		      try{
		    	  Files.delete(file.toPath());
		    	  Files.copy(srcfile.toPath(), file.toPath());
		      }
		      catch(IOException e){
		    	  System.out.println( e);
		      }
        	
        }
        hcCopyPaste.Value(false);
		
	}
	
	void hcBackup(){
		String filePath=tvPref.get("hardcodelocation", "<none>");
        File file=new File(filePath);
        if(file==null)return;
		String  thisLine = null;
		BufferedReader br;
			      try{
			         // open input stream test.txt for reading purpose.
			         br = new BufferedReader(new FileReader(file));
			         thisLine = br.readLine(); 
			         thisLine = br.readLine(); 
			         br.close();
			      }catch(Exception e){
			         e.printStackTrace();
			      }
			      if(thisLine==null)return;
			      thisLine = "."+thisLine.substring(6);
			      thisLine=thisLine.replace(":", "_");
			      File fileTo=new File(filePath+thisLine);
			      if(!fileTo.exists()){
			      try{
			    	  Files.copy(file.toPath(), fileTo.toPath());
			      }
			      catch(IOException e){
			    	  System.out.println( e);
			      }
			    }
		
	}

	 boolean hcGeneateCodeActionFlag=false;
	 boolean hcReady=false;
	 cpi.NetString hcDestination;
	 cpi.NetBoolean hcGetDestination;
	 cpi.NetBoolean hcGeneateCode;
	 cpi.NetBoolean hcCopyPaste;
		JFrame hcframe=new JFrame();
	 	BufferedWriter bufferedWriter;
	 	BufferedReader bufferedReader;
		 NetBoolean isRefreshKeylist;
		 static NetString keylist;
		 Preferences tvPref;
		 String prefix;
		 Timer timer;
}
