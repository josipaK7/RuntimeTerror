package hr.fer.pi.planinarskidnevnik.controllers;

import hr.fer.pi.planinarskidnevnik.dtos.User.*;
import hr.fer.pi.planinarskidnevnik.dtos.MountainLodgeArchive.MountainLodgeArchiveResponse;
import hr.fer.pi.planinarskidnevnik.dtos.MountainPath.MountainPathGradeResponse;
import hr.fer.pi.planinarskidnevnik.dtos.MountainPathArchiveResponse;
import hr.fer.pi.planinarskidnevnik.dtos.User.UserCreateDto;
import hr.fer.pi.planinarskidnevnik.dtos.User.UserHeaderDto;
import hr.fer.pi.planinarskidnevnik.dtos.User.UserProfilePageDto;
import hr.fer.pi.planinarskidnevnik.dtos.User.UserSearchDto;
import hr.fer.pi.planinarskidnevnik.exceptions.LodgeAlreadyArchivedException;
import hr.fer.pi.planinarskidnevnik.exceptions.MountainPathAlreadyAddedAsFavouriteException;
import hr.fer.pi.planinarskidnevnik.models.MountainPath;
import hr.fer.pi.planinarskidnevnik.models.User;
import hr.fer.pi.planinarskidnevnik.services.impl.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("users")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") final Long userId) {
        LOGGER.info("User fetching");
        final User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/get-friends")
    public ResponseEntity<?> getUserCommunity(Principal principal) {
        LOGGER.info("User fetching by name");
        final List<UserSearchDto> list = userService.getUserCommunity(principal);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<?> getAllUsers(Principal principal) {
        LOGGER.info("User fetching by name");
        final List<UserSearchDto> list = userService.getAllUsers(principal);
        return ResponseEntity.ok(list);
    }

    @GetMapping("role/{id}")
    public ResponseEntity<?> getUserRoleById(@PathVariable("id") final Long userId) {
        LOGGER.info("User fetching");
        final User user = userService.getUserById(userId);
        return ResponseEntity.ok(userService.getRole(user.getEmail()));
    }

    @GetMapping("is-admin")
    public ResponseEntity<Boolean> getIsAdmin(Principal principal) {
        LOGGER.info("User fetching");
        return ResponseEntity.ok(userService.getRole(principal.getName()).equals("ADMIN"));
    }

    @GetMapping("profileOwner/{id}")
    public ResponseEntity<Boolean> isProfileOwner(@PathVariable("id") final Long profileId, Principal principal) {
        LOGGER.info("Checking if current user is profile owner");
        final Boolean isOwner = userService.isOwner(profileId, principal.getName());
        return ResponseEntity.ok(isOwner);
    }

    @GetMapping("/me")
    public ResponseEntity<UserHeaderDto> getCurrentUser(Principal principal) {
        LOGGER.info("Getting current user");
        return ResponseEntity.ok(userService.getHeaderInformation(principal));
    }
    @GetMapping("/user/role")
    public ResponseEntity<UserFooterDto> getCurrentUserRole(Principal principal) {
        LOGGER.info("Getting current user role");
        return ResponseEntity.ok(userService.getFooterInformation(principal));
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody final UserCreateDto dto) {
        LOGGER.info("User creating");
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(dto));
    }

    @GetMapping(value = "/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getCurrentUserImage(Principal principal) {
        return ResponseEntity.ok(userService.getImage(principal.getName()));
    }

    @PostMapping("/add-friend/{id}")
    public ResponseEntity<?> sendFriendRequest(Principal principal, @PathVariable("id") final Long receiverId) {
        userService.sendFriendRequest(principal.getName(), receiverId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/remove-friend/{id}")
    public ResponseEntity<?> removeFriend(Principal principal, @PathVariable("id") final Long friendRemovedId) {
        userService.removeFriend(principal, friendRemovedId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/friend-requests-received")
    public ResponseEntity<?> checkFriendRequests(Principal principal) {
        return ResponseEntity.ok(userService.checkFriendRequests(principal.getName()));
    }

    @PostMapping("/friend-request-accept/{id}")
    public ResponseEntity<?> acceptFriendRequest(Principal principal, @PathVariable("id") final Long senderId) {
        userService.acceptFriendRequest(principal, senderId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/friend-request-decline/{id}")
    public ResponseEntity<?> friendRequestDecline(Principal principal, @PathVariable("id") final Long senderId) {
        userService.friendRequestDecline(principal, senderId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/image/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImageById(@PathVariable("id") final Long id) {
        return ResponseEntity.ok(userService.getImage(userService.getUserById(id).getEmail()));
    }

    @GetMapping(value = "/profile/{id}")
    public ResponseEntity<UserProfilePageDto> getProfilePageInfo(@PathVariable("id") final Long profileId, Principal principal) {
        return ResponseEntity.ok(userService.getProfilePageInfo(profileId, principal));
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable("id") final Long userId, Principal principal) {
        LOGGER.info("User removing");
        userService.deleteUser(userId, principal);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/current")
    public ResponseEntity<UserCreateDto> editCurrentUser(@RequestBody UserCreateDto userCreateDto, Principal principal) {
        LOGGER.info("Current user editing");
        final User user = userService.editCurrentUser(userCreateDto, principal);
        return ResponseEntity.ok(new UserCreateDto(user.getName(), user.getPassword(), user.getEmail(), user.getPlaceOfResidence(), user.getDateOfBirth(), user.getDescription(), null));
    }

    @ExceptionHandler(LodgeAlreadyArchivedException.class)
    public final ResponseEntity<?> constraintsViolations(final Exception ex) {
        LOGGER.error("Constraint exception. " + ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @GetMapping(value = "/archived-lodges/all")
    public final ResponseEntity<List<MountainLodgeArchiveResponse>> getArchivedLodges(Principal principal) {
        List<MountainLodgeArchiveResponse> lodges = userService.getArchivedLodges(principal);
        return ResponseEntity.status(200).body(lodges);
    }

    @GetMapping(value = "/archived-paths/all")
    public final ResponseEntity<List<MountainPathArchiveResponse>> getArchivedPaths(Principal principal) {
        return ResponseEntity.status(200).body(userService.getArchivedPaths(principal));
    }

    @PatchMapping(value="/add/path-wish/{path_id}")
    public final ResponseEntity<String> addPathToWishList(@PathVariable("path_id") Long pathId, Principal principal) {
        try {
            var s = userService.addPathToWishList(principal, pathId);
            return ResponseEntity.ok("Staza: " + s.getName() + " uspješno dodana u favorite korisnika: " + principal.getName());
        } catch (DataIntegrityViolationException ex) {
            throw new MountainPathAlreadyAddedAsFavouriteException("Već ste dodali planinarsku stazu identifikatora: " + pathId + " u svoje favorite.");
        }

    }

    @GetMapping(value = "/graded-paths/all")
    public final ResponseEntity<List<MountainPathGradeResponse>> getGradedPaths(Principal principal) {
        return ResponseEntity.status(200).body(userService.getGradedPaths(principal));
    }

    @DeleteMapping(value="/delete/path-wish/{path_id}")
    public final ResponseEntity<String> deletePathFromWishList(@PathVariable("path_id") Long pathId, Principal principal) {
        var s = userService.deletePathFromWishlist(principal, pathId);
        return ResponseEntity.ok("Uspješno ste uklonili stazu: " + s.getName() + " s Vašega popisa želja.");
    }

    @GetMapping(value="/path-wishlist")
    public final ResponseEntity<List<Long>> getPathWishlist(Principal principal) {
        return ResponseEntity.ok(userService.getPathWishlistForUser(principal).stream().map(MountainPath::getId).collect(Collectors.toList()));
    }

    @GetMapping(value="/notifications")
    public final ResponseEntity<List<UserSearchDto>> getNotifications(Principal principal) {
        return ResponseEntity.ok(userService.getNotifications(principal));
    }

    @DeleteMapping(value="/notifications/{id}")
    public final ResponseEntity<?> removeNotifications(@PathVariable("id") Long removeFromUserId, Principal principal) {
        userService.removeNotifications(principal, removeFromUserId);
        return ResponseEntity.ok().build();
    }
}
