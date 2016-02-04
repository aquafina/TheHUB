package view.nishat.net.Helper;

import java.io.BufferedWriter;
import java.io.File;

import java.io.FileWriter;
import java.io.PrintWriter;

import java.util.Date;

import javax.servlet.http.HttpSession;

public class Logger {
    String rootFolderPath = "//home//oracle//Desktop//AttendaceIrregularities";
    File rootFolder;
    PrintWriter pw ;
    
    String username;

    public Logger() {
        rootFolder = new File(rootFolderPath);
        if (!rootFolder.exists()) {
            rootFolder.mkdir();
        }
        
    }

    public void logInfo(String line)throws Exception {
        File userLogFolder = new File(rootFolderPath);
        if (!userLogFolder.exists()) {
            userLogFolder.mkdir();
        }
        
        File logFile =
            new File(rootFolderPath  + "//"+ "irregularities" + "_" +
                     new Date(System.currentTimeMillis()).toString().split(" ")[0] +
                     " " +
                     new Date(System.currentTimeMillis()).toString().split(" ")[1] +
                     " " +
                     new Date(System.currentTimeMillis()).toString().split(" ")[2]);
        
        if (!logFile.exists()) {
            logFile.createNewFile();
        }
        pw = new PrintWriter(new BufferedWriter(new FileWriter(logFile,true)));
        
        pw.write(line);
        
        pw.close();
        
    }
    public void logError(Exception e,String username) throws Exception{
        File userLogDir =
            new File(rootFolderPath + "//" + username);
        if (!userLogDir.exists()) {
            userLogDir.mkdir();
        }

        File userLogFile =
            new File(rootFolderPath  + "//" + username +
                     "//" + username + "_" +
                     new Date(System.currentTimeMillis()).toString().split(" ")[0] +
                     " " +
                     new Date(System.currentTimeMillis()).toString().split(" ")[1] +
                     " " +
                     new Date(System.currentTimeMillis()).toString().split(" ")[2]+"_error");
        
        if (!userLogFile.exists()) {
            userLogFile.createNewFile();
        }
        
        pw =
            new PrintWriter(new BufferedWriter(new FileWriter(userLogFile,
                                                              true)));
        
        writeln("");
        writeln("*********************************************************************");
        writeln("Time= "+new Date(System.currentTimeMillis()).toString());
        writeln("ErrorMessage= "+e.getMessage()+" ThrowableErrorMessage= "+e.getCause().getMessage()+" ");
        writeln("*********************************************************************");
        writeln("");
        pw.close();
        
    }
    public void logDebugStmt(String debug,String username) throws Exception{
        File userLogDir = new File(rootFolderPath + "//" + username);
        if (!userLogDir.exists()) {
            userLogDir.mkdir();
            System.out.println("****************************************************************");
            System.out.println(" created user log dir "+username);
            System.out.println("****************************************************************");
        }

        File userLogFile =
            new File(rootFolderPath  + "//" + username +
                     "//" + username + "_" +
                     new Date(System.currentTimeMillis()).toString().split(" ")[0] +
                     " " +
                     new Date(System.currentTimeMillis()).toString().split(" ")[1] +
                     " " +
                     new Date(System.currentTimeMillis()).toString().split(" ")[2]);
        
        if (!userLogFile.exists()) {
            userLogFile.createNewFile();
            System.out.println("****************************************************************");
            System.out.println(" created user log file "+username);
            System.out.println("****************************************************************");
        }
        
        pw =
            new PrintWriter(new BufferedWriter(new FileWriter(userLogFile,
                                                              true)));
        writeln(debug);
        pw.close();
    }
    
    private void writeln(String str){
        pw.append(str+"\n");
    }
    
    public void logAction(String action,String username)throws Exception{        
        File userLogDir =
            new File(rootFolderPath + "//" + username);
        

        File userLogFile =
            new File(rootFolderPath  + "//" + username +
                     "//" + username + "_" +
                     new Date(System.currentTimeMillis()).toString().split(" ")[0] +
                     " " +
                     new Date(System.currentTimeMillis()).toString().split(" ")[1] +
                     " " +
                     new Date(System.currentTimeMillis()).toString().split(" ")[2]);
        
        if (!userLogFile.exists()) {
            userLogFile.createNewFile();
        }
        
        pw =
            new PrintWriter(new BufferedWriter(new FileWriter(userLogFile,
                                                              true)));
        writeln("=========>   "+action+"    <==========");
        pw.close();
        
    }
    
    
    
}