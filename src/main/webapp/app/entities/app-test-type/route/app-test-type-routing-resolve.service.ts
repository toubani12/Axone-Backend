import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAppTestType } from '../app-test-type.model';
import { AppTestTypeService } from '../service/app-test-type.service';

const appTestTypeResolve = (route: ActivatedRouteSnapshot): Observable<null | IAppTestType> => {
  const id = route.params['id'];
  if (id) {
    return inject(AppTestTypeService)
      .find(id)
      .pipe(
        mergeMap((appTestType: HttpResponse<IAppTestType>) => {
          if (appTestType.body) {
            return of(appTestType.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default appTestTypeResolve;
