import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ResumeComponent } from './list/resume.component';
import { ResumeDetailComponent } from './detail/resume-detail.component';
import { ResumeUpdateComponent } from './update/resume-update.component';
import ResumeResolve from './route/resume-routing-resolve.service';

const resumeRoute: Routes = [
  {
    path: '',
    component: ResumeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ResumeDetailComponent,
    resolve: {
      resume: ResumeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ResumeUpdateComponent,
    resolve: {
      resume: ResumeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ResumeUpdateComponent,
    resolve: {
      resume: ResumeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default resumeRoute;
