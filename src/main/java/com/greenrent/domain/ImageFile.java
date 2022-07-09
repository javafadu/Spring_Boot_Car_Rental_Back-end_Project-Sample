package com.greenrent.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table(name="tbl_imagefile")
@Entity
public class ImageFile {

    @Id
    @GeneratedValue(generator="uuid")
    @GenericGenerator(name="uuid", strategy="uuid2")
    private String id;

    private String name;

    private String type;

    @JsonIgnore
    @Lob
    private byte[] data;

    public ImageFile(String name, String type,byte[] data) {
        this.name=name;
        this.type=type;
        this.data=data;
    }

}