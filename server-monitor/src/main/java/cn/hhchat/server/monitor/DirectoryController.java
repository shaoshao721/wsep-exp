package cn.hhchat.server.monitor;

import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/dir")
public class DirectoryController {

    @PostMapping("create")
    public String createDir(@RequestParam String dir, String baseDir) {
        String str = RuntimeUtil.execForStr(StrUtil.format("mkdir -p {}/{}", baseDir, dir));
        return "ok";
    }

}
