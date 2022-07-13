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

@Table(name="tbl_imagefile")  // db deki table name tbl_imagefile olacak
@Entity
public class ImageFile {

    @Id
    @GeneratedValue(generator="uuid")
    @GenericGenerator(name="uuid", strategy="uuid2")
    // uuid : universal unique identify
    // uuid2 : daha gelismis bir yapiya sahip
    private String id;

    private String name;

    private String type;

    @JsonIgnore // image bilgilerini disari cikarmak istemiyorsak
    @Lob // image leri byte olarak tutmak istiyoruz
    // resimleri veritabaninda tutabiliyoruz.
    // image in datasini tutuyoruz.
    private byte[] data;


    // Constructor, ozel bir constructor id haric
    public ImageFile(String name, String type,byte[] data) {
        this.name=name;
        this.type=type;
        this.data=data;
    }

}