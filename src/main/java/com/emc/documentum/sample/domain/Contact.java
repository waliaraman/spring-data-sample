package com.emc.documentum.sample.domain;

import com.emc.documentum.springdata.entitymanager.mapping.DctmAttribute;
import com.emc.documentum.springdata.entitymanager.mapping.DctmEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import java.util.List;

/**
 * Contact domain object represents contact repository object
 *
 * @author Simon O'Brien
 */
@DctmEntity(repository = "contact")
@Entity(name="contact")
public class Contact {

	@Id
	protected String id;

	@DctmAttribute(value="object_name")
	private String name;

	private String email;

	private String telephone;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

}
