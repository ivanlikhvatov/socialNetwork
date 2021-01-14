package com.example.socialNetwork.domain;

public final class Views {
    public interface Id{}

    public interface IdName extends Id{}

    public interface FullProfile extends IdName {}

    public interface FullMessage extends IdName, FullProfile{}

    public interface SecureProfile extends FullProfile {}

    public interface FullGroup extends IdName, FullMessage{}
}
