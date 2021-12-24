package cn.hjmao.primer.index.utils;

import cn.hjmao.primer.index.Indexer;
import cn.hjmao.primer.io.Seq;

public class IndexThread implements Runnable {
  private Seq seq;
  private Indexer indexer;

  public IndexThread(Seq seq, Indexer indexer) {
    this.seq = seq;
    this.indexer = indexer;
  }
  @Override
  public void run() {
    // System.out.println("Going to processing the " + seq.getIndex() + " sequence [" + seq.getId() + "] with k = " + k + " ...");
    indexer.index(seq);
    System.out.println("Done processing the " + seq.getIndex() + " sequence [" + seq.getId() + "] ...");
  }
}
