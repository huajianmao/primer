package cn.hjmao.primer.index;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.hjmao.primer.io.Seq;
import lombok.Getter;

@Getter
public class MapIndexer extends Indexer {
  Map<Integer, List<Integer>>[] index;

  public MapIndexer(List<Seq> seqs, int k) {
    super(seqs, k);

    int indexLength = (int) Math.pow(4, k);
    index = new ConcurrentHashMap[indexLength];

    for (int i = 0; i < index.length; i++) {
      index[i] = new ConcurrentHashMap<>();
    }
  }

  @Override
  protected List<Integer> getPositions(int kmer, int seqNo) {
    List<Integer> positions = index[kmer].get(seqNo);
    if (positions == null) {
      positions = new ArrayList<>();
      index[kmer].put(seqNo, positions);
    }
    return positions;
  }

  @Override
  public void save() {
    // TODO Auto-generated method stub
  }
}
