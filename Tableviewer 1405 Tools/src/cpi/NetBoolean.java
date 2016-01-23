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
public class NetBoolean implements IMainTables  {
    public NetBoolean(String ptableName,String pbooleanKey){
    	Constructor(ptableName,pbooleanKey,false, "Net.Boolean");
    }
    public  NetBoolean(String ptableName,String pbooleanKey,boolean pDefault){
    	Constructor(ptableName,pbooleanKey,pDefault, "Net.Boolean");
    }
    public NetBoolean(String ptableName,String pbooleanKey,ID id){
    	Constructor(ptableName,pbooleanKey,false,id.Value());
    }
    public  NetBoolean(String ptableName,String pbooleanKey,boolean pDefault,ID id){
    	Constructor(ptableName,pbooleanKey,pDefault,id.Value());
    }
    private void Constructor(String ptableName,String pbooleanKey,boolean pDefaultValue,String id){
    	pId=id;
        booleanKey=pbooleanKey;
      defaultValue=pDefaultValue;
        tableName=ptableName;
        defCommPrefix=bCommPrefix;
        if(tableName.startsWith("/")){
        	defCommPrefix="";
        	tableName=tableName.substring(1);
        }
    	 table=NetworkTable.getTable(defCommPrefix+tableName);
    	 boolean temp=table.getBoolean(booleanKey,defaultValue);
          table.putBoolean(booleanKey, temp);
         // table.putString(booleanKey+".ID", id);
          table.addTableListener(booleanKey, new ITableListener(){
        	    public void valueChanged(ITable source, String key, Object pvalue, boolean isNew){
        	    	if(key.equals(Tables.Constants.IS_REFRESH_KEY)){
        	    	}
        	    	if(isLocked&&(oldValue!=(Boolean)pvalue)){
        	    		table.putBoolean(booleanKey, value);
        	    		oldValue=value;
        	    		return;
        	    	}
        	    value=(Boolean) pvalue;
	    		oldValue=value;
        	    }
          }, true);
 }
    public boolean Value(){
        return value;
    }
    public boolean Value(boolean pvalue){
    	boolean tmpLock=isLocked;
    	if(tmpLock)isLocked=false;
            if(pvalue!=value)table.putBoolean(booleanKey, pvalue);
        	if(tmpLock)isLocked=true;
        value = pvalue;
        return value;
    }
    public void addActionListner(ITableListener listener){
    	table.addTableListener(booleanKey,listener,true);
    }

    public void lock(){
    	isLocked=true;	
    }
    public void unlock(){
    	isLocked=false;	
    }
    boolean value;
    boolean oldValue;
   boolean defaultValue;
   NetworkTable table;
   String booleanKey;
   String tableName;
   String pId;
   String defCommPrefix;
   boolean isLocked=false;
   String prefix="Utilitiea/";
   cpi.NetString hcDestination;
}
