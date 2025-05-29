package ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.mapper

import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.sight.SightDTO
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.model.SightInfo
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.util.ServerUtils
import ru.vsu.cs.iachnyi_m_a.gigaguide.mobile.dto.mapper.Mapper

class SightDTOMapper : Mapper<SightDTO, SightInfo> {
    override fun map(value: SightDTO): SightInfo {
        return SightInfo(
            id = value.id,
            name = value.name,
            description = value.description,
            time = 30,
            imageLink = ServerUtils.imageLink(value.imagePath),
            city = value.city
        )
    }
}
