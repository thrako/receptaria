package dev.thrako.receptaria.common.constant;

import java.time.Duration;

public enum Constants {
    ;
    public static final String BINDING_RESULT_PATH = "org.springframework.validation.BindingResult.";
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String FORMAT_USER_WITH_EMAIL_NOT_FOUND = "User with email: %s not found!";
    public static final String FORMAT_NO_SUCH_TEMP_PHOTO = "Temporary photo with id: %d does not exist.";
    public static final Duration MAX_ALLOWED_TEMP_PHOTO_DURATION = Duration.ofHours(24);

    public enum Registration {

        ;
        public static final int MIN_DISPLAY_NAME_LENGTH = 3;
        public static final int MAX_DISPLAY_NAME_LENGTH = 30;
        public static final int MIN_PASSWORD_LENGTH = 4;

        public static final String EMAIL_REG_EXP = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";

        public static final String NOT_BLANK_OR_EMPTY = "should not be blank or empty.";
        public static final String MSG_FIRST_NAME_BLANK_OR_EMPTY = "First Name " + NOT_BLANK_OR_EMPTY;
        public static final String MSG_LAST_NAME_BLANK_OR_EMPTY = "Last Name " + NOT_BLANK_OR_EMPTY;
        public static final String MSG_DISPLAY_NAME_BLANK_OR_EMPTY = "Display Name " + NOT_BLANK_OR_EMPTY;
        public static final String MSG_MIN_DISPLAY_NAME = "Display Name length should be minimum " + MIN_DISPLAY_NAME_LENGTH + " characters.";
        public static final String MSG_MAX_DISPLAY_NAME = "Display Name length should be maximum " + MAX_DISPLAY_NAME_LENGTH + " characters.";
        public static final String MSG_NOT_WELL_FORMATTED_EMAIL = "Please provide well formatted email!";
        public static final String MSG_EMAIL_NOT_AVAILABLE = "There is existing registration with this email.";
        public static final String MSG_PASSWORD_EMPTY = "Password should not be empty.";
        public static final String MSG_MIN_PASSWORD_LENGTH = "Password length should be minimum " + MIN_PASSWORD_LENGTH + " characters.";

    }

}
