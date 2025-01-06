import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { WalletComponent } from './list/wallet.component';
import { WalletDetailComponent } from './detail/wallet-detail.component';
import { WalletUpdateComponent } from './update/wallet-update.component';
import WalletResolve from './route/wallet-routing-resolve.service';

const walletRoute: Routes = [
  {
    path: '',
    component: WalletComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: WalletDetailComponent,
    resolve: {
      wallet: WalletResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: WalletUpdateComponent,
    resolve: {
      wallet: WalletResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: WalletUpdateComponent,
    resolve: {
      wallet: WalletResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default walletRoute;
