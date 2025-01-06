import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAppAccount } from '../app-account.model';
import { AppAccountService } from '../service/app-account.service';

const appAccountResolve = (route: ActivatedRouteSnapshot): Observable<null | IAppAccount> => {
  const id = route.params['id'];
  if (id) {
    return inject(AppAccountService)
      .find(id)
      .pipe(
        mergeMap((appAccount: HttpResponse<IAppAccount>) => {
          if (appAccount.body) {
            return of(appAccount.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default appAccountResolve;
