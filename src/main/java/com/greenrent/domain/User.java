package com.greenrent.domain;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="tbl_user")
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable=false)
    private String firstName;


    @Column(length = 50, nullable=false)
    private String lastName;


    @Column(length=20,nullable = false,unique = true)
    private String email;

    @Column(length =120,nullable=false )
    private String password;


    @Column(length = 14,nullable = false)
    private String phoneNumber;

    @Column(length = 100,nullable = false)
    private String address;

    @Column(length=15,nullable = false)
    private String zipCode;

    // degistirilebilir veya degistirilemez (false ise degistirilebilir demek)
    @Column(nullable = false)
    private Boolean builtIn=false;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="tbl_user_roles",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<Role> roles=new HashSet<>();



}