package hr.fer.pi.planinarskidnevnik.controllers;

import hr.fer.pi.planinarskidnevnik.dtos.CommunityEvent.CommunityEventDto;
import hr.fer.pi.planinarskidnevnik.dtos.CommunityEvent.EventAttendanceParticipatingResponse;
import hr.fer.pi.planinarskidnevnik.dtos.CommunityEvent.PreviewCommunityEventDto;
import hr.fer.pi.planinarskidnevnik.models.CommunityEvent;
import hr.fer.pi.planinarskidnevnik.models.User;
import hr.fer.pi.planinarskidnevnik.services.impl.CommunityEventService;
import hr.fer.pi.planinarskidnevnik.services.impl.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("event")
public class CommunityEventController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final CommunityEventService eventService;
    private final UserService userService;

    public CommunityEventController(CommunityEventService eventService, UserService userService) {
        this.eventService = eventService;
        this.userService = userService;
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getEventById(@PathVariable("id") final Long eventId) {
        LOGGER.info("User fetching");
        final CommunityEvent communityEvent = eventService.getEventById(eventId);
        return ResponseEntity.ok(communityEvent);
    }

    @PostMapping
    public ResponseEntity<?> createEvent(@Valid @RequestBody final CommunityEventDto dto, Principal principal) {
        LOGGER.info("Event creating");
        CommunityEvent event = eventService.createEvent(dto, principal);
        return ResponseEntity.status(HttpStatus.CREATED).body("Uspješno stvoren događaj: " + event.getName());
    }

    @GetMapping("/all")
    public ResponseEntity<List<PreviewCommunityEventDto>> getAllEvents(Principal principal) {
        LOGGER.info("Fetching all events");
        return ResponseEntity.ok(eventService.getMyCommunityEvents(principal));
    }

    @GetMapping("/by-author")
    public ResponseEntity<List<PreviewCommunityEventDto>> getEventsByAuthor(Principal principal) {
        LOGGER.info("Fetching all events by author");
        return ResponseEntity.ok(eventService.getArchivedAuthoredEvents(principal));
    }

    @PatchMapping("/participate/{event_id}")
    public ResponseEntity<EventAttendanceParticipatingResponse> participateOnEvent(@PathVariable("event_id") Long eventId, Principal principal) {
        return ResponseEntity.ok(eventService.particiapteOnEvent(eventId, principal));
    }

    @PatchMapping("/departicipate/{event_id}")
    public ResponseEntity<EventAttendanceParticipatingResponse> departicipateOnEvent(@PathVariable("event_id") Long eventId, Principal principal) {
         return ResponseEntity.ok(eventService.deParticiapteOnEvent(eventId, principal));
    }
}