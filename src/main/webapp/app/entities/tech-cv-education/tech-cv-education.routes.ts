import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TechCVEducationComponent } from './list/tech-cv-education.component';
import { TechCVEducationDetailComponent } from './detail/tech-cv-education-detail.component';
import { TechCVEducationUpdateComponent } from './update/tech-cv-education-update.component';
import TechCVEducationResolve from './route/tech-cv-education-routing-resolve.service';

const techCVEducationRoute: Routes = [
  {
    path: '',
    component: TechCVEducationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TechCVEducationDetailComponent,
    resolve: {
      techCVEducation: TechCVEducationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TechCVEducationUpdateComponent,
    resolve: {
      techCVEducation: TechCVEducationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TechCVEducationUpdateComponent,
    resolve: {
      techCVEducation: TechCVEducationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default techCVEducationRoute;
