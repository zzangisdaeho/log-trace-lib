package com.autocrypt.logtracer.v3;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;


@Entity
@Getter
public class TestEntity {

    @Id
    @GeneratedValue
    public Long id;
}
