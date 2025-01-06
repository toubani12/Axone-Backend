import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TechCVHardSkillsComponent } from './list/tech-cv-hard-skills.component';
import { TechCVHardSkillsDetailComponent } from './detail/tech-cv-hard-skills-detail.component';
import { TechCVHardSkillsUpdateComponent } from './update/tech-cv-hard-skills-update.component';
import TechCVHardSkillsResolve from './route/tech-cv-hard-skills-routing-resolve.service';

const techCVHardSkillsRoute: Routes = [
  {
    path: '',
    component: TechCVHardSkillsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TechCVHardSkillsDetailComponent,
    resolve: {
      techCVHardSkills: TechCVHardSkillsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TechCVHardSkillsUpdateComponent,
    resolve: {
      techCVHardSkills: TechCVHardSkillsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TechCVHardSkillsUpdateComponent,
    resolve: {
      techCVHardSkills: TechCVHardSkillsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default techCVHardSkillsRoute;
