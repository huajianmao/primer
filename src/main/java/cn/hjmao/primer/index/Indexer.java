package cn.hjmao.primer.index;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.hjmao.primer.io.Seq;
import lombok.Getter;

@Getter
public abstract class Indexer {
  private int k;
  private List<Seq> seqs;

  public Indexer(List<Seq> seqs, int k) {
    this.seqs = seqs;
    this.k = k;
  }

  public void index(int processes) {
    if (processes <= 0) {
      processes = Runtime.getRuntime().availableProcessors();
    }

    ExecutorService executor = Executors.newFixedThreadPool(processes);
    for (int i = 0; i < seqs.size(); i++) {
      Seq seq = seqs.get(i);
      executor.execute(new IndexThread(seq, this));
    }
    executor.shutdown();
    while (!executor.isTerminated()) { }
    System.out.println("Done indexing.");
  }

  public abstract void save();

  protected void index(Seq seq) {
    byte[] sequence = seq.getReads();
    if (sequence.length < this.getK()) {
      return;
    }
    int offset = seq.getIndex();

    int pos = 0;
    int k = 0;
    int index = 0;

    while (pos < sequence.length - k) {
      while (k < this.getK() && pos < sequence.length - k) {
        if (sequence[pos + k] < 0) {
          pos = pos + k + 1;
          index = 0;
          k = 0;
          break;
        } else {
          index = (index << 2) + sequence[pos + k];
          k = k + 1;
        }
      }
      if (k == this.getK()) {
        this.getPositions(index, offset).add(calcPosition(pos, offset));
        pos = pos + 1;
        index = index & 0x0ffff;
        k = k - 1;
      }
    }
  }

  protected int calcPosition(int pos, int seqNo) {
    return pos;
  }

  protected abstract List<Integer> getPositions(int kmer, int seqNo);
}
