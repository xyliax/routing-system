package tju.ds.map.model;

public enum EdgeCondition {
    BAD(1, "较差"), CROWDED(2, "拥堵"), MEDIUM(3, "一般"),
    WELL(4, "较好"), PERFECT(5, "畅通"), UNKNOWN(0, "未知");
    final int ratio;
    final String info;

    EdgeCondition(int ratio, String info) {
        this.ratio = ratio;
        this.info = info;
    }
}
