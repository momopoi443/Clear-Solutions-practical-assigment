package com.example.clearsolutionspracticalassigment.dto;

import com.example.clearsolutionspracticalassigment.entity.User;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private UUID uuid;

    private String email;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private String address;

    private String phoneNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User teacher)) return false;

        return getUuid().equals(teacher.getUuid());
    }

    @Override
    public int hashCode() {
        return getUuid().hashCode();
    }
}
