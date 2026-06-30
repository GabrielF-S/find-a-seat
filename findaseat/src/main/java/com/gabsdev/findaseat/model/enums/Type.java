package com.gabsdev.findaseat.model.enums;

public enum Type {
    SEAT(1L, "seat"),
    DESK(2L, "desk"),
    TABLE(3L, "table"),
    ROOM(4L, "room");

    Type() {
    }

    Type(long number, String type) {

    }
}
