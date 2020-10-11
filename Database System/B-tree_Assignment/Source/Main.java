import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException, FileNotFoundException {


        int size = 0;

        switch (args[0]) {
            case "-c": // creation
                PrintWriter pw = new PrintWriter(args[1]);
                pw.println(args[2]);
                pw.close();
                break;


            case "-i":
                BufferedReader br = new BufferedReader(new FileReader(args[1]));
                while (true) {
                    String line = br.readLine();
                    if (line == null) break;
                    size = Integer.parseInt(line);
                }
                br.close();
                Node node = new Node(size);

                // node made
                br = new BufferedReader(new FileReader(args[2]));
                int k = 0;
                while(true) {
                    String line = br.readLine();
                    if (line==null) break;



                    String lineInfo[] = line.split(",");
                    if(!lineInfo[0].equals("")){
                        Integer key = Integer.parseInt(lineInfo[0]);
                        Integer value = Integer.parseInt(lineInfo[1]);

                        if(k < size){
                            node = node.firstInsert(key, value);
                        } else{
                            node = node.insert(key, value);
                        }
                        k++;
                    }
                }
                br.close();

                // have to write at index.dat
                FileWriter fw = new FileWriter(args[1]);
                PrintWriter pwi = new PrintWriter(fw,true);
                pwi.println(4);
                node.write(pwi);

                pwi.flush();
                pwi.close();


                // not completed yet
                break;


            case "-d": //deletion

                break;
            case "-s":
//                br = new BufferedReader(new FileReader(args[2]));
//                String line = br.readLine();
//                size = Integer.parseInt(line);
//                node = new NonLeafNode(size);
//
//                while(true) {
//                    line = br.readLine();
//                    if (line==null) break;
//
//
//
//                    String lineInfo[] = line.split(" ");
//                    if(!lineInfo[0].equals("")){
//                        Integer key = Integer.parseInt(lineInfo[0]);
//                        Integer value = Integer.parseInt(lineInfo[1]);
//
//                        if(k < size){
//                            node = node.firstInsert(key, value);
//                        } else{
//                            node = node.insert(key, value);
//                        }
//                        k++;
//                    }
//                }
//                br.close();
                break;
            case "-r":

                break;

        }





    }
}
