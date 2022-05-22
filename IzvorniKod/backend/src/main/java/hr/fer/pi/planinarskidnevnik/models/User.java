package hr.fer.pi.planinarskidnevnik.models;

import com.fasterxml.jackson.annotation.*;
import hr.fer.pi.planinarskidnevnik.models.MountainLodgeUserArchive.MountainLodgeUserArchive;
import hr.fer.pi.planinarskidnevnik.models.MountainPathUserArchive.MountainPathUserArchive;
import hr.fer.pi.planinarskidnevnik.models.UserBadge.UserBadge;
import hr.fer.pi.planinarskidnevnik.models.friendships.FriendshipRequest;
import hr.fer.pi.planinarskidnevnik.models.friendships.Friendships;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "user", schema = "public")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "user_id_gen")
    @SequenceGenerator(name = "user_id_gen", sequenceName = "user_id_seq", allocationSize = 1)
    private Long id;

    @Size(max = 50, message = "Ime mora biti kraće od 50 znakova")
    @NotEmpty(message = "Unos imena je obavezan.")
    private String name;

    @Size(max = 50, message = "E-mail mora biti kraći od 50 znakova")
    @NotEmpty(message = "Unos e-maila je obavezan.")
    @Email(message = "Email mora biti u zadovoljavajućem formatu.")
    @Column(unique = true)
    private String email;

    @Size(max = 128, message = "E-mail smije sadržavati najviše 128 znakova.")
    private String placeOfResidence;

    private Date dateOfBirth;

    @Size(max = 2048, message = "Opis smije sadržavati najviše 2048 znakova.")
    private String description;

    @Size(max = 128, message = "Lozinka mora biti kraća od 50 znakova")
    @NotEmpty(message = "Unos lozinke je obavezan.")
    private String password;

    private byte[] image;

    @ManyToOne
    @JoinColumn(nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Role role;

    @ManyToMany
    @JoinTable(
            name = "friendships",
            joinColumns = @JoinColumn(name = "current_user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<User> friends;

    @ManyToMany
    @JoinTable(
            name = "event_attendance",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private List<User> participatedEvents;

    @ManyToMany
    @JoinTable(
            name = "friendship_request",
            joinColumns = @JoinColumn(name = "sender"),
            inverseJoinColumns = @JoinColumn(name = "receiver")
    )
    private List<User> friendRequests;

    @ManyToMany
    @JoinTable(
            name = "friendships_notifications",
            joinColumns = @JoinColumn(name = "friendship_request_sender"),
            inverseJoinColumns = @JoinColumn(name = "friendship_request_receiver")
    )
    private List<User> friendRequestsNotifications;

    @OneToMany(mappedBy = "user")
    private List<MountainPathGrade> mountainPathGradeList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserBadge> userBadgeList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<MountainLodgeUserArchive> mountainLodgeUserArchive = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<MountainPathUserArchive> mountainPathUserArchive = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "path_user_wishlist",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "path_id"))
    List<MountainPath> pathsWishlist = new ArrayList<>();

    public List<User> getParticipatedEvents() {
        return participatedEvents;
    }

    public void setParticipatedEvents(List<User> participatedEvents) {
        this.participatedEvents = participatedEvents;
    }

    @JsonIgnore
    public List<MountainPath> getPathsWishlist() {
        return pathsWishlist;
    }

    public void setPathsWishlist(List<MountainPath> pathsWishlist) {
        this.pathsWishlist = pathsWishlist;
    }

    @JsonIgnore
    public List<MountainPathUserArchive> getMountainPathUserArchive() {
        return mountainPathUserArchive;
    }

    public void setMountainPathUserArchive(List<MountainPathUserArchive> mountainPathUserArchive) {
        this.mountainPathUserArchive = mountainPathUserArchive;
    }

    @JsonIgnore
    public List<MountainLodgeUserArchive> getMountainLodgeUserArchive() {
        return mountainLodgeUserArchive;
    }

    public void setMountainLodgeUserArchive(List<MountainLodgeUserArchive> mountainLodgeUserArchive) {
        this.mountainLodgeUserArchive = mountainLodgeUserArchive;
    }

    public User() {
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User(String name, String password, String email, String placeOfResidence, Date dateOfBirth, String description, byte[] image) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.placeOfResidence = placeOfResidence;
        this.dateOfBirth = dateOfBirth;
        this.description = description;
        this.image = image;
    }

    public User(String name, String password, String email, String placeOfResidence, Date dateOfBirth, String description, byte[] image, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.placeOfResidence = placeOfResidence;
        this.dateOfBirth = dateOfBirth;
        this.description = description;
        this.password = password;
        this.image = image;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getPlaceOfResidence() {
        return placeOfResidence;
    }

    public void setPlaceOfResidence(String placeOfResidence) {
        this.placeOfResidence = placeOfResidence;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<MountainPathGrade> getMountainPathGradeList() {
        return mountainPathGradeList;
    }

    public void setMountainPathGradeList(List<MountainPathGrade> mountainPathGradeList) {
        this.mountainPathGradeList = mountainPathGradeList;
    }

    @JsonIgnore
    public List<UserBadge> getUserBadgeList() { return userBadgeList; }

    public void setUserBadgeList(List<UserBadge> userBadgeList) { this.userBadgeList = userBadgeList; }

    public List<User> getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(List<User> friendRequest) {
        this.friendRequests = friendRequest;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public List<User> getFriendRequestsNotifications() {
        return friendRequestsNotifications;
    }

    public void setFriendRequestsNotifications(List<User> friendRequestsNotifications) {
        this.friendRequestsNotifications = friendRequestsNotifications;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) &&
                name.equals(user.name) &&
                email.equals(user.email) &&
                password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, password);
    }
}
