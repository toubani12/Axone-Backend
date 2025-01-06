import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TechCVAchievementComponent } from './list/tech-cv-achievement.component';
import { TechCVAchievementDetailComponent } from './detail/tech-cv-achievement-detail.component';
import { TechCVAchievementUpdateComponent } from './update/tech-cv-achievement-update.component';
import TechCVAchievementResolve from './route/tech-cv-achievement-routing-resolve.service';

const techCVAchievementRoute: Routes = [
  {
    path: '',
    component: TechCVAchievementComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TechCVAchievementDetailComponent,
    resolve: {
      techCVAchievement: TechCVAchievementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TechCVAchievementUpdateComponent,
    resolve: {
      techCVAchievement: TechCVAchievementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TechCVAchievementUpdateComponent,
    resolve: {
      techCVAchievement: TechCVAchievementResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default techCVAchievementRoute;
