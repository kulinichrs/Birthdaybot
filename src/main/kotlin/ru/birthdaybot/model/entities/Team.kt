package ru.birthdaybot.model.entities

import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "team")
data class Team(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    var teamId: Int? = null,

    @Column
    var name: String? = null,

    @Column(name = "default_text")
    var defaultText: String? = null,

    @Column(name = "description", nullable = true)
    var description: String? = null,

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinTable(name = "team_user",
        joinColumns = [JoinColumn(name = "team_id")],
        inverseJoinColumns = [JoinColumn(name = "user_id")])
    var users: MutableList<User> = mutableListOf()
) : Serializable
