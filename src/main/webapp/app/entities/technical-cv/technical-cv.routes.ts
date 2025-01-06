import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TechnicalCVComponent } from './list/technical-cv.component';
import { TechnicalCVDetailComponent } from './detail/technical-cv-detail.component';
import { TechnicalCVUpdateComponent } from './update/technical-cv-update.component';
import TechnicalCVResolve from './route/technical-cv-routing-resolve.service';

const technicalCVRoute: Routes = [
  {
    path: '',
    component: TechnicalCVComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TechnicalCVDetailComponent,
    resolve: {
      technicalCV: TechnicalCVResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TechnicalCVUpdateComponent,
    resolve: {
      technicalCV: TechnicalCVResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TechnicalCVUpdateComponent,
    resolve: {
      technicalCV: TechnicalCVResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default technicalCVRoute;
