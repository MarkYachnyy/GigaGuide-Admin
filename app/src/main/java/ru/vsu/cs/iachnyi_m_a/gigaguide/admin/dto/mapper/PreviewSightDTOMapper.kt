package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.mapper

import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.sight.PreviewSightDTO
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.model.SightTourThumbnail
import ru.vsu.cs.iachnyi_m_a.gigaguide.mobile.dto.mapper.Mapper

class PreviewSightDTOMapper: Mapper<PreviewSightDTO, SightTourThumbnail> {
    override fun map(value: PreviewSightDTO): SightTourThumbnail {
        return SightTourThumbnail(sightId = value.id.toLong(), imageLink = value.imagePath, rating = value.rating, name = value.name, proximity = 0f)
    }

}