package yandex.school.project.domain.entities

data class Category(
    val id: Int,
    val name: String,
    val isIncome: Boolean,
    val color: String? = null,
    val icon: String? = null
) 