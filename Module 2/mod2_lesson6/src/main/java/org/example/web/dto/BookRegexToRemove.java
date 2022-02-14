package org.example.web.dto;

import javax.validation.constraints.Pattern;

public class BookRegexToRemove {

    @Pattern(regexp = "(author:\\s.+)|(title:\\s.+)|(size:\\s\\p{Digit}+)")
    private String regex;

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

}
