package org.ngcvfb.eventhubkz.Controllers;

import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Getting all tags")
    @GetMapping
    public List<Tag> getTags() {
        return  tagService.getAllTags();
    }

    @Operation(summary = "Creating one new tag")
    @PostMapping("/create")
    public Tag addTag(@RequestBody String name) {
        return tagService.createTag(name);
    }

    @Operation(summary = "Creating a list of new tags")
    @PostMapping("/create-branch")
    public List<Tag> addTags(@RequestBody List<String> tags) {
        return tagService.createTags(tags);
    }


}
