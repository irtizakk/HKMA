package com.housekeepingmanagingapp.models;

/**
 * Created by hp on 6/29/2017.
 */

public class UserModel
{
  private String email;
  private String password;

    private String role;
    private String userId;


    public UserModel()
    {

    }
    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }



}
