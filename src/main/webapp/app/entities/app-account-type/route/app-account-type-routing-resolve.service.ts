import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAppAccountType } from '../app-account-type.model';
import { AppAccountTypeService } from '../service/app-account-type.service';

const appAccountTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IAppAccountType> => {
  const id = route.params['id'];
  if (id) {
    return inject(AppAccountTypeService)
      .find(id)
      .pipe(
        mergeMap((appAccountType: HttpResponse<IAppAccountType>) => {
          if (appAccountType.body) {
            return of(appAccountType.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default appAccountTypeResolve;
