package org.example.backend.ai;

import org.example.backend.ai.dto.MonologueResponse;
import org.example.backend.user.CurrentUser;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/pets")
public class AiDialogueController {

    private final AiPetDialogueService dialogueService;
    private final CurrentUser currentUser;

    public AiDialogueController(AiPetDialogueService dialogueService, CurrentUser currentUser) {
        this.dialogueService = dialogueService;
        this.currentUser = currentUser;
    }

    @GetMapping("/{id}/monologue")
    public MonologueResponse monologue(@PathVariable Long id, Principal principal) {
        return new MonologueResponse(
                dialogueService.generateMonologue(id, currentUser.from(principal))
        );
    }
}