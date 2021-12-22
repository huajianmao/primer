package cn.hjmao.primer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class KmerUtils {
  private static final Character[] CHARS = {'A', 'G', 'C', 'T'};
  private static final Set<Character> ATCG = new HashSet<>(Arrays.asList(CHARS));

  public static Map<String, List<Integer>> makeIndex(String reads, int k) {
    Map<String, List<Integer>> index = new HashMap<>();
    for (int i = 0; i < reads.length() - k + 1; i++) {
      String kmer = reads.substring(i, i + k);
      if (containsIllegalChar(kmer)) {
        continue;
      }
      if (!index.containsKey(kmer)) {
        index.put(kmer, new ArrayList<>());
      }
      index.get(kmer).add(i);
    }

    return index;
  }

  public static Map<String, Map<String, List<Integer>>> makeIndex(List<Seq> seqs, int k) {
    Map<String, Map<String, List<Integer>>> index = new HashMap<>();
    for (int i = 0; i < seqs.size(); i++) {
      if (i % 10 == 0) {
        System.out.println("Processing sequence " + i + "...");
      }

      Seq seq = seqs.get(i);
      String sequence = seq.getReads().toString();
      if (sequence == null || sequence.length() < k) {
        continue;
      }

      for (int position = 0; position < sequence.length() - k + 1; position++) {
        String kmer = sequence.substring(position, position + k);

        if (containsIllegalChar(kmer)) {
          continue;
        }

        if (!index.containsKey(kmer)) {
          index.put(kmer, new HashMap<>());
        }

        Map<String, List<Integer>> positions = index.get(kmer);
        if (!positions.containsKey(seq.getId())) {
          positions.put(seq.getId(), new ArrayList<>());
        }
        positions.get(seq.getId()).add(position);
      }
    }

    return index;
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
