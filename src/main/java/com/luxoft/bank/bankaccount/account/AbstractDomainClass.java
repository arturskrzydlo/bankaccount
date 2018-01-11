package com.luxoft.bank.bankaccount.account;

import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * Created by jt on 12/16/15.
 */
@MappedSuperclass
@EqualsAndHashCode(exclude = {"version"}) class AbstractDomainClass {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    @Version
    private Integer version;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

}
