package com.user.music.transcript.web.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user")
public class UserData extends BaseEntity{

    @Indexed(unique=true)
    private Long userId;

    private String userName;
    private String userPassword;
    private String email;
}
