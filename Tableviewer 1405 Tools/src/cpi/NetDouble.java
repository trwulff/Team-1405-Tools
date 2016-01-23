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
public class NetDouble implements IMainTables  {
    public NetDouble(String ptableName,String pbooleanKey){
    	Constructor(ptableName,pbooleanKey,0.0,"Net.Number");
    }
    public  NetDouble(String ptableName,String pbooleanKey,double pDefault){
    	Constructor(ptableName,pbooleanKey,pDefault,"Net.Number");
    }
        public NetDouble(String ptableName,String pbooleanKey,ID id){
        	Constructor(ptableName,pbooleanKey,0.0,id.Value());
        }
        public  NetDouble(String ptableName,String pbooleanKey,double pDefault,ID id){
        	Constructor(ptableName,pbooleanKey,pDefault,id.Value());
    }
    private void Constructor(String ptableName,String pbooleanKey,double pDefaultValue,String id){
    	pId=id;
    	sensitivity=0.05;
    	numberKey=pbooleanKey;
      defaultValue=pDefaultValue;
        tableName=ptableName;
        defCommPrefix=dCommPrefix;
        if(tableName.startsWith("/")){
        	defCommPrefix="";
        	tableName=tableName.substring(1);
        }
    	 table=NetworkTable.getTable(defCommPrefix+tableName);
          table.putNumber(numberKey, defaultValue);
          //table.putString(numberKey+".ID", id);
          table.addTableListener(numberKey, new ITableListener(){
        	    public void valueChanged(ITable source, String key, Object pvalue, boolean isNew){
        	    	if(isLocked&&(oldValue!=(Double)pvalue)){
        	    		table.putNumber(numberKey, value);
        	    		oldValue=value;
        	    		return;
        	    	}
        	    value=(Double)pvalue;
	    		oldValue=value;
        	    }
          }, true);
 }
    public double Value(){
        return value;
    }
    public double Value(double pvalue){
    	double abs=pvalue-value;if(abs<0.0)abs=-abs;

    	boolean tmpLock=isLocked;
    	if(tmpLock)isLocked=false;
        if(abs>sensitivity)table.putNumber(numberKey, pvalue);
    	if(tmpLock)isLocked=true;
        value = pvalue;
        return value;
    }
    public void setSensitivity(double pvalue){
    	sensitivity=pvalue;
    	if(sensitivity<0.0)sensitivity=-sensitivity;
    }
    
    public double getSensitivity(){
    	return sensitivity;
    }
    public void addActionListner(ITableListener listener){
    	table.addTableListener(numberKey,listener,true);
    }

    public void lock(){
    	isLocked=true;	
    }
    public void unlock(){
    	isLocked=false;	
    }
    double value;
    double oldValue;
   double defaultValue;
   double sensitivity;
   NetworkTable table;
   String numberKey;
   String tableName;
   String pId;
   String defCommPrefix;
   boolean isLocked=false;
}
