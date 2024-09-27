package com.autocrypt.logtracer.v3;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class TestEntity {

    @Id
    @GeneratedValue
    public Long id;
}
