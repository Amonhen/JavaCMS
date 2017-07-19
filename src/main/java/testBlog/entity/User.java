package testBlog.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    private int id;
    private String userName;
    private String email;
    private String fullName;
    private String password;
    private String profilePictureBase64String;
    private String userInfo;
    private Set<Role> roles;
    private Set<Article> articles;
   // private Set<Friend> userFriends;

    public User(String email, String fullName, String password,String userName,String profilePictureBase64,String userInfo) {
        this.userName = userName;
        this.email = email;
        this.fullName = fullName;
        this.password = password;
        this.profilePictureBase64String = profilePictureBase64;
        this.userInfo = userInfo;
        this.roles = new HashSet<>();
        this.articles = new HashSet<>();
       // this.userFriends = new HashSet<>();
    }

    public User() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "email", unique = true, nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "fullname", nullable = false)
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Column(name = "password", length = 60, nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "profile_pic",length = 65545)
    public String getProfilePictureBase64String() {
        return profilePictureBase64String;
    }

    public void setProfilePictureBase64String(String profilePictureBase64String) {
        this.profilePictureBase64String = profilePictureBase64String;
    }

    @Column(name = "username",nullable = false)
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "user_info")
    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles")
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    @OneToMany(mappedBy = "author")
    public Set<Article> getArticles() {
        return articles;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }

//    @ManyToMany()
//    @JoinColumn(table = "user_friends")
//    public Set<Friend> getUserFriends() {
//        return userFriends;
//    }
//
//    public void setUserFriends(Set<Friend> userFriends) {
//        this.userFriends = userFriends;
//    }


    @Transient
    public boolean isAdmin() {
        return this.getRoles()
                .stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
    }

    @Transient
    public boolean isAuthor(Article article) {
        return Objects.equals(this.getId(),article.getAuthor().getId());
    }
}