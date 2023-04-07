package ru.vorobyov.authorization.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewRefreshJwtRequest {
    private String refreshToken;
}
