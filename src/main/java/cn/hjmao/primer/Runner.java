package cn.hjmao.primer;

import java.util.List;

import cn.hjmao.primer.index.ArrayIndexer;
import cn.hjmao.primer.index.Indexer;
import cn.hjmao.primer.index.MapIndexer;
import cn.hjmao.primer.io.Fasta;
import cn.hjmao.primer.io.Seq;

class Runner {
  public static void main(String[] args) {
    // String fasta = "./data/small.fasta";
    String fasta = "./data/test.fasta";
    // String fasta = "./data/GRCh38_latest_genomic.fasta";
    // String fasta = "./data/sequences2021.09.30.fasta";

    int processes = 25;
    int k = 9;

    System.out.println("Load sequences from fasta file: " + fasta);
    List<Seq> sequences = Fasta.load(fasta);
    System.out.println(sequences.size() + " sequences loaded.");

    // Indexer worker = new ArrayIndexer(sequences, k);
    Indexer worker = new MapIndexer(sequences, k);
    worker.index(processes);
  }
}
