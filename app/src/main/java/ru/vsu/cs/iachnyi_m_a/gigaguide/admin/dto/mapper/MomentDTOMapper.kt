package ru.vsu.cs.iachnyi_m_a.gigaguide.mobile.dto.mapper

import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.moment.MomentDTO
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.model.MomentInfo
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.util.ServerUtils


class MomentDTOMapper: Mapper<MomentDTO, MomentInfo> {
    override fun map(value: MomentDTO): MomentInfo {
        return MomentInfo(id = value.id, name = value.name, imagePath = ServerUtils.imageLink(value.imagePath))
    }
}