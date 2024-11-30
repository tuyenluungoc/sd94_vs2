package org.example.sd_94vs1.model.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpsertBlogRequest {

    String title;
    String description;
    String content;
    Boolean status;
    String thumbnail;

}
