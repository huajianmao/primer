package cn.hjmao.primer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Runner {
  public static void main(String[] args) {
    // String fasta = "./data/test.fasta";
    String fasta = "./data/GRCh38_latest_genomic.fasta";
    // String fasta = "./data/sequences2021.09.30.fasta";
    int k = 9;
    int processes = -1;

    System.out.println("Load sequences from fasta file: " + fasta);
    List<Seq> sequences = Seq.load(fasta);
    System.out.println(sequences.size() + " sequences loaded.");

    Map<Integer, List<Integer>>[] index = KmerUtils.initIndex(k);

    KmerUtils.makeIndex(sequences, k, index, processes);
    // KmerUtils.makeIndex(sequences, k, index);

    // for (int i = 0; i < index.length; i++) {
    //   if (index[i].size() == 0) {
    //     continue;
    //   }
    //   System.out.println("Kmer " + i + ": " + index[i].size() + " positions. ");
    // }
  }
}
