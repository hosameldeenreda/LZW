import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

class LZW {
    private static  ArrayList<Integer> compress(String  text){
        if(text.length()==0)
            throw new RuntimeException("text cannot be empty String or null");
        ArrayList<Integer> outputTags = new ArrayList<>();
        Hashtable<String, Integer> dictionary = new Hashtable<>();
        String old= String.valueOf(text.charAt(0));
        int currentOutput= text.charAt(0);
        int dictionaryCounter=256;//out of ascii
        for (char currentChar : text.substring(1).toCharArray()){
            int temp=dictionary.getOrDefault((old+currentChar), -1);
            if(temp!=-1){
                //found at dictionary
                currentOutput = temp;
                old+=  currentChar;
            }
            else{
                outputTags.add(currentOutput);
                dictionary.put(old+currentChar,dictionaryCounter);
                old=String.valueOf(currentChar);
                dictionaryCounter++;
                currentOutput=currentChar;
            }
        }
        outputTags.add(currentOutput);
        return outputTags;
    }
    private static String decompres(ArrayList<Integer> tags){
        if(tags.size()==0)
            throw new RuntimeException("tags cannot be empty or null");
        String text="";
        Hashtable<Integer, String> dictionary = new Hashtable<>();
        String old= String.valueOf((char)tags.get(0).intValue());
        text+=old;
        int dictionaryCounter=256;//out of ascii
        for (int currentTag : tags.subList(1,tags.size())){
            if(currentTag<256){
                // tag is represent one character
                text+=(char)currentTag;
                dictionary.put(dictionaryCounter,old+(char)currentTag);
                dictionaryCounter++;
                old= String.valueOf((char)currentTag);
            }
            else{
                String temp=dictionary.getOrDefault(currentTag, "-1");
                if (!temp.equals("-1")) {
                        //found at dictionary
                        text+=temp;
                        dictionary.put(dictionaryCounter,old+temp.charAt(0));
                        old= temp;
                }
                else{
                    text+=(old+old.charAt(0));
                    dictionary.put(dictionaryCounter,old+old.charAt(0));
                    old+=old.charAt(0);
                }
                dictionaryCounter++;
            }
        }
        return text;
    }
    private static String readTextFile(File file) throws IOException {
        if(!file.exists())
            throw new RuntimeException("file not found");
        Scanner sc = new Scanner(file);
        sc.useDelimiter("\\Z");
        return (sc.next());
    }
    private static void writeTags(File file,ArrayList<Integer> tags)throws IOException {
        file.createNewFile(); // if file already exists will do nothing
        FileWriter write = new FileWriter(file);
        for (int currentTag : tags) {
            write.append(currentTag+" ");
        }
        write.flush();
        write.close();
    }
    private static ArrayList<Integer> readTagsFromFile(File file) throws IOException {
        if(!file.exists())
            throw new RuntimeException("file not found");
        Scanner sc = new Scanner(file);
        ArrayList<Integer> tags=new ArrayList<>();
        while (sc.hasNextInt()){
            tags.add(sc.nextInt());
        }
        return tags;
    }
    private static void writeText(File file,String data) throws IOException {
        file.createNewFile(); // if file already exists will do nothing
        FileWriter write = new FileWriter(file);
        write.append(data);
        write.flush();
        write.close();
    }
    public static  void compress(File  original,File compressed) throws IOException {
        try {
            ArrayList<Integer> tags = compress(readTextFile(original));
            writeTags(compressed, tags);
        }  catch (Exception ex) {
            System.out.print(ex.toString());
        }
    }
    public static void decompres(File  compressed,File uncompressed) throws IOException {
        try{
        writeText(uncompressed,decompres(readTagsFromFile(compressed)));
        }  catch (Exception ex) {
            System.out.print(ex.toString());
        }

    }
}
