package com.android.gotonotes.utils;

import java.util.regex.Pattern;

public class PasswordCheck {
    public static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[0-9])"
                    + "(?=.*[a-z])(?=.*[A-Z])"
                    + "(?=.*[@#$%^&+=])"
                    + "(?=\\S+$).{8,20}$");
}
