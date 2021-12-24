package cn.hjmao.primer.index;

import java.util.ArrayList;
import java.util.List;

import cn.hjmao.primer.io.Seq;

public class ArrayIndexer extends Indexer {
  private List<Integer>[][] index;

  public ArrayIndexer(List<Seq> seqs, int k) {
    super(seqs, k);

    int indexLength = (int) Math.pow(4, k);
    index = new ArrayList[seqs.size()][indexLength];
  }

  @Override
  protected List<Integer> getPositions(int kmer, int seqNo) {
    List<Integer> positions = this.index[seqNo][kmer];
    if (positions == null) {
      this.index[seqNo][kmer] = new ArrayList<>();
      positions = this.index[seqNo][kmer];
    }
    return positions;
  }
}
