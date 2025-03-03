package org.ngcvfb.eventhubkz.Controllers;

import lombok.RequiredArgsConstructor;
import org.ngcvfb.eventhubkz.Models.Tag;
import org.ngcvfb.eventhubkz.Services.TagService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;
    @GetMapping
    public List<Tag> getTags() {
        return  tagService.getAllTags();
    }
    @PostMapping("/create")
    public Tag addTag(@RequestBody String name) {
        return tagService.createTag(name);
    }
    @PostMapping("/create-branch")
    public List<Tag> addTags(@RequestBody List<String> tags) {
        return tagService.createTags(tags);
        //TODO: handler for recurring tags
    }


}
