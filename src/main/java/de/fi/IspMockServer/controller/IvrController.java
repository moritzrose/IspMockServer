package de.fi.IspMockServer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/ivr")
public class IvrController {
    private static final Logger logger = LoggerFactory.getLogger(IvrController.class);

    @GetMapping("/helloworld")
    public String vxmlHelloWorld()
    {
        return readVxml("./src/main/resources/vxml/helloworld.vxml");
    }

    @GetMapping("/multpartdtmf")
    public String vxmlMultpartDtmf()
    {
        return readVxml("./src/main/resources/vxml/multpartdtmf.vxml");
    }

    @GetMapping("/multpartvoice")
    public String vxmlMultpartVoice()
    {
        return readVxml("./src/main/resources/vxml/multpartvoice.vxml");
    }

    @GetMapping("/multpartvoiceext")
    public String vxmlMultpartVoiceExt()
    {
        return readVxml("./src/main/resources/vxml/multpartvoice_ext.vxml");
    }

    @GetMapping("/pin")
    public String vxmlPIN()
    {
        return readVxml("./src/main/resources/vxml/pin.vxml");
    }

    @GetMapping("/record")
    public String vxmlRecord()
    {
        return readVxml("./src/main/resources/vxml/record.vxml");
    }

    private String readVxml(String path)
    {
        logger.info("readVxml/0: Reading vxml from " + path);
        StringBuilder result = new StringBuilder();
        try {
            List<String> allLines = Files.readAllLines(Paths.get(path));
            for (String line : allLines) {
                result.append(line);
            }
        }
        catch (IOException e)
        {
            logger.error("readVxml/1: " + e.toString());
        }
        return result.toString();
    }

}
