package eclpz.chatzy;

import java.util.regex.Pattern;

/**
 * Login class handles user registration, validation rules, 
 * and authentication logic based on assessment rubric specifications.
 */
public class Login {
    private String registeredUsername;
    private String registeredPassword;
    private String firstName;
    private String lastName;

    public Login() {}

    public Login(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Checks if the username contains an underscore and is no more than 5 characters long.
     * @param username The username to validate.
     * @return true if valid, false otherwise.
     */
    public boolean checkUserName(String username) {
        if (username == null) {
            return false;
        }
        return username.contains("_") && username.length() <= 5;
    }

    /**
     * Checks if the password meets complexity requirements:
     * - At least 8 characters long
     * - Contains a capital letter
     * - Contains a number
     * - Contains a special character
     * @param password The password to validate.
     * @return true if valid, false otherwise.
     */
    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        boolean hasCapital = Pattern.compile("[A-Z]").matcher(password).find();
        boolean hasNumber = Pattern.compile("[0-9]").matcher(password).find();
        boolean hasSpecial = Pattern.compile("[^a-zA-Z0-9]").matcher(password).find();

        return hasCapital && hasNumber && hasSpecial;
    }

    /**
     * Validates that cell phone number contains international code and total length is no more than 10 characters.
     * @param cellNumber Cell phone number string to check.
     * @return true if valid, false otherwise.
     */
    public boolean checkCellPhoneNumber(String cellNumber) {
        if (cellNumber == null) {
            return false;
        }
        // Must start with international code (e.g., '+') and total length <= 10 (or matching international format regex)
        // Regex checks for international prefix (e.g. starting with + or country code) and max 10 chars
        return cellNumber.matches("^\\+[0-9]{1,9}$") && cellNumber.length() <= 10;
    }

    /**
     * Registers user details if username, password, and cell phone validation pass.
     * @param username Username to register
     * @param password Password to register
     * @param cellNumber Cell phone number to register
     * @return Status message regarding registration success or failure.
     */
    public String registerUser(String username, String password, String cellNumber) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber(cellNumber)) {
            return "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.";
        }

        this.registeredUsername = username;
        this.registeredPassword = password;
        return "Username successfully captured.\nPassword successfully captured.\nCell phone number successfully added.";
    }

    /**
     * Verifies that login details entered match stored registration details.
     * @param username Entered username
     * @param password Entered password
     * @return true if credentials match, false otherwise.
     */
    public boolean loginUser(String username, String password) {
        if (this.registeredUsername == null || this.registeredPassword == null) {
            return false;
        }
        return this.registeredUsername.equals(username) && this.registeredPassword.equals(password);
    }

    /**
     * Returns necessary messaging for successful or failed login.
     * @param isLoggedIn Login verification status flag
     * @return Welcome message on success or error message on failure.
     */
    public String returnLoginStatus(boolean isLoggedIn) {
        if (isLoggedIn) {
            return "Welcome " + (firstName != null ? firstName : "<user first name>") + " ," + (lastName != null ? lastName : "<user last name>") + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }

    // Getters and Setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
}
