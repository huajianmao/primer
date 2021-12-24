package cn.hjmao.primer.io;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Seq {
  private String id;
  private int index;
  private String desc;
  private int offset;
  private int lineBases = -1;
  private int lineWidth = -1;
  private byte[] reads;

  public Seq(String id, int index, String desc, int offset) {
    this.id = id;
    this.index = index;
    this.desc = desc;
    this.offset = offset;
  }
}
