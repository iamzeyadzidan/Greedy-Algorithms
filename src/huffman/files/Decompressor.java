package huffman.files;

import huffman.Huffman;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Decompressor {
    private Decompressor() {
    }

    public static boolean decompress(String path) {
        /*
         * prepare the input stream buffer to read the compressed file
         * */
        try (InputStream inputStream = new FileInputStream(path);
             BufferedInputStream bis = new BufferedInputStream(inputStream);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            /*
             * header info to help with decompression
             * */
            int fileSize;
            int n;

            /*
             * read the header field first
             * */
            String header = (String) ois.readObject();
            try (Scanner scanner = new Scanner(header)) {
                Map<String, String> localPrefixCode = new HashMap<>();
                int mapLength = Integer.parseInt(scanner.nextLine());
                fileSize = Integer.parseInt(scanner.nextLine().split("=")[1]);
                n = Integer.parseInt(scanner.nextLine().split("=")[1]);
                String[] entry;
                for (int i = 0; i < mapLength; i++) {
                    entry = scanner.nextLine().split("=");
                    localPrefixCode.put(entry[1], entry[0]);
                }
                Huffman.prefixCode.putAll(localPrefixCode);
            }

            /*
             * prepare the output stream buffer for writing the decompressed file,
             * */
            String outputPath = getOutputPath(path);
            StringBuilder sb;

            /*
             * decompress
             * */
            try (OutputStream outputStream = new FileOutputStream(outputPath);
                 BufferedOutputStream bos = new BufferedOutputStream(outputStream)) {
                byte[] fileAsBytes = new byte[n * 10000];
                int counter;
                int scannedBytes = 0;
                sb = new StringBuilder();
                StringBuilder prefixChecker;
                do {
                    prefixChecker = new StringBuilder();
                    counter = bis.read(fileAsBytes);
                    // construct our map of bytes and their corresponding frequencies
                    for (int i = 0; i < counter; i++) {
                        String key = String.format("%8s", Integer.toBinaryString(fileAsBytes[i] & 0xFF));
                        sb.append(new StringBuilder(key.replace(' ', '0')).reverse());
                        while (!sb.isEmpty()) {
                            prefixChecker.append(sb.charAt(0));
                            sb.deleteCharAt(0);
                            if (Huffman.prefixCode.containsKey(prefixChecker.toString())) {
                                if (fileSize == scannedBytes)
                                    break;
                                String[] nBytes = Huffman.prefixCode.get(prefixChecker.toString()).split(" ");
                                byte[] bytes = new byte[nBytes.length];
                                for (int k = 0; k < nBytes.length; k++)
                                    bytes[k] = Byte.parseByte(nBytes[k]);
                                prefixChecker = new StringBuilder();
                                bos.write(bytes);
                                scannedBytes += bytes.length;
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
