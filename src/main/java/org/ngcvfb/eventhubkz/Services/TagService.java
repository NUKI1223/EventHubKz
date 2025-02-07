package org.ngcvfb.eventhubkz.Services;

import lombok.RequiredArgsConstructor;
import org.ngcvfb.eventhubkz.Models.Tag;
import org.ngcvfb.eventhubkz.Repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public Tag createTag(String name) {
        Tag tag = Tag.builder().name(name).build();
        return tagRepository.save(tag);
    }

    public List<Tag> createTags(List<String> names) {
        List<Tag> tags = new ArrayList<>();
        for (String name : names) {
            tags.add(createTag(name));
        }
        return tags;
    }

    public Tag findOrCreateTag(String name) {
        return tagRepository.findByName(name)
                .orElseGet(() -> createTag(name));
    }
    public List<Tag> findTag(List<String> name) {
        return tagRepository.findByNameIn(name);
    }
}
