import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TechCVSoftSkillsComponent } from './list/tech-cv-soft-skills.component';
import { TechCVSoftSkillsDetailComponent } from './detail/tech-cv-soft-skills-detail.component';
import { TechCVSoftSkillsUpdateComponent } from './update/tech-cv-soft-skills-update.component';
import TechCVSoftSkillsResolve from './route/tech-cv-soft-skills-routing-resolve.service';

const techCVSoftSkillsRoute: Routes = [
  {
    path: '',
    component: TechCVSoftSkillsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TechCVSoftSkillsDetailComponent,
    resolve: {
      techCVSoftSkills: TechCVSoftSkillsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TechCVSoftSkillsUpdateComponent,
    resolve: {
      techCVSoftSkills: TechCVSoftSkillsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TechCVSoftSkillsUpdateComponent,
    resolve: {
      techCVSoftSkills: TechCVSoftSkillsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default techCVSoftSkillsRoute;
