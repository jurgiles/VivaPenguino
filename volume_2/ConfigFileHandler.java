import processing.core.*;
import java.util.HashMap;

public class ConfigFileHandler implements PConstants{
  PApplet parent;
  HashMap files = new HashMap();
  
  ConfigFileHandler(PApplet parent, String[] files){
    this.parent = parent;

     for(int i=0; i<files.length; ++i){
       String[] lines = parent.loadStrings(files[i]);
       this.files.put(files[i], lines);
       parent.println("Loaded " + files[i]);
     } 

  }
  
  String[] getContents(String file){
    if(files.containsKey(file)){
      parent.println("Getting content for " + file);
      return (String[]) files.get(file); 
    }else{
      return null;
    }
  }
  
  void setContents(String file, String[] strs){
    
  }
  
  void appendContents(String file, String str){
    
  }
}