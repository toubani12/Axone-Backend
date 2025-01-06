import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { RecruiterComponent } from './list/recruiter.component';
import { RecruiterDetailComponent } from './detail/recruiter-detail.component';
import { RecruiterUpdateComponent } from './update/recruiter-update.component';
import RecruiterResolve from './route/recruiter-routing-resolve.service';

const recruiterRoute: Routes = [
  {
    path: '',
    component: RecruiterComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RecruiterDetailComponent,
    resolve: {
      recruiter: RecruiterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RecruiterUpdateComponent,
    resolve: {
      recruiter: RecruiterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RecruiterUpdateComponent,
    resolve: {
      recruiter: RecruiterResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default recruiterRoute;
