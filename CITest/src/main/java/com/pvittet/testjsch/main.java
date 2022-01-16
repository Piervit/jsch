package com.pvittet.testjsch;

import com.jcraft.jsch.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;


/**
 * Basic scp client.
 * args:
 *   - first arg is the path to the local file.
 *   - second arg is the destination using format <user>@<ip>:<destPath>
 *   - third arg is the password
 * 
 * We only accept though port 22
 *
 * Part of this code taken from ScpTo.java exemple.
 *
 */
class Main{

  public static void main (String args[]){
    if(args.length!=3){
      System.err.println("usage: java ScpTo file1 user@remotehost:file2");
      System.exit(-1);
    }      

    FileInputStream fis=null;
    try{
      /*Local file*/
      String lfile=args[0];
      /*user*/
      String user=args[1].substring(0, args[1].indexOf('@'));
      String dest=args[1].substring(args[1].indexOf('@')+1);
      /*host ip*/
      String host=dest.substring(0, dest.indexOf(':'));
      /*destfile */
      String rfile=dest.substring(dest.indexOf(':')+1);
      /*password */
      String password =args[2];

      /* Start opening session */
      JSch jsch=new JSch();
      Session session=jsch.getSession(user, host, 22);

      // username and password will be given via UserInfo interface.
      session.setPassword(password);
      session.connect();

      boolean ptimestamp = true;

      // exec 'scp -t rfile' remotely
      String command="scp " + (ptimestamp ? "-p" :"") +" -t "+rfile;
      Channel channel=session.openChannel("exec");
      ((ChannelExec)channel).setCommand(command);

      // get I/O streams for remote scp
      OutputStream out=channel.getOutputStream();
      InputStream in=channel.getInputStream();

      channel.connect();

      if(checkAck(in)!=0){
        System.exit(0);
      }

      File _lfile = new File(lfile);

      if(ptimestamp){
        command="T"+(_lfile.lastModified()/1000)+" 0";
        // The access time should be sent here,
        // but it is not accessible with JavaAPI ;-<
        command+=(" "+(_lfile.lastModified()/1000)+" 0\n"); 
        out.write(command.getBytes()); out.flush();
        if(checkAck(in)!=0){
          System.exit(0);
        }
      }

      // send "C0644 filesize filename", where filename should not include '/'
      long filesize=_lfile.length();
      command="C0644 "+filesize+" ";
      if(lfile.lastIndexOf('/')>0){
        command+=lfile.substring(lfile.lastIndexOf('/')+1);
      }
      else{
        command+=lfile;
      }
      command+="\n";
      out.write(command.getBytes()); out.flush();
      if(checkAck(in)!=0){
        System.exit(0);
      }

      // send a content of lfile
      fis=new FileInputStream(lfile);
      byte[] buf=new byte[1024];
      while(true){
        int len=fis.read(buf, 0, buf.length);
        if(len<=0) break;
        out.write(buf, 0, len); //out.flush();
      }
      fis.close();
      fis=null;
      // send '\0'
      buf[0]=0; out.write(buf, 0, 1); out.flush();
      if(checkAck(in)!=0){
        System.exit(0);
      }
      out.close();

      channel.disconnect();
      session.disconnect();

      System.exit(0);
    }
    catch(Exception e){
      System.out.println(e);
      try{if(fis!=null)fis.close();}catch(Exception ee){}
    }

  }
  static int checkAck(InputStream in) throws IOException{
    int b=in.read();
    // b may be 0 for success,
    //          1 for error,
    //          2 for fatal error,
    //          -1
    if(b==0) return b;
    if(b==-1) return b;

    if(b==1 || b==2){
      StringBuffer sb=new StringBuffer();
      int c;
      do {
        c=in.read();
        sb.append((char)c);
      }
      while(c!='\n');
      if(b==1){ // error
        System.out.print(sb.toString());
      }
      if(b==2){ // fatal error
        System.out.print(sb.toString());
      }
    }
    return b;
  }




}
