package huff;

import java.io.*;


public class lzwt { 
 
 static String dict[]; 
 static int point; 
  
 public lzwt() { 
 } 
 
 public static String compress( String in ) { 
  dictInit( in.length() ); 
  String out = ""; 
  String w = ""; 
  char k; 
  while (in.length() > 0) { 
   k = in.charAt(0); 
   in = in.substring(1); 
   if (dictHas(w + k)) { w+=k; } 
   else { 
    out+= dictCode(w); 
    dictAdd(w + k); 
    w = k + ""; 
   } 
  } 
  System.out.println(out+dictCode(w));
  return out + dictCode(w); 
 } 
 
 
  
 private static void dictInit( int size ) { 
  dict = new String[size]; 
  point = 0; 
 } 
 
 
 private static boolean dictHas( String s ) { 
  return (dictCode(s).length() > 0); 
 } 
 private static String dictCode( String s ) { 
  String code = ""; 
  char c; 
  int i=0; 
  if (s.length() == 1) { //it's a character, just return the ascii code 
   return "" + s.charAt(0); 
  } else { 
   while ((code.length() == 0) && (i < dict.length)) { 
    if ((dict[i] != null) && dict[i].equals(s)) { 
     c = (char)(i+256); 
     code = "" + c; 
     
    } 
    i++; 
   } 
   return code; 
  } 
 } 
 private static String dictChar( int code ) { 
  if (code < 256) { return "" + (char)code; } 
  else if ((code-256) < dict.length) { return dict[code-256]; } 
  else { return ""; } 
 } 
 private static void dictAdd( String s ) { 
  dict[point] = s; 
  point++; 
 } 
   
 
public static String countAlphabetFrequency(String src)
{
	String total=null;
	try {
	  //파일 읽어오기
      BufferedReader in = new BufferedReader(new FileReader(src));
      String s;
      total="";
      while ((s = in.readLine()) != null) {
    	  	// 문자의 개수를 하나씩 셉니다.
    	  	for(int i=0;i<s.length();i++)
    	  	{
    	  		char c = s.charAt(i);
    	  	    total+=c;
    	  	}
      }
      in.close();
      
    } catch (IOException e) {
        System.err.println(e); // 에러가 있다면 메시지 출력
        System.exit(1);
    }
	return total;
} 
 
 
 public static void main( String args[] ) { 
   
 
  String str1=countAlphabetFrequency("src/harry.txt");
  long tt = System.currentTimeMillis(); 
  String c = compress( str1 ); 
  tt = System.currentTimeMillis() - tt; 
  System.out.println("Compressed: (" + str1.length() + " -> " + c.length() + " chars) in " + tt + " ms."); 
  if (args.length > 0) { 
   System.out.println("---COMPRESSED DATA FOLLOWS\r\n" + c + "\r\nEND OF DATA---"); 
  } 
 
  System.out.println("A saving of: " + (str1.length() - c.length()) + " chars.");
  System.out.println("lzw 압축 전: "+(str1.length())*8+"bit");
  System.out.println("lzw 압축 후 : "+(c.length())*8+"bit");

   
 } 
  
}

