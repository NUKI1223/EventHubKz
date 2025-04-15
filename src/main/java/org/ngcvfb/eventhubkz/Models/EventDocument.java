package org.ngcvfb.eventhubkz.Models;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.Set;

@Document(indexName = "events")
@Getter
@Setter
public class EventDocument {
    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String title;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String description;

    @Field(type = FieldType.Text, analyzer = "standard")
    private String avatarUrl;

    @Field(type = FieldType.Keyword)
    private Set<String> tags;

    @Field(type = FieldType.Keyword)
    private String location;

    @Field(type = FieldType.Date,  format = DateFormat.strict_date_hour_minute)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime eventDate;

    public EventDocument() {
    }

    public EventDocument(String id, String title, String description, Set<String> tags, String location, LocalDateTime eventDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.location = location;
        this.eventDate = eventDate;
    }
}
