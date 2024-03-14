package dev.luisjohann.checklist.infra.project.slug.memory;

import dev.luisjohann.checklist.domain.project.ISlugGenerator;

public class SlugGeneratorTest implements ISlugGenerator {

    @Override
    public String generateSlugy(String strName) {
        return strName.replaceAll(" ", "_").toLowerCase();
    }

}
