package org.ngcvfb.eventhubkz.Models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.Set;

@Document(indexName = "events")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventDocument {
    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "english")
    private String titleEnglish;

    @Field(type = FieldType.Text, analyzer = "russian")
    private String titleRussian;

    @Field(type = FieldType.Text, analyzer = "custom_kazakh")
    private String titleKazakh;

    @Field(type = FieldType.Text, analyzer = "english")
    private String descriptionEnglish;

    @Field(type = FieldType.Text, analyzer = "russian")
    private String descriptionRussian;

    @Field(type = FieldType.Text, analyzer = "custom_kazakh")
    private String descriptionKazakh;

    @Field(type = FieldType.Keyword)
    private Set<String> tags;

    @Field(type = FieldType.Keyword)
    private String location;

    @Field(type = FieldType.Date)
    private LocalDateTime eventDate;


}
