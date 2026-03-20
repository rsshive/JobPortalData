package com.jobportal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "administrators")
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@SuperBuilder
public class Admin extends User {
    // The ERD only lists admin_id which is essentially mapped 
    // to the user_id in the connected User superclass via JOINED strategy.
    // No specific fields defined in the specs.
}
