package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record UserJson(@JsonProperty("id") UUID id,
                       @JsonProperty("username") String username,
                       @JsonProperty("currency") CurrencyValues currency,
                       @JsonProperty("firstname") String firstname,
                       @JsonProperty("surname") String surname,
                       @JsonProperty("fullname") String fullname,
                       @JsonProperty("photo") byte[] photo,
                       @JsonProperty("photoSmall") byte[] photoSmall) {
}
