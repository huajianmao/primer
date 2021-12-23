package cn.hjmao.primer;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.List;
import java.util.Map;

public class KmerUtils {
  private static int K = 9;
  // private static int BASE_BITS = 2;

  public static Map<Integer, List<Integer>>[] initIndex() {
    Map<Integer, List<Integer>>[] index = new ConcurrentHashMap[(int) Math.pow(4, KmerUtils.K)];
    for (int i = 0; i < index.length; i++) {
      index[i] = new ConcurrentHashMap<>();
    }
    return index;
  }

  public static void makeIndex(List<Seq> sequences, Map<Integer, List<Integer>>[] index, int processes) {
    if (processes <= 0) {
      processes = Runtime.getRuntime().availableProcessors();
    }

    ExecutorService executor = Executors.newFixedThreadPool(processes);
    for (int i = 0; i < sequences.size(); i++) {
      Seq seq = sequences.get(i);
      executor.execute(new IndexThread(seq, index));
    }
    executor.shutdown();
    while (!executor.isTerminated()) { }
    System.out.println("Done indexing.");
  }

  public static void makeIndex(List<Seq> seqs, Map<Integer, List<Integer>>[] result) {
    for (int i = 0; i < seqs.size(); i++) {
      makeIndex(seqs.get(i), result);
    }
  }

  public static void makeIndex(Seq seq, Map<Integer, List<Integer>>[] result) {
    byte[] sequence = seq.getReads();
    if (sequence.length < KmerUtils.K) {
      return;
    }

    for (int position = 0; position < sequence.length - KmerUtils.K + 1; position++) {

      int index = kmer2int(sequence, position);
      if (index == -1) {
        continue;
      }

      int offset = seq.getOffset();
      if (!result[index].containsKey(offset)) {
        result[index].put(offset, new ArrayList<>());
      }
      result[index].get(offset).add(position);
    }
  }

  private static int kmer2int(byte[] reads, int start) {
    int value = 0;
    for (int i = 0; i < KmerUtils.K; i++) {
      if (reads[start + i] < 0) {
        return -1;
      }
      value = (value << 2) + reads[start + i];
    }
    return value;
  }

  // private static int kmer2int(int prev, byte current, int k) {
  //   prev = prev & ((1 << ((k - 1) * BASE_BITS)) - 1);
  //   prev = prev << BASE_BITS;
  //   return prev + current;
  // }
}
