package com.gabsdev.findaseat.model;

public enum Status {
    AVALIABLE(1L, "avaliable"),
    RESERVED(2L, "reserved");

    private Long id;
    private String disponibility;


    Status(Long id, String disponibility) {
        this.id = id;
        this.disponibility = disponibility;
    }


}
