import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TechCVAltActivitiesComponent } from './list/tech-cv-alt-activities.component';
import { TechCVAltActivitiesDetailComponent } from './detail/tech-cv-alt-activities-detail.component';
import { TechCVAltActivitiesUpdateComponent } from './update/tech-cv-alt-activities-update.component';
import TechCVAltActivitiesResolve from './route/tech-cv-alt-activities-routing-resolve.service';

const techCVAltActivitiesRoute: Routes = [
  {
    path: '',
    component: TechCVAltActivitiesComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TechCVAltActivitiesDetailComponent,
    resolve: {
      techCVAltActivities: TechCVAltActivitiesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TechCVAltActivitiesUpdateComponent,
    resolve: {
      techCVAltActivities: TechCVAltActivitiesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TechCVAltActivitiesUpdateComponent,
    resolve: {
      techCVAltActivities: TechCVAltActivitiesResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default techCVAltActivitiesRoute;
