package cn.hjmao.primer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Seq {
  private String id;
  private String desc;
  private int offset;
  private int lineBases = -1;
  private int lineWidth = -1;
  private StringBuilder reads;

  public static final int NEW_LINE_WIDTH = 1; // '\n'

  private static int CURRENT_POSITION = 0;

  public Seq(String id, String desc, int offset) {
    this.id = id;
    this.desc = desc;
    this.offset = offset;
    this.reads = new StringBuilder();
  }

  public static List<Seq> load(String filename) {
    return load(filename, -1);
  }

  public static List<Seq> load(String filename, int limit) {
    List<Seq> sequences = new ArrayList<>();

    try {
      File file = new File(filename);
      FileReader fr = new FileReader(file);
      BufferedReader br = new BufferedReader(fr);

      int count = 0;
      String line;
      while((line=br.readLine()) != null) {
        increaseCurrentPosition(line);
        if (!line.startsWith(">")) {
          continue;
        } else {
          Seq seq = parseHeader(line);
          sequences.add(seq);
          count = count + 1;
          break;
        }
      }

      while((line=br.readLine()) != null) {
        increaseCurrentPosition(line);
        if (!line.startsWith(">")) {
          Seq seq = sequences.get(sequences.size() - 1);
          seq.append(line);
        } else {
          count = count + 1;
          if (limit > 0 && count >= limit) {
            break;
          }
          Seq seq = parseHeader(line);
          sequences.add(seq);
        }
      }
      fr.close();
      return sequences;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private void append(String partial) {
    this.reads.append(partial.toUpperCase());
    if (this.lineBases < 0) {
      this.lineBases = partial.length();
      this.lineWidth = this.lineBases + NEW_LINE_WIDTH;
    }
  }

  private static void increaseCurrentPosition(String line) {
    CURRENT_POSITION = CURRENT_POSITION + line.length() + Seq.NEW_LINE_WIDTH;
  }

  private static Seq parseHeader(String header) {
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

    return new Seq(id, desc, CURRENT_POSITION);
  }
}
