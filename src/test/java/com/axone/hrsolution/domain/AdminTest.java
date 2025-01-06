package com.axone.hrsolution.domain;

import static com.axone.hrsolution.domain.AdminTestSamples.*;
import static com.axone.hrsolution.domain.WalletTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.axone.hrsolution.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdminTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Admin.class);
        Admin admin1 = getAdminSample1();
        Admin admin2 = new Admin();
        assertThat(admin1).isNotEqualTo(admin2);

        admin2.setId(admin1.getId());
        assertThat(admin1).isEqualTo(admin2);

        admin2 = getAdminSample2();
        assertThat(admin1).isNotEqualTo(admin2);
    }

    @Test
    void systemWalletTest() throws Exception {
        Admin admin = getAdminRandomSampleGenerator();
        Wallet walletBack = getWalletRandomSampleGenerator();

        admin.setSystemWallet(walletBack);
        assertThat(admin.getSystemWallet()).isEqualTo(walletBack);

        admin.systemWallet(null);
        assertThat(admin.getSystemWallet()).isNull();
    }
}
