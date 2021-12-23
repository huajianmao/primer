package cn.hjmao.primer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class KmerUtils {
  private static final Character[] CHARS = {'A', 'C', 'G', 'T'};
  private static final Set<Character> ATCG = new HashSet<>(Arrays.asList(CHARS));

  public static Map<Integer, List<Integer>>[] initIndex(int k) {
    Map<Integer, List<Integer>>[] index = new ConcurrentHashMap[(int) Math.pow(4, k)];
    for (int i = 0; i < index.length; i++) {
      index[i] = new ConcurrentHashMap<>();
    }
    return index;
  }

  public static void makeIndex(List<Seq> sequences, int k, Map<Integer, List<Integer>>[] index, int processes) {
    if (processes <= 0) {
      processes = Runtime.getRuntime().availableProcessors();
    }

    ExecutorService executor = Executors.newFixedThreadPool(processes);
    for (int i = 0; i < sequences.size(); i++) {
      Seq seq = sequences.get(i);
      executor.execute(new IndexThread(seq, k, index));
    }
    executor.shutdown();
    while (!executor.isTerminated()) { }
    System.out.println("Done indexing.");
  }

  public static void makeIndex(List<Seq> seqs, int k, Map<Integer, List<Integer>>[] result) {
    for (int i = 0; i < seqs.size(); i++) {
      makeIndex(seqs.get(i), k, result);
    }
  }

  public static void makeIndex(Seq seq, int k, Map<Integer, List<Integer>>[] result) {
    String sequence = seq.getReads().toString();
    if (sequence == null || sequence.length() < k) {
      return;
    }

    for (int position = 0; position < sequence.length() - k + 1; position++) {
      String kmer = sequence.substring(position, position + k);

      if (containsIllegalChar(kmer)) {
        continue;
      }

      int index = kmer2int(kmer);

      Map<Integer, List<Integer>> positions = result[index];
      if (!positions.containsKey(seq.getOffset())) {
        positions.put(seq.getOffset(), new ArrayList<>());
      }
      positions.get(seq.getOffset()).add(position);
    }
  }

  public static int kmer2int(String kmer) {
    int value = 0;
    for (int i = 0; i < kmer.length(); i++) {
      value = (value << 2);
      switch (kmer.charAt(i)) {
        case 'A':
          break;
        case 'C':
          value = value + 1;
          break;
        case 'G':
          value = value + 2;
          break;
        case 'T':
          value = value + 3;
          break;
        default:
          System.out.println("Illegal character: " + kmer.charAt(i));
          break;
      }
    }

    return value;
  }

  private static boolean containsIllegalChar(String kmer) {
    for (int i = 0; i < kmer.length(); i++) {
      if (!ATCG.contains(kmer.charAt(i))) {
        return true;
      }
    }
    return false;
  }
}
