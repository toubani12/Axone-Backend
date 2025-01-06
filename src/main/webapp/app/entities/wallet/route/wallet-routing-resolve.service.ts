import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IWallet } from '../wallet.model';
import { WalletService } from '../service/wallet.service';

const walletResolve = (route: ActivatedRouteSnapshot): Observable<null | IWallet> => {
  const id = route.params['id'];
  if (id) {
    return inject(WalletService)
      .find(id)
      .pipe(
        mergeMap((wallet: HttpResponse<IWallet>) => {
          if (wallet.body) {
            return of(wallet.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default walletResolve;
