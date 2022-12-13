package problemtwo.files;

import problemtwo.Huffman;

import java.io.*;
import java.util.*;

public class Decompressor {
    private Decompressor() {}

    public static boolean decompress(String path) {
        try {
            /*
             * prepare the input stream buffer to read the compressed file
             * */
            InputStream inputStream = new FileInputStream(path);
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            ObjectInputStream ois = new ObjectInputStream(bis);
            String header = (String) ois.readObject();
            Scanner scanner = new Scanner(header);
            Map<String, String> localPrefixCode = new HashMap<>();
            int headerLength = Integer.parseInt(scanner.nextLine()) - 1;
            int n = Integer.parseInt(scanner.nextLine().split("=")[1]);
            String[] entry;
            for (int i = 0; i < headerLength; i++) {
                entry = scanner.nextLine().split("=");
                localPrefixCode.put(entry[1], entry[0]);
            }
            Huffman.prefixCode.putAll(localPrefixCode);

            /*
            * prepare the output stream buffer for writing the decompressed file,
            * */
            String outputPath = getOutputPath(path);
            StringBuilder sb;

//            System.out.println(Huffman.prefixCode);

            try (OutputStream outputStream = new FileOutputStream(outputPath);
                 BufferedOutputStream bos = new BufferedOutputStream(outputStream)) {
                byte[] fileAsBytes = new byte[n * 10000];
                int counter;
                sb = new StringBuilder();
                StringBuilder prefixChecker;
                do {
                    prefixChecker = new StringBuilder();
                    counter = bis.read(fileAsBytes);
                    // construct our map of bytes and their corresponding frequencies
                    for (int i = 0; i < counter; i++) {
                        String key = String.format("%8s", Integer.toBinaryString(fileAsBytes[i] & 0xFF));
//                        System.out.println("KEY: " + key);
                        sb.append(new StringBuilder(key.replace(' ', '0')).reverse());
//                        System.out.println(sb);
                        while (!sb.isEmpty()) {
                            prefixChecker.append(sb.charAt(0));
                            sb.deleteCharAt(0);
//                            System.out.println("PREFIX " + prefixChecker);
//                            System.out.println("ORIGIN " + sb);
                            if (Huffman.prefixCode.containsKey(prefixChecker.toString())) {
                                String[] nBytes = Huffman.prefixCode.get(prefixChecker.toString()).split(" ");
                                byte[] bytes = new byte[n];
                                for (int k = 0; k < nBytes.length; k++)
                                    bytes[k] = Byte.parseByte(nBytes[k]);
                                prefixChecker = new StringBuilder();
                                bos.write(bytes);
                            }
                        }
                    }
                } while (counter > 0);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static String getOutputPath(String path) {
        StringBuilder sb = new StringBuilder();
        String[] inputPathArray = path.split("\\\\");
        int inputLength = inputPathArray.length;
        for (int i = 0; i < inputLength - 1; i++)
            sb.append(inputPathArray[i]).append("\\");
        sb.append("extracted.").append(inputPathArray[inputLength - 1].replace(".hc", ""));
        return sb.toString();
    }
}
