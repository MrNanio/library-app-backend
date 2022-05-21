package com.nankiewic.libraryappbackend.model.View;

import java.time.LocalDateTime;

public interface BookBasicView {

    Long getId();

    String getTitle();

    String getAuthor();

    String getCategory();

    LocalDateTime getAddDate();

    String getPublishYear();

}
