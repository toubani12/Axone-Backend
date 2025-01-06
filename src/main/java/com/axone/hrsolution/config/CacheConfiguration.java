package com.axone.hrsolution.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build()
        );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.axone.hrsolution.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.axone.hrsolution.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.axone.hrsolution.domain.User.class.getName());
            createCache(cm, com.axone.hrsolution.domain.Authority.class.getName());
            createCache(cm, com.axone.hrsolution.domain.User.class.getName() + ".authorities");
            createCache(cm, com.axone.hrsolution.domain.Recruiter.class.getName());
            createCache(cm, com.axone.hrsolution.domain.Recruiter.class.getName() + ".requests");
            createCache(cm, com.axone.hrsolution.domain.Recruiter.class.getName() + ".applications");
            createCache(cm, com.axone.hrsolution.domain.Recruiter.class.getName() + ".operationalDomains");
            createCache(cm, com.axone.hrsolution.domain.Recruiter.class.getName() + ".ndaStatuses");
            createCache(cm, com.axone.hrsolution.domain.Recruiter.class.getName() + ".contracts");
            createCache(cm, com.axone.hrsolution.domain.Employer.class.getName());
            createCache(cm, com.axone.hrsolution.domain.Employer.class.getName() + ".operationalDomains");
            createCache(cm, com.axone.hrsolution.domain.Employer.class.getName() + ".paymentAccounts");
            createCache(cm, com.axone.hrsolution.domain.Employer.class.getName() + ".applications");
            createCache(cm, com.axone.hrsolution.domain.Employer.class.getName() + ".contracts");
            createCache(cm, com.axone.hrsolution.domain.Employer.class.getName() + ".templates");
            createCache(cm, com.axone.hrsolution.domain.Employer.class.getName() + ".ndaStatuses");
            createCache(cm, com.axone.hrsolution.domain.Candidate.class.getName());
            createCache(cm, com.axone.hrsolution.domain.Candidate.class.getName() + ".interviewResults");
            createCache(cm, com.axone.hrsolution.domain.Candidate.class.getName() + ".candidateResumes");
            createCache(cm, com.axone.hrsolution.domain.Candidate.class.getName() + ".domains");
            createCache(cm, com.axone.hrsolution.domain.Candidate.class.getName() + ".applications");
            createCache(cm, com.axone.hrsolution.domain.Candidate.class.getName() + ".ndaStatuses");
            createCache(cm, com.axone.hrsolution.domain.Resume.class.getName());
            createCache(cm, com.axone.hrsolution.domain.NDA.class.getName());
            createCache(cm, com.axone.hrsolution.domain.TechnicalCV.class.getName());
            createCache(cm, com.axone.hrsolution.domain.TechnicalCV.class.getName() + ".hardSkills");
            createCache(cm, com.axone.hrsolution.domain.TechnicalCV.class.getName() + ".softSkills");
            createCache(cm, com.axone.hrsolution.domain.TechnicalCV.class.getName() + ".educations");
            createCache(cm, com.axone.hrsolution.domain.TechnicalCV.class.getName() + ".employments");
            createCache(cm, com.axone.hrsolution.domain.TechnicalCV.class.getName() + ".projects");
            createCache(cm, com.axone.hrsolution.domain.TechnicalCV.class.getName() + ".achievements");
            createCache(cm, com.axone.hrsolution.domain.TechnicalCV.class.getName() + ".attachedDocs");
            createCache(cm, com.axone.hrsolution.domain.TechnicalCV.class.getName() + ".altActivities");
            createCache(cm, com.axone.hrsolution.domain.TechCVEducation.class.getName());
            createCache(cm, com.axone.hrsolution.domain.TechCVEmployment.class.getName());
            createCache(cm, com.axone.hrsolution.domain.TechCVProject.class.getName());
            createCache(cm, com.axone.hrsolution.domain.TechCVAchievement.class.getName());
            createCache(cm, com.axone.hrsolution.domain.TechCVDocs.class.getName());
            createCache(cm, com.axone.hrsolution.domain.TechCVHardSkills.class.getName());
            createCache(cm, com.axone.hrsolution.domain.TechCVSoftSkills.class.getName());
            createCache(cm, com.axone.hrsolution.domain.TechCVAltActivities.class.getName());
            createCache(cm, com.axone.hrsolution.domain.Request.class.getName());
            createCache(cm, com.axone.hrsolution.domain.Application.class.getName());
            createCache(cm, com.axone.hrsolution.domain.Application.class.getName() + ".contracts");
            createCache(cm, com.axone.hrsolution.domain.Application.class.getName() + ".interviews");
            createCache(cm, com.axone.hrsolution.domain.Application.class.getName() + ".contractTypes");
            createCache(cm, com.axone.hrsolution.domain.Application.class.getName() + ".contractTemplates");
            createCache(cm, com.axone.hrsolution.domain.Application.class.getName() + ".criteria");
            createCache(cm, com.axone.hrsolution.domain.Application.class.getName() + ".domains");
            createCache(cm, com.axone.hrsolution.domain.Application.class.getName() + ".recruiters");
            createCache(cm, com.axone.hrsolution.domain.Application.class.getName() + ".candidates");
            createCache(cm, com.axone.hrsolution.domain.ContractType.class.getName());
            createCache(cm, com.axone.hrsolution.domain.ContractType.class.getName() + ".applications");
            createCache(cm, com.axone.hrsolution.domain.Domain.class.getName());
            createCache(cm, com.axone.hrsolution.domain.Domain.class.getName() + ".recruiters");
            createCache(cm, com.axone.hrsolution.domain.Domain.class.getName() + ".candidates");
            createCache(cm, com.axone.hrsolution.domain.Domain.class.getName() + ".applications");
            createCache(cm, com.axone.hrsolution.domain.Criteria.class.getName());
            createCache(cm, com.axone.hrsolution.domain.Criteria.class.getName() + ".applications");
            createCache(cm, com.axone.hrsolution.domain.Template.class.getName());
            createCache(cm, com.axone.hrsolution.domain.Template.class.getName() + ".applications");
            createCache(cm, com.axone.hrsolution.domain.Contract.class.getName());
            createCache(cm, com.axone.hrsolution.domain.Wallet.class.getName());
            createCache(cm, com.axone.hrsolution.domain.AppAccount.class.getName());
            createCache(cm, com.axone.hrsolution.domain.AppAccount.class.getName() + ".types");
            createCache(cm, com.axone.hrsolution.domain.AppAccount.class.getName() + ".providers");
            createCache(cm, com.axone.hrsolution.domain.Provider.class.getName());
            createCache(cm, com.axone.hrsolution.domain.Provider.class.getName() + ".appAccounts");
            createCache(cm, com.axone.hrsolution.domain.AppAccountType.class.getName());
            createCache(cm, com.axone.hrsolution.domain.AppAccountType.class.getName() + ".appAccounts");
            createCache(cm, com.axone.hrsolution.domain.AppTest.class.getName());
            createCache(cm, com.axone.hrsolution.domain.AppTest.class.getName() + ".customQuestions");
            createCache(cm, com.axone.hrsolution.domain.AppTest.class.getName() + ".types");
            createCache(cm, com.axone.hrsolution.domain.AppTestType.class.getName());
            createCache(cm, com.axone.hrsolution.domain.AppTestType.class.getName() + ".appTests");
            createCache(cm, com.axone.hrsolution.domain.CustomQuestion.class.getName());
            createCache(cm, com.axone.hrsolution.domain.Interview.class.getName());
            createCache(cm, com.axone.hrsolution.domain.Admin.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
