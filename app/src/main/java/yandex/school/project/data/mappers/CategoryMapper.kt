package yandex.school.project.data.mappers

import yandex.school.project.data.models.Category as DataCategory
import yandex.school.project.domain.entities.Category as DomainCategory

object CategoryMapper {
    fun mapToDomain(dataCategory: DataCategory): DomainCategory {
        return DomainCategory(
            id = dataCategory.id,
            name = dataCategory.name,
            isIncome = dataCategory.isIncome,
            color = null,
            icon = dataCategory.emoji
        )
    }
    
    fun mapToData(domainCategory: DomainCategory): DataCategory {
        return DataCategory(
            id = domainCategory.id,
            name = domainCategory.name,
            emoji = domainCategory.icon ?: "",
            isIncome = domainCategory.isIncome
        )
    }
} 