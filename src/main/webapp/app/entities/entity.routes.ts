import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'hrSolutionApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'recruiter',
    data: { pageTitle: 'hrSolutionApp.recruiter.home.title' },
    loadChildren: () => import('./recruiter/recruiter.routes'),
  },
  {
    path: 'employer',
    data: { pageTitle: 'hrSolutionApp.employer.home.title' },
    loadChildren: () => import('./employer/employer.routes'),
  },
  {
    path: 'candidate',
    data: { pageTitle: 'hrSolutionApp.candidate.home.title' },
    loadChildren: () => import('./candidate/candidate.routes'),
  },
  {
    path: 'resume',
    data: { pageTitle: 'hrSolutionApp.resume.home.title' },
    loadChildren: () => import('./resume/resume.routes'),
  },
  {
    path: 'nda',
    data: { pageTitle: 'hrSolutionApp.nDA.home.title' },
    loadChildren: () => import('./nda/nda.routes'),
  },
  {
    path: 'technical-cv',
    data: { pageTitle: 'hrSolutionApp.technicalCV.home.title' },
    loadChildren: () => import('./technical-cv/technical-cv.routes'),
  },
  {
    path: 'tech-cv-education',
    data: { pageTitle: 'hrSolutionApp.techCVEducation.home.title' },
    loadChildren: () => import('./tech-cv-education/tech-cv-education.routes'),
  },
  {
    path: 'tech-cv-employment',
    data: { pageTitle: 'hrSolutionApp.techCVEmployment.home.title' },
    loadChildren: () => import('./tech-cv-employment/tech-cv-employment.routes'),
  },
  {
    path: 'tech-cv-project',
    data: { pageTitle: 'hrSolutionApp.techCVProject.home.title' },
    loadChildren: () => import('./tech-cv-project/tech-cv-project.routes'),
  },
  {
    path: 'tech-cv-achievement',
    data: { pageTitle: 'hrSolutionApp.techCVAchievement.home.title' },
    loadChildren: () => import('./tech-cv-achievement/tech-cv-achievement.routes'),
  },
  {
    path: 'tech-cv-docs',
    data: { pageTitle: 'hrSolutionApp.techCVDocs.home.title' },
    loadChildren: () => import('./tech-cv-docs/tech-cv-docs.routes'),
  },
  {
    path: 'tech-cv-hard-skills',
    data: { pageTitle: 'hrSolutionApp.techCVHardSkills.home.title' },
    loadChildren: () => import('./tech-cv-hard-skills/tech-cv-hard-skills.routes'),
  },
  {
    path: 'tech-cv-soft-skills',
    data: { pageTitle: 'hrSolutionApp.techCVSoftSkills.home.title' },
    loadChildren: () => import('./tech-cv-soft-skills/tech-cv-soft-skills.routes'),
  },
  {
    path: 'tech-cv-alt-activities',
    data: { pageTitle: 'hrSolutionApp.techCVAltActivities.home.title' },
    loadChildren: () => import('./tech-cv-alt-activities/tech-cv-alt-activities.routes'),
  },
  {
    path: 'request',
    data: { pageTitle: 'hrSolutionApp.request.home.title' },
    loadChildren: () => import('./request/request.routes'),
  },
  {
    path: 'application',
    data: { pageTitle: 'hrSolutionApp.application.home.title' },
    loadChildren: () => import('./application/application.routes'),
  },
  {
    path: 'contract-type',
    data: { pageTitle: 'hrSolutionApp.contractType.home.title' },
    loadChildren: () => import('./contract-type/contract-type.routes'),
  },
  {
    path: 'domain',
    data: { pageTitle: 'hrSolutionApp.domain.home.title' },
    loadChildren: () => import('./domain/domain.routes'),
  },
  {
    path: 'criteria',
    data: { pageTitle: 'hrSolutionApp.criteria.home.title' },
    loadChildren: () => import('./criteria/criteria.routes'),
  },
  {
    path: 'template',
    data: { pageTitle: 'hrSolutionApp.template.home.title' },
    loadChildren: () => import('./template/template.routes'),
  },
  {
    path: 'contract',
    data: { pageTitle: 'hrSolutionApp.contract.home.title' },
    loadChildren: () => import('./contract/contract.routes'),
  },
  {
    path: 'wallet',
    data: { pageTitle: 'hrSolutionApp.wallet.home.title' },
    loadChildren: () => import('./wallet/wallet.routes'),
  },
  {
    path: 'app-account',
    data: { pageTitle: 'hrSolutionApp.appAccount.home.title' },
    loadChildren: () => import('./app-account/app-account.routes'),
  },
  {
    path: 'provider',
    data: { pageTitle: 'hrSolutionApp.provider.home.title' },
    loadChildren: () => import('./provider/provider.routes'),
  },
  {
    path: 'app-account-type',
    data: { pageTitle: 'hrSolutionApp.appAccountType.home.title' },
    loadChildren: () => import('./app-account-type/app-account-type.routes'),
  },
  {
    path: 'app-test',
    data: { pageTitle: 'hrSolutionApp.appTest.home.title' },
    loadChildren: () => import('./app-test/app-test.routes'),
  },
  {
    path: 'app-test-type',
    data: { pageTitle: 'hrSolutionApp.appTestType.home.title' },
    loadChildren: () => import('./app-test-type/app-test-type.routes'),
  },
  {
    path: 'custom-question',
    data: { pageTitle: 'hrSolutionApp.customQuestion.home.title' },
    loadChildren: () => import('./custom-question/custom-question.routes'),
  },
  {
    path: 'interview',
    data: { pageTitle: 'hrSolutionApp.interview.home.title' },
    loadChildren: () => import('./interview/interview.routes'),
  },
  {
    path: 'admin',
    data: { pageTitle: 'hrSolutionApp.admin.home.title' },
    loadChildren: () => import('./admin/admin.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
