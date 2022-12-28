package ru.birthdaybot.model.entities

import java.io.Serializable
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @Column(name = "user_id")
    // telegramid
    var id: Long? = null,

    @Column(name = "birthday")
    var birthday: LocalDate? = null,


    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    var teams: MutableList<Team> = mutableListOf(),

    @Column(name = "fio")
    var fio: String? = null
) : Serializable

