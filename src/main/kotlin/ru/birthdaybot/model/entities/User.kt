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
) : Serializable {


    override fun toString(): String {
        return "User(name: $fio, birthday: $birthday)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (birthday?.hashCode() ?: 0)
        result = 31 * result + teams.hashCode()
        result = 31 * result + (fio?.hashCode() ?: 0)
        return result
    }
}

