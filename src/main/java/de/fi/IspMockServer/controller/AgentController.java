package de.fi.IspMockServer.controller;


import de.fi.IspMockServer.Event;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/agent")
public class AgentController {

    @GetMapping("/grammar")
    public String grammar() {
        return "Grammatik-Beispiel";
    }

    @PostMapping("/event")
    public String event(@RequestBody Event event) {
        if (event.getType().equals(Event.RINGING)) {
            return "Es klingelt!";
        } else {
            return "Es macht was, was nicht Klingeln ist.";
        }
    }
}
