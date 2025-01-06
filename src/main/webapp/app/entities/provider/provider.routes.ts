import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ProviderComponent } from './list/provider.component';
import { ProviderDetailComponent } from './detail/provider-detail.component';
import { ProviderUpdateComponent } from './update/provider-update.component';
import ProviderResolve from './route/provider-routing-resolve.service';

const providerRoute: Routes = [
  {
    path: '',
    component: ProviderComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProviderDetailComponent,
    resolve: {
      provider: ProviderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProviderUpdateComponent,
    resolve: {
      provider: ProviderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProviderUpdateComponent,
    resolve: {
      provider: ProviderResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default providerRoute;
