package com.dailywords.resource.domain.entity;

import com.dailywords.resource.domain.Result;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonStringType.class),
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class Word implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    private String word;

    @Type(type = "jsonb")
    @Column(name = "results", columnDefinition = "jsonb")
    private List<Result> results;

    @Column(name = "definition")
    private String definition;

    @Column(name = "part_of_speech")
    private String partOfSpeech;

    @Type(type = "jsonb")
    @Column(name = "synonyms", columnDefinition = "jsonb")
    private String[] synonyms;

    @Type(type = "jsonb")
    @Column(name = "typeof", columnDefinition = "jsonb")
    private String[] typeOf;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Word word = (Word) o;
        return id != null && Objects.equals(id, word.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
