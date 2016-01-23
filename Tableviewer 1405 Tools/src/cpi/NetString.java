/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpi;

import Tables.IMainTables;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.*;


/**
 *
 * @author Team 1405
 */
public class NetString  implements IMainTables {
    public NetString(String ptableName,String pbooleanKey){
    	Constructor(ptableName,pbooleanKey,"","Net.String");
    }
    public  NetString(String ptableName,String pbooleanKey,String pDefault){
    	Constructor(ptableName,pbooleanKey,pDefault,"Net.String");
    }
    public NetString(String ptableName,String pbooleanKey,ID id){
    	Constructor(ptableName,pbooleanKey,"",id.Value());
    }
    public  NetString(String ptableName,String pbooleanKey,String pDefault,ID id){
    	Constructor(ptableName,pbooleanKey,pDefault,id.Value());
    }
    private void Constructor(String ptableName,String pStringKey,String pDefaultValue,String id){
    	pId=id;
    	stringKey=pStringKey;
      defaultValue=pDefaultValue;
        tableName=ptableName;
        defCommPrefix=sCommPrefix;
        if(tableName.startsWith("/")){
        	defCommPrefix="";
        	tableName=tableName.substring(1);
        }
    	 table=NetworkTable.getTable(defCommPrefix+tableName);
          table.putString(stringKey, defaultValue);
         // table.putString(stringKey+".ID", id);
          table.addTableListener(stringKey, new ITableListener(){
        	    public void valueChanged(ITable source, String key, Object pvalue, boolean isNew){
            	    	if(isLocked&&
            	    			(pvalue!=null)&&
            	    			!(oldValue.equals((String)pvalue))){
            	    		table.putString(stringKey, value);
            	    		oldValue=value;
            	    		return;
            	    	}
        	    value=(String)pvalue;
	    		oldValue=value;
        	    }
          }, true);
 }
    public String Value(){
        return value;
    }
    public String Value(String pvalue){
    	boolean tmpLock=isLocked;
    	if(tmpLock)isLocked=false;
            if(!pvalue.equals(value))table.putString(stringKey, pvalue);
        	if(tmpLock)isLocked=true;
        value = pvalue;
        return value;
    }
    public void addActionListner(ITableListener listener){
    	table.addTableListener(stringKey,listener,true);
    }

    public void lock(){
    	isLocked=true;	
    }
    public void unlock(){
    	isLocked=false;	
    }
    String value="";
    String oldValue="";
   String defaultValue;
   NetworkTable table;
   String stringKey;
   String tableName;
   String pId;
   String defCommPrefix;
   boolean isLocked=false;
}
