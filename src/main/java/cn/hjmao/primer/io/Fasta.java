package cn.hjmao.primer.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Fasta {
  public static final int NEW_LINE_WIDTH = 1; // '\n'
  private static int CURRENT_POSITION = 0;

  public static List<Seq> load(String filename) {
    return load(filename, -1);
  }

  public static List<Seq> load(String filename, int limit) {
    Fasta.CURRENT_POSITION = 0;
    List<Seq> sequences = new ArrayList<>();

    try {
      File file = new File(filename);
      FileReader fr = new FileReader(file);
      BufferedReader br = new BufferedReader(fr);

      int count = 0;
      String line;
      StringBuilder sb = new StringBuilder();

      while((line=br.readLine()) != null) {
        increaseCurrentPosition(line);
        if (!line.startsWith(">")) {
          continue;
        } else {
          Seq seq = parseHeader(line, count);
          sequences.add(seq);
          count = count + 1;
          break;
        }
      }

      while((line=br.readLine()) != null) {
        increaseCurrentPosition(line);
        if (!line.startsWith(">")) {
          sb.append(line);
        } else {
          setReadsForLastSeq(sequences, sb);
          Seq seq = parseHeader(line, count);
          sequences.add(seq);

          count = count + 1;
          if (limit > 0 && count >= limit) {
            break;
          }
        }
      }
      setReadsForLastSeq(sequences, sb);
      fr.close();

      return sequences;
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  private static byte[] getReadsAsBytes(String reads) {
    byte[] bytes = new byte[reads.length()];
    for (int i = 0; i < reads.length(); i++) {
      bytes[i] = -1;
      switch (reads.charAt(i)) {
        case 'A':
          bytes[i] = 0;
          break;
        case 'C':
          bytes[i] = 1;
          break;
        case 'G':
          bytes[i] = 2;
          break;
        case 'T':
          bytes[i] = 3;
          break;
        default:
          break;
      }
    }
    return bytes;
  }

  private static void setReadsForLastSeq(List<Seq> sequences, StringBuilder sb) {
    if (!sb.isEmpty() && sequences.size() > 0) {
      Seq seq = sequences.get(sequences.size() - 1);
      seq.setReads(getReadsAsBytes(sb.toString()));
      sb.setLength(0); // clear StringBuilder
    }
  }

  private static void increaseCurrentPosition(String line) {
    CURRENT_POSITION = CURRENT_POSITION + line.length() + Fasta.NEW_LINE_WIDTH;
  }

  private static Seq parseHeader(String header, int index) {
    int start = header.indexOf('>');
    int end = header.indexOf(' ');

    String id;
    String desc;
    if (end != -1) {
      id = header.substring(start + 1, end);
      desc = header.substring(end);
    } else {
      id = header.substring(start + 1);
      desc = "";
    }

    return new Seq(id, index, desc, CURRENT_POSITION);
  }
}
