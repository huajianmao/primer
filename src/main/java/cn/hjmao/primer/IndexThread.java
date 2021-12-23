package cn.hjmao.primer;

import java.util.List;
import java.util.Map;

public class IndexThread implements Runnable {
  private Seq seq;
  private Map<Integer, List<Integer>>[] index;

  public IndexThread(Seq seq, Map<Integer, List<Integer>>[] index) {
    this.seq = seq;
    this.index = index;
  }

  @Override
  public void run() {
    // System.out.println("Going to processing the " + seq.getIndex() + " sequence [" + seq.getId() + "] with k = " + k + " ...");
    KmerUtils.makeIndex(seq, index);
    System.out.println("Done processing the " + seq.getIndex() + " sequence [" + seq.getId() + "] ...");
  }
}
