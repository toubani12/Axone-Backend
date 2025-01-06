import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TechCVEmploymentComponent } from './list/tech-cv-employment.component';
import { TechCVEmploymentDetailComponent } from './detail/tech-cv-employment-detail.component';
import { TechCVEmploymentUpdateComponent } from './update/tech-cv-employment-update.component';
import TechCVEmploymentResolve from './route/tech-cv-employment-routing-resolve.service';

const techCVEmploymentRoute: Routes = [
  {
    path: '',
    component: TechCVEmploymentComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TechCVEmploymentDetailComponent,
    resolve: {
      techCVEmployment: TechCVEmploymentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TechCVEmploymentUpdateComponent,
    resolve: {
      techCVEmployment: TechCVEmploymentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TechCVEmploymentUpdateComponent,
    resolve: {
      techCVEmployment: TechCVEmploymentResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default techCVEmploymentRoute;
