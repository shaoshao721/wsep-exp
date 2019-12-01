package cn.hhchat.server.monitor;

import lombok.Data;

@Data
public class ResourceStats {

    Double totalCPU;
    Double totalMemory;
    Double usedCPU;
    Double usedMemory;

}
