package ru.vsu.cs.iachnyi_m_a.gigaguide.mobile.dto.mapper

import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.dto.CoordinatesDTO
import ru.vsu.cs.iachnyi_m_a.gigaguide.admin.model.MapPoint


class CoordinatesDTOMapper: Mapper<CoordinatesDTO, MapPoint> {
    override fun map(value: CoordinatesDTO): MapPoint {
        return MapPoint(value.latitude, value.longitude)
    }
}