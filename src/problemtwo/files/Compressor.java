package problemtwo.files;

import problemtwo.Huffman;
import problemtwo.HuffmanNode;

import java.io.*;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class Compressor {
    private Compressor() {}

    public static boolean compress(int n, String path) {
        try {
            byte[] fileAsBytes = new byte[n * 5000];   // size is a multiple of n to reduce fragmentation
            int counter;
            Map<String, Integer> frequencies = new HashMap<>();
            StringBuilder sb;
            try (InputStream inputStream = new FileInputStream(path);
            BufferedInputStream bis = new BufferedInputStream(inputStream)) {
                while ((counter = bis.read(fileAsBytes)) != -1) {
                    // construct our map of bytes and their corresponding frequencies
                    for (int i = 0; i < counter; i += n) {
                        sb = new StringBuilder();
                        appendKey(n, fileAsBytes, counter, i, sb);
                        String key = sb.toString();
                        if (!frequencies.containsKey(key))
                            frequencies.put(key, 1);
                        else frequencies.replace(key, frequencies.get(key) + 1);
                    }
                }
            }

            // now we construct the huffman tree and then the prefix code
            HuffmanNode<String> root = Huffman.constructHuffmanTree(frequencies);
            Huffman.constructPrefixCode(root, new StringBuilder());

            sb = new StringBuilder();

            // create the header file to help us in decompression later.
            sb.append(Huffman.prefixCode.size() + 1).append("\n"); // header length (Byte unit length and prefix code length).
            sb.append("n=").append(n).append("\n");
            for (Map.Entry<String, String> entry : Huffman.prefixCode.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
            }

            // compress.
            bitCompress(path, n, sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static void bitCompress(String path, int n, String header) throws IOException {
        /*
        * helping hands for preparing the reading operation.
        * */
        byte[] fileAsBytes = new byte[n * 10000];   // size is a multiple of n to reduce fragmentation
        byte[] compressedFile;
        int counter;

        /*
        * preparing the output path to help the buffered output stream.
        * */
        StringBuilder sb = new StringBuilder();
        String[] inputPathArray = path.split("\\\\");
        int inputLength = inputPathArray.length;
        for (int i = 0; i < inputLength - 1; i++)
            sb.append(inputPathArray[i]).append("\\");
        sb.append("19015709.").append(n).append(".");
        sb.append(inputPathArray[inputLength - 1]);
        sb.append(".hc");
        String outputPath = sb.toString();  // our output path ready to go.

        File outputFile = new File(outputPath);

        try (InputStream inputStream = new FileInputStream(path);
             BufferedInputStream bis = new BufferedInputStream(inputStream);
             OutputStream fos = new FileOutputStream(outputFile);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            // write the header to the file
            oos.writeObject(header);
            counter = bis.read(fileAsBytes);
            while (counter > 0) {
                // compress the file itself
                sb = new StringBuilder();
                for (int i = 0; i < counter; i += n) {
                    StringBuilder bytes = new StringBuilder();
                    appendKey(n, fileAsBytes, counter, i, bytes);
                    sb.append(Huffman.prefixCode.get(bytes.toString()));
                }
//                System.out.println(sb);
                BitSet fileAsBitSet = stringToBitSet(sb.toString());
                compressedFile = fileAsBitSet.toByteArray();
//                System.out.println(Huffman.prefixCode);
//                System.out.println(new String(compressedFile));
                fos.write(compressedFile);
                counter = bis.read(fileAsBytes);
            }
        }
    }

    private static void appendKey(int n, byte[] fileAsBytes, int counter, int i, StringBuilder bytes) {
        if (counter - i == counter % n) {
            // last fragmentation handling
            for (int j = i; j < counter; j++) {
                if (j < counter - 1)
                    bytes.append(fileAsBytes[j]).append(" ");
                else bytes.append(fileAsBytes[j]);
            }
        } else {
            for (int j = 0; j < n; j++) {
                if (j < n - 1)
                    bytes.append(fileAsBytes[i + j]).append(" ");
                else bytes.append(fileAsBytes[i + j]);
            }
        }
    }

    private static BitSet stringToBitSet(String bitString) {
        bitString = bitString.replace("null", "");
        BitSet bits = new BitSet(bitString.length());
        for (int i = 0; i < bitString.length(); i++) {
            bits.set(i, (bitString.charAt(i) == '1'));
        }
        return bits;
    }
}