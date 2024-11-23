package com.pwr.gastromate.dto;
import com.pwr.gastromate.data.Address;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String email;
    private String username;
    @Size(min = 5, message = "Password contains min 5 characters")
    private String password;
    private Address address;
}
