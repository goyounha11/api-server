package com.server.common.member.domain;

import com.server.common.support.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.OffsetDateTime;

import static exception.Preconditions.require;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name="uq_user_id", columnNames = "user_id")
        }
)
public class Customer extends BaseEntity {

    /* 사용자 아이디 */
    @Column(length = 15, nullable = false)
    private String user_id;


    @Column(length = 256, nullable = false)
    private String password;

    /* 사용자 성 */
    @Column(length = 2, nullable = false)
    private String last_name;

    /* 사용자 이름 */
    @Column(length = 8, nullable = false)
    private String first_name;

    /* 사용자 생일일 */
    @Column(length = 8, nullable = false)
   private String birthday;

    /* 사용자 성별 */
    @Column(length = 3, nullable = false)
    private String gender;

    /* 사용자 국가 번호 */
    @Column(length = 3, nullable = false)
    private String country_number = "82";

    /* 사용자 국가 번호 코드 */
    @Column(length = 3, nullable = false)
    private String country_code = "410";

    /* 사용자 국가 */
    @Column(length = 3, nullable = false)
    private CountryType country_type = CountryType.KR;

    /* 사용자 상태 */
    @Column(length = 10, nullable = false)
    private MemberStatus status = MemberStatus.CREATED;

    private OffsetDateTime createdAt = OffsetDateTime.now();

    public Customer(String user_id, String password, String last_name, String first_name, String birthday, String gender) {
        require(Strings.isNotBlank(user_id));
        require(Strings.isNotBlank(password));
        require(Strings.isNotBlank(last_name));
        require(Strings.isNotBlank(first_name));
        require(Strings.isNotBlank(birthday));
        require(Strings.isNotBlank(gender));

        this.user_id = user_id;
        this.password = password;
        this.last_name = last_name;
        this.first_name = first_name;
        this.birthday = birthday;
        this.gender = gender;
    }
}
