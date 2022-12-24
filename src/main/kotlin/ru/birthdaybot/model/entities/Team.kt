package ru.birthdaybot.model.entities

import javax.persistence.*

@Entity
@Table(name = "team")
data class Team(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var groupid: Int? = null,

    @Column
    var name: String? = null,

    @Column(name = "default_text")
    var defaultText: String? = null,

    @Column(name = "description", nullable = true)
    var description: String
)
