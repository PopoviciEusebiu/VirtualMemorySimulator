import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class MemoryManager {
    private Map<Long, VirtualPage> pageTable; // Tabelul de pagini (Virtual Page -> Physical Page)
    private Map<Long, PhysicalPage> physicalMemory; // Memoria fizică (Physical Page -> Data)
    private Queue<Long> tlbFifoQueue; // FIFO Queue pentru TLB

    private int pageSize; // Dimensiunea paginii
    private int memorySize; // Dimensiunea memoriei fizice
    private int tlbSize; // Dimensiunea TLB
    private String lastOperationResult = "";
    private int usedMemory = 0;

    public MemoryManager(int pageSize, int tlbSize, int memorySize) {
        pageTable = new HashMap<>();
        physicalMemory = new HashMap<>();
        tlbFifoQueue = new LinkedList<>();
        this.pageSize = pageSize;
        this.tlbSize = tlbSize;
        this.memorySize = memorySize;
    }

    public void addPageToMemory(long virtualPageId, long data) {
        long physicalPageId;
        VirtualPage virtualPage;
        boolean isHit = false;
        if (pageTable.containsKey(virtualPageId)) {
            physicalPageId = pageTable.get(virtualPageId).getPhysicalPage();
            isHit = true;
        } else {
            physicalPageId = generateUniquePhysicalPage();
            virtualPage = new VirtualPage(virtualPageId, physicalPageId);
            pageTable.put(virtualPageId, virtualPage);
        }
        PhysicalPage physicalPage = new PhysicalPage(physicalPageId, data, 0);
        physicalMemory.put(physicalPageId, physicalPage);
        if (!tlbFifoQueue.contains(virtualPageId)) {
            tlbFifoQueue.add(virtualPageId);
        }

        if (tlbFifoQueue.size() > tlbSize) {
            tlbFifoQueue.poll();
        }
        lastOperationResult = isHit ? "Hit" : "Miss";
    }





    public long readFromMemory(long virtualAddress) {
        if (pageTable.containsKey(virtualAddress)) {
            VirtualPage virtualPage = pageTable.get(virtualAddress);
            long physicalPageId = virtualPage.getPhysicalPage();
            if (physicalMemory.containsKey(physicalPageId)) {
                return physicalMemory.get(physicalPageId).getData();
            }
        }
        return -1;
    }

    public void writeToMemory(long virtualAddress, long data) {
        if (pageTable.containsKey(virtualAddress)) {
            VirtualPage virtualPage = pageTable.get(virtualAddress);
            long physicalPageId = virtualPage.getPhysicalPage();
            physicalMemory.get(physicalPageId).setData(data);
        } else {
            addPageToMemory(virtualAddress, data);
        }
    }

    public void removePageFromMemory(long virtualPageId) {
        if (pageTable.containsKey(virtualPageId)) {
            VirtualPage virtualPage = pageTable.get(virtualPageId);
            long physicalPageId = virtualPage.getPhysicalPage();
            pageTable.remove(virtualPageId);
            physicalMemory.remove(physicalPageId);
            tlbFifoQueue.remove(virtualPageId);
        }
    }

    private long generateUniquePhysicalPage() {
        long physicalPageId = 0;
        while (physicalMemory.containsKey(physicalPageId)) {
            physicalPageId++;
        }
        return physicalPageId;
    }

    public String getTlbInfo() {
        StringBuilder info = new StringBuilder("");

        for (Long virtualPageId : tlbFifoQueue) {
            VirtualPage virtualPage = pageTable.get(virtualPageId);
            if (virtualPage != null) {
                info.append("Tag: ").append(virtualPage.getId()).append(" -> Physical Page: ").append(virtualPage.getPhysicalPage()).append("\n");
            }
        }

        return info.toString();
    }

    public String getPageTableInfo() {
        StringBuilder info = new StringBuilder("");
        int currentCapacity = 0;
        for (VirtualPage virtualPage : pageTable.values()) {
            info.append("Virtual Page: ").append(virtualPage.getId()).append(" -> ");

            long physicalPageId = virtualPage.getPhysicalPage();

            if (currentCapacity < usedMemory) {
                info.append("Physical Page: ").append(physicalPageId);
                currentCapacity += pageSize;
            } else {
                info.append("Physical Page: disk");
            }

            info.append("\n");
        }
        return info.toString();
    }


    public String getPhysicalMemoryInfo() {
        StringBuilder info = new StringBuilder("");

        int currentCapacity = 0;

        for (PhysicalPage physicalPage : physicalMemory.values()) {
            if (currentCapacity < usedMemory) {
                info.append("Physical Page: ").append(physicalPage.getIndex()).append(" -> Data: ").append(physicalPage.getData()).append("\n");
                currentCapacity += pageSize;
            } else {
                break;
            }
        }
        return info.toString();
    }

    public Queue<Long> getFifoQueue() {
        return tlbFifoQueue;
    }

    public int getMemorySize() {
        return memorySize;
    }

    public String getLastOperationResult() {
        return lastOperationResult;
    }

    public int getUsedMemory() {
        if (usedMemory < memorySize) {
            usedMemory += pageSize;
        }
        return usedMemory;
    }

    public boolean isAddressWritten(long virtualAddress) {
        return pageTable.containsKey(virtualAddress);
    }

    public Map<Long, VirtualPage> getPageTable() {
        return pageTable;
    }
}