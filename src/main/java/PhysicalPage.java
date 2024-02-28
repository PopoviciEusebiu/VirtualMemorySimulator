public class PhysicalPage {

    private long index;
    private long data;
    private long block;
    private long virtualPageId;


    public PhysicalPage(long index, long data, long block) {
        this.index = index;
        this.data = data;
        this.block = block;
    }

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }

    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }


    public long getBlock() {
        return block;
    }

    public void setBlock(long block) {
        this.block = block;
    }

    public long getVirtualPageId() {
        return virtualPageId;
    }

    public void setVirtualPageId(long virtualPageId) {
        this.virtualPageId = virtualPageId;
    }
}
