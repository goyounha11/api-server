package com.server.common.member.domain;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static com.server.common.member.domain.CountryType.KR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class CustomerTest {
    @Test
    void 소비자_생성_성공() {
        val customerMember = new Customer("admin", "123", "shin", "chulho", "19930115", "남");

        assertThat(customerMember.getUser_id()).isEqualTo("admin");
        assertThat(customerMember.getPassword()).isEqualTo("123");
        assertThat(customerMember.getLast_name()).isEqualTo("shin");
        assertThat(customerMember.getFirst_name()).isEqualTo("chulho");
        assertThat(customerMember.getBirthday()).isEqualTo("19930115");
        assertThat(customerMember.getGender()).isEqualTo("남");
        assertThat(customerMember.getCountry_code()).isEqualTo("410");
        assertThat(customerMember.getCountry_number()).isEqualTo("82");
        assertThat(customerMember.getCountry_type()).isEqualTo(KR);
        assertThat(customerMember.getStatus()).isEqualTo(MemberStatus.CREATED);
        assertThat(customerMember.getCreatedAt()).isNotNull();
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 소비자_생성_실패__user_id가_null이거나_빈값(String user_id) {
        assertThatIllegalArgumentException().isThrownBy(() -> new Customer(user_id, "123", "shin", "chulho", "19930115", "남"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 소비자_생성_실패__password가_null이거나_빈값(String password) {
        assertThatIllegalArgumentException().isThrownBy(() -> new Customer("user_id", password, "shin", "chulho", "19930115", "남"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 소비자_생성_실패__last_name이_null이거나_빈값(String lastname) {
        assertThatIllegalArgumentException().isThrownBy(() -> new Customer("user_id", "1234", lastname, "chulho", "19930115", "남"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 소비자_생성_실패__first_name이_null이거나_빈값(String firstname) {
        assertThatIllegalArgumentException().isThrownBy(() -> new Customer("user_id", "1234", "lastname", firstname, "19930115", "남"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 소비자_생성_실패__birthday가_null이거나_빈값(String birthday) {
        assertThatIllegalArgumentException().isThrownBy(() -> new Customer("user_id", "1234", "lastname", "firstname", birthday, "남"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 소비자_생성_실패__gender가_null이거나_빈값(String gender) {
        assertThatIllegalArgumentException().isThrownBy(() -> new Customer("user_id", "1234", "lastname", "firstname", "19930115", gender));
    }

    @Test
    void 소비자_생성_실패__user_id가_공백() {
        val user_id = " ";

        assertThatIllegalArgumentException().isThrownBy(() -> new Customer(user_id, "123", "shin", "chulho", "19930115", "남"));
    }

    @Test
    void 소비자_생성_실패__password가_공백() {
        val password = " ";

        assertThatIllegalArgumentException().isThrownBy(() -> new Customer("user_id", password, "shin", "chulho", "19930115", "남"));
    }

    @Test
    void 소비자_생성_실패__last_name이_공백() {
        val last_name = " ";

        assertThatIllegalArgumentException().isThrownBy(() -> new Customer("user_id", "password", last_name, "chulho", "19930115", "남"));
    }

    @Test
    void 소비자_생성_실패__first_name이_공백() {
        val first_name = " ";

        assertThatIllegalArgumentException().isThrownBy(() -> new Customer("user_id", "password", "last_name", first_name, "19930115", "남"));
    }

    @Test
    void 소비자_생성_실패__birthday가_공백() {
        val birthday = " ";

        assertThatIllegalArgumentException().isThrownBy(() -> new Customer("user_id", "password", "last_name", "first_name", birthday, "남"));
    }

    @Test
    void 소비자_생성_실패__gender가_공백() {
        val gender = " ";

        assertThatIllegalArgumentException().isThrownBy(() -> new Customer("user_id", "password", "last_name", "first_name", "birthday", gender));
    }
}