package com.example.activitytracker.validators;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validators {

    public static boolean isValidEmail(String email)
    {
        if (email == null)
            return false;
        Pattern pat = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\."+
                                        "[a-zA-Z0-9_+&*-]+)*@" +
                                        "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                                        "A-Z]{2,7}$");
        return pat.matcher(email).matches();
    }
    public static boolean isValidUsername(String username) {
        if (username == null) return false;

        Pattern p = Pattern.compile("^[A-Za-z]\\w{5,29}$");
        Matcher m = p.matcher(username);
        return m.matches();
    }

    public static boolean isValidPassword(String password) {
        if (!((password.length() >= 8) && (password.length() <= 15))) return false;

        if (password.contains(" ")) return false;

        int count = 0;
        for (int i = 0; i <= 9; i++) {
            if (password.contains(Integer.toString(i))) count = 1;
        }
        if (count == 0)
            return false;

        return true;
    }

    public static boolean isEditTextFilled(EditText editText) {
        if (editText.getText().toString().isEmpty()) {
            editText.setError(editText.getHint() + " is required");
            return false;
        }
        return true;
    }
}
