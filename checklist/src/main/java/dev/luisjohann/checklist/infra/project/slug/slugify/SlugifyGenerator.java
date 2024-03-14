package dev.luisjohann.checklist.infra.project.slug.slugify;

import com.github.slugify.Slugify;

import dev.luisjohann.checklist.domain.project.ISlugGenerator;

public class SlugifyGenerator implements ISlugGenerator {

    @Override
    public String generateSlugy(String strName) {
        return Slugify.builder()
                .underscoreSeparator(true)
                .build().slugify(strName);
    }

}
