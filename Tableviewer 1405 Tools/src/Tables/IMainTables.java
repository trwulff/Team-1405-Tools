package Tables;

import edu.wpi.first.wpilibj.networktables.*;
import edu.wpi.first.wpilibj.tables.*;

public interface IMainTables {
	
	String bCommPrefix="Communications/Boolean/";
	String dCommPrefix="Communications/Double/";
	String sCommPrefix="Communications/String/";
	
/*
	NetworkTable commBooleanTable=NetworkTable.getTable(bCommPrefix); */
	//NetworkTable commdoubleTable=NetworkTable.getTable(dCommPrefix);
	//NetworkTable commStringTable=NetworkTable.getTable(sCommPrefix);
	
	String bSettingsPrefix="Settings/Boolean/";
	String dSettingsPrefix="Settings/Double/";
	String sSettingsPrefix="Settings/String/";

/*
	NetworkTable settingsTableBoolean=NetworkTable.getTable(bSettingsPrefix);
	NetworkTable settingsTableDouble=NetworkTable.getTable(dSettingsPrefix);
	NetworkTable settingsTableString=NetworkTable.getTable(sSettingsPrefix);
/*
	NetworkTable commTable=NetworkTable.getTable("Communications");
	ITable  commBooleanTable=commTable.getSubTable("Boolean");
	ITable commdoubleTable=commTable.getSubTable("Double");
	ITable commStringTable=commTable.getSubTable("String");
	NetworkTable settingsTable=NetworkTable.getTable("Settings");
	ITable  settingsTableBoolean=settingsTable.getSubTable("Boolean");
	String bSettingsPrefix="Settings/Boolean/";
	ITable settingsTableDouble=settingsTable.getSubTable("Double");
	String dSettingsPrefix="Settings/Double/";
	ITable settingsTableString=settingsTable.getSubTable("String");
	String sSettingsPrefix="Settings/String/";
	NetworkTable interfaceTable=NetworkTable.getTable("Interfaces");
	ITable interfaceInput=interfaceTable.getSubTable("Inputs");
	ITable interfaceInputBoolean=interfaceInput.getSubTable("Boolean");
	String bInterfacesInputsPrefix="Interfaces/Inputs/Boolean/";
	ITable interfaceInputDouble=interfaceInput.getSubTable("Double");
	String dInterfacesInputsPrefix="Interfaces/Inputs/Double/";
	ITable interfaceInputString=interfaceInput.getSubTable("String");
	String sInterfacesInputsPrefix="Interfaces/Inputs/String/";
	ITable interfaceOutput=interfaceTable.getSubTable("Outputs");
	ITable interfaceOuputBoolean=interfaceOutput.getSubTable("Boolean");
	String bInterfacesOutputsPrefix="Interfaces/Outputs/Boolean/";
	ITable interfaceOuputDouble=interfaceOutput.getSubTable("Double");
	String dInterfacesOutputsPrefix="Interfaces/Inputs/Double/";
	ITable interfaceOuputString=interfaceOutput.getSubTable("String");
	String sInterfacesOutputsPrefix="Interfaces/Inputs/String/";
	\*/
}