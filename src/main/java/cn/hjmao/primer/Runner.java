package cn.hjmao.primer;

import java.util.List;
import java.util.Map;

class Runner {
  public static void main(String[] args) {
    // String fasta = "./data/test.fasta";
    String fasta = "./data/sequences2021.09.30.fasta";
    int k = 9;

    System.out.println("Load sequences from fasta file: " + fasta);
    List<Seq> sequences = Seq.load(fasta);
    System.out.println(sequences.size() + " sequences loaded.");

    Map<String, Map<String, List<Integer>>> index = KmerUtils.makeIndex(sequences, k);
    // Map<String, List<Integer>> index = KmerUtils.makeIndex(sequences.get(0).getReads().toString(), k);
    // for (Map.Entry<String, List<Integer>> entry: index.entrySet()) {
      // System.out.println(entry.getKey() + ": " + entry.getValue());
    // }
  }
}
