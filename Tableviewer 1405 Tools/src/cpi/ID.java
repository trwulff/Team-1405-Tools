package cpi;

public class ID {
	public ID(){
		
	}
	public ID(String id){
		pId=id;
	}
	public String Value(){
		return pId;
	}
	public String Value(String id){
		pId=id;
		return pId;
	}
String pId;
}
