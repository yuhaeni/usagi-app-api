package security.oauth

import com.kou.kouappapi.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User

class CustomOAuth2User(
    val user: User,
    private val attributes: Map<String, Any>
) : OAuth2User {


    /**
     * OAuth2 제공자가 제공한 사용자 속성
     * ex) Google: sub, email, name, picture
     */
    override fun getAttributes(): Map<String, Any> = attributes

    /**
     * 사용자 권한 목록
     * TODO 현재는 모든 사용자에게 ROLE_USER 부여 -> 추후 수정 필요
     */
    override fun getAuthorities(): Collection<GrantedAuthority?>? {
        return listOf(SimpleGrantedAuthority("ROLE_USER"))
    }

    /**
     * 사용자 식별자 (기본 키)
     */
    override fun getName(): String = user.id.toString()
}
