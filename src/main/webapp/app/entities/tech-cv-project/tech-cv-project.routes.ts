import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TechCVProjectComponent } from './list/tech-cv-project.component';
import { TechCVProjectDetailComponent } from './detail/tech-cv-project-detail.component';
import { TechCVProjectUpdateComponent } from './update/tech-cv-project-update.component';
import TechCVProjectResolve from './route/tech-cv-project-routing-resolve.service';

const techCVProjectRoute: Routes = [
  {
    path: '',
    component: TechCVProjectComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TechCVProjectDetailComponent,
    resolve: {
      techCVProject: TechCVProjectResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TechCVProjectUpdateComponent,
    resolve: {
      techCVProject: TechCVProjectResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TechCVProjectUpdateComponent,
    resolve: {
      techCVProject: TechCVProjectResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default techCVProjectRoute;
