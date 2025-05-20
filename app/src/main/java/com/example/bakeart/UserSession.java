package com.example.bakeart;

public class UserSession {
    private static String email;

    public static void login(String userEmail) {
        email = userEmail;
    }

    public static void logout() {
        email = null;
    }

    public static String getEmail() {
        return email;
    }

    public static boolean isLoggedIn() {
        return email != null;
    }
}
