import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ContractComponent } from './list/contract.component';
import { ContractDetailComponent } from './detail/contract-detail.component';
import { ContractUpdateComponent } from './update/contract-update.component';
import ContractResolve from './route/contract-routing-resolve.service';

const contractRoute: Routes = [
  {
    path: '',
    component: ContractComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContractDetailComponent,
    resolve: {
      contract: ContractResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContractUpdateComponent,
    resolve: {
      contract: ContractResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContractUpdateComponent,
    resolve: {
      contract: ContractResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default contractRoute;
