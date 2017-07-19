package testBlog.bindingModel;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

public class UserBindingModel {
    @NotNull
    private int id;
    @NotNull
    private String userName;
    private MultipartFile profilePictureBase64;
    private String userInfo;
    @NotNull
    private String email;
    @NotNull
    private String fullName;
    @NotNull
    private String password;
    @NotNull
    private String confirmPassword;
    private String friendsInSerArray;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public MultipartFile getProfilePictureBase64() {
        return profilePictureBase64;
    }

    public void setProfilePictureBase64(MultipartFile profilePictureBase64) {
        this.profilePictureBase64 = profilePictureBase64;
    }

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFriendsInSerArray() {
        return friendsInSerArray;
    }

    public void setFriendsInSerArray(String friendsInSerArray) {
        this.friendsInSerArray = friendsInSerArray;
    }
}