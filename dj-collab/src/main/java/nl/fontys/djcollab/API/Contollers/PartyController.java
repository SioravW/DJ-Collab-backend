package nl.fontys.djcollab.API.Contollers;

import nl.fontys.djcollab.Domain.Models.Party;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class PartyController {

    private Party party = new Party(1, 5, "Rave");

    public List<Party> parties = new ArrayList<>();


    @PostMapping(value = "/party/")
    public Party AddParty(@RequestBody int ownerId, @RequestBody String name) {
        Party party = new Party(1, ownerId, name);
        parties.add(party);
        return party;
    }

    @GetMapping(value = "/party")
    public List<Party> GetAllParties() {
        parties.add(party);
        return parties;
    }

}