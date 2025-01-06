import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAppTest } from '../app-test.model';
import { AppTestService } from '../service/app-test.service';

const appTestResolve = (route: ActivatedRouteSnapshot): Observable<null | IAppTest> => {
  const id = route.params['id'];
  if (id) {
    return inject(AppTestService)
      .find(id)
      .pipe(
        mergeMap((appTest: HttpResponse<IAppTest>) => {
          if (appTest.body) {
            return of(appTest.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default appTestResolve;
