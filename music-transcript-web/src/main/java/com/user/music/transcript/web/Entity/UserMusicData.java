package com.user.music.transcript.web.Entity;


import com.user.music.transcript.web.enums.Status;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "userMusicData")
@CompoundIndexes({
        @CompoundIndex(name = "userMusicDataIndex", def = "{ 'userId': 1, 'audioUrl': 1, 'transcriptUrl':1}")
})
@SuperBuilder
public class UserMusicData extends BaseEntity{

    private Long userId;
    private Status status;
    private String audioUrl;
    private String transcriptUrl;

}
