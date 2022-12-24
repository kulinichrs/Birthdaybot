package ru.birthdaybot.model.entities

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @Column(name = "id")
    // telegramid
    var id: String? = null,

    @Column(name = "birtday")
    var birtday: LocalDate?,

    @Column(name = "group_id", nullable = false)
    @ManyToMany(
        targetEntity = Team::class,
        mappedBy = "groupid",
        cascade = [CascadeType.ALL]
    )
    var groupid: List<Team>,

    @Column(name = "fio")
    var fio: String? = null,


    )

