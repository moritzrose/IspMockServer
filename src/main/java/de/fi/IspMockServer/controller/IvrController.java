package de.fi.IspMockServer.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ivr")
public class IvrController {

    private static final String VXML_HELLO_WORLD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<vxml version=\"2.1\" xmlns=\"http://www.w3.org/2001/vxml\">\n" +
            "    <form id=\"ansage\">\n" +
            "        <block><prompt>ISP sagt hallo</prompt>\n" +
            "         </block>\n" +
            "    </form>\n" +
            "</vxml>";

    private static final String VXML_PIN = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<vxml jwcid=\"vxml\">\n" +
            "\t<form>\n" +
            "\t\t<field name=\"pin\">\n" +
            "\t\t\t<grammar xmlns=\"http://www.w3.org/2001/06/grammar\" \n" +
            "\t\t\t\txmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n" +
            "\t\t\t\tmode=\"voice\" \n" +
            "\t\t\t\troot=\"root\" \n" +
            "\t\t\t\ttag-format=\"swi-semantics/1.0\" \n" +
            "\t\t\t\tversion=\"1.0\" \n" +
            "\t\t\t\txml:lang=\"de-DE\" \n" +
            "\t\t\t\txsi:schemaLocation=\"http://www.w3.org/2001/06/grammar http://www.w3.org/TR/speech-grammar/grammar.xsd\">\n" +
            "\t\t\t\t<rule id=\"root\">\n" +
            "\t\t\t\t\t<item repeat=\"1-6\">\n" +
            "\t\t\t\t\t\t<ruleref uri=\"#digit\"/>\n" +
            "\t\t\t\t\t</item>\n" +
            "\t\t\t\t</rule>\n" +
            "\t\t\t\t<rule id=\"digit\">\n" +
            "\t\t\t\t\t<one-of>\n" +
            "\t\t\t\t\t\t<item>null</item>\n" +
            "\t\t\t\t\t\t<item>eins</item>\n" +
            "\t\t\t\t\t\t<item>zwei</item>\n" +
            "\t\t\t\t\t\t<item>drei</item>\n" +
            "\t\t\t\t\t\t<item>vier</item>\n" +
            "\t\t\t\t\t\t<item>fünf</item>\n" +
            "\t\t\t\t\t\t<item>sechs</item>\n" +
            "\t\t\t\t\t\t<item>sieben</item>\n" +
            "\t\t\t\t\t\t<item>acht</item>\n" +
            "\t\t\t\t\t\t<item>neun</item>\n" +
            "\t\t\t\t\t</one-of>\n" +
            "\t\t\t\t</rule>\n" +
            "\t\t\t</grammar>\n" +
            "\t\t\t<prompt>Bitte nennen Sie mir zur Authentifizierung jetzt Ihre PIN</prompt>\n" +
            "\t\t\t<filled>\n" +
            "\t\t\t\t<prompt>Folgendes habe ich verstanden <value expr=\"pin\"/></prompt>\n" +
            "\t\t\t</filled>\n" +
            "\t\t</field>\n" +
            "\t</form>\n" +
            "</vxml>";

    @GetMapping("/helloworld")
    public String vxmlHelloWorld()
    {
        return VXML_HELLO_WORLD;
    }

    @GetMapping("/pin")
    public String vxmlPIN()
    {
        return VXML_PIN;
    }

}
