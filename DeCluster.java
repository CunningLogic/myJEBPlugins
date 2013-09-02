//? name=DeCluster v1.0, help=This Java file is a JEB plugin

import jeb.api.IScript;
import jeb.api.JebInstance;
import jeb.api.dex.Dex;
import java.util.ArrayList; 

public class DeCluster implements IScript {

    @SuppressWarnings("unchecked")
    public void run(JebInstance jeb) {
        jeb.print("DeCluster Plugin v1.0");
        jeb.print("By jcase@cunninglogic.com");
        String classPre = "_JEBClass";
        String fieldPre = "_JEBfield";
        String methodPre = "_JEBMethod";

        int count = 0;

        if  (!jeb.isFileLoaded()) {

            jeb.print("Please load a dex/apk/jar file");

        } else {
            
            jeb.print("Renaming fields"); 
            ArrayList<String> myArr = jeb.getDex().getFieldSignatures(true);
            for (int i = myArr.size()-1; i >= 0; i--) { 
                String fieldName = myArr.get(i);

                if (!isValid(fieldName.substring(fieldName.indexOf(">")+1, fieldName.indexOf(":")))) {
                    ++count;

                
                    if(!jeb.setFieldComment(fieldName, "Renamed from " +fieldName)) {
                        jeb.print("Error commenting field " + fieldName);
                    }
                    if(!jeb.renameField(fieldName,fieldPre + Integer.toString(count))) {
                        jeb.print("Error renaming field " + fieldName);
                    }

                }
                
            }

            count = 0;
            myArr.clear();

            jeb.print("Renaming methods"); 
            myArr = jeb.getDex().getMethodSignatures(true);
             for (int i = myArr.size()-1; i >= 0; i--) { 
                String methodName = myArr.get(i);

                if (!isValid(methodName.substring(methodName.indexOf(">")+1, methodName.indexOf("(")))) {
                  
                        ++count;

                        /* For some reason commenting on some methods throws null pointers for me

                        if(!jeb.setMethodComment(methodName, "Renamed from " +methodName)) {
                            jeb.print("Error commenting method " + methodName);
                        }*/
                        if(!jeb.renameMethod(methodName,methodPre + Integer.toString(count))) {
                            jeb.print("Error renaming method " + methodName);
                        }

                }
                
            }

            count = 0;
            myArr.clear();

            jeb.print("Renaming classes"); 
            myArr = jeb.getDex().getClassSignatures(true);
             for (int i = myArr.size()-1; i >= 0; i--) { 
                String className = myArr.get(i);

                if (!isValid(className.substring(className.lastIndexOf("/")+1, className.length()-1))) {
                    ++count;

                
                    if(!jeb.setClassComment(className, "Renamed from " +className)) {
                        jeb.print("Error commenting class " + className);
                    }
                    if(!jeb.renameClass(className,classPre + Integer.toString(count))) {
                        jeb.print("Error renaming class " + className);
                    }

                }
                
            }

        }

    }

    public boolean isValid (String className){
        // This needs 
        if (className == null || className.length() == 0 || className.contains("<init>") || className.contains("<clinit>") )
            return true;
        return className.matches("\\w+");
    }

}
