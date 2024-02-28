public class VirtualPage {
    private long id;
    private long physicalPage;
    private long location;
    private boolean valid;
    private long block;

    public VirtualPage(long id,long page)
    {
        this.id = id;
        this.physicalPage = page;
        this.valid = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPhysicalPage() {
        return physicalPage;
    }

    public void setPhysicalPage(long physicalPage) {
        this.physicalPage = physicalPage;
    }

    public long getLocation() {
        return location;
    }

    public void setLocation(long location) {
        this.location = location;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public long getBlock() {
        return block;
    }

    public void setBlock(long block) {
        this.block = block;
    }
}
