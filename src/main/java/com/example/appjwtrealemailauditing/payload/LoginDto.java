package com.example.appjwtrealemailauditing.payload;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginDto {

    @NotNull
    public String userName;

    @NotNull
    public String password;

}
