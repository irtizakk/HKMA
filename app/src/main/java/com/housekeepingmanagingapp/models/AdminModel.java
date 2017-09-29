package com.housekeepingmanagingapp.models;

/**
 * Created by hp on 6/29/2017.
 */

public class AdminModel
{
    private String email;
    private String password;

    private String profilePic;
    private String userId;
    private String firstName;
    private String lastName;
    private String adminDetails;

    public AdminModel()
    {

    }
    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
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

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAdminDetails() {
        return adminDetails;
    }

    public void setAdminDetails(String adminDetails) {
        this.adminDetails = adminDetails;
    }

    private String contactNumber;


}
