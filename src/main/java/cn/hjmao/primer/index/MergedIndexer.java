package cn.hjmao.primer.index;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.hjmao.primer.io.Seq;

public class MergedIndexer extends Indexer {
  private List<Integer>[] index;
  private int[] offsets;

  public MergedIndexer(List<Seq> seqs, int k) {
    super(seqs, k);

    int indexLength = (int) Math.pow(4, k);
    index = new List[indexLength];
    for (int i = 0; i < indexLength; i++) {
      index[i] = Collections.synchronizedList(new ArrayList<>());
    }

    offsets = new int[seqs.size()];
    offsets[0] = 0;
    for (int i = 1; i < offsets.length; i++) {
      offsets[i] = offsets[i - 1] + seqs.get(i).getReads().length;
    }
  }

  @Override
  protected List<Integer> getPositions(int kmer, int seqNo) {
    return index[kmer];
  }

  @Override
  protected int calcPosition(int pos, int seqNo) {
    return pos + offsets[seqNo];
  }

  @Override
  public void save() {
    String outputFile = "/tmp/kmers/hjmao.bin";
    int count = 0;

    try (OutputStream outputStream = new FileOutputStream(outputFile);) {
      for (int i = 0; i < index.length; i++) {
        List<Integer> integers = index[i];
        byte[] allBytes = new byte[integers.size() * 4];
        count = count + integers.size();
        for (int j = 0; j < integers.size(); j++) {
          int integerTemp = integers.get(j);
          for (int k = 0; k < 4; k++) {
            allBytes[j * 4 + k] = (byte) ((integerTemp >> (8 * k)) & 0xFF);
          }
        }
        outputStream.write(allBytes);
        outputStream.write('\n');
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }
}
