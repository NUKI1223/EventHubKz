package org.ngcvfb.eventhubkz.Services;

import lombok.RequiredArgsConstructor;
import org.ngcvfb.eventhubkz.Models.Tag;
import org.ngcvfb.eventhubkz.Repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public Tag createTag(String name) {
        Tag tag = Tag.builder().name(name).build();
        Optional<Tag> existedTag =  tagRepository.findByName(name);
        if (existedTag.isPresent()) {
            return existedTag.get();
        }
        return tagRepository.save(tag);
    }

    public List<Tag> createTags(List<String> names) {
        List<Tag> tags = new ArrayList<>();
        for (String name : names) {
            tags.add(createTag(name));
        }
        return tags;
    }

    public Tag findTag(String name) {
        Optional<Tag> tag =  tagRepository.findByName(name);
        if (tag.isPresent()) {
            return tag.get();
        }
        return null;
    }

    public Set<Tag> findTags(List<String> names) {
        List<Tag> tags = new ArrayList<>();
        for (String name :names) {
            Optional<Tag> tag =  tagRepository.findByName(name);
            if (tag.isPresent()) {
                tags.add(tag.get());
            }
        }
        return new HashSet<>(tags);
    }

    public Set<Tag> findOrCreateTags(List<String> names) {
        Set<Tag> tags = new HashSet<>();
        for (String name : names) {
            Tag tag = findTag(name);
            if (tag != null) {
                tags.add(tag);
            }
        }
        return tags;
    }
}
