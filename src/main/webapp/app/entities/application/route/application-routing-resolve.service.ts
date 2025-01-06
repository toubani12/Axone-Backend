import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IApplication } from '../application.model';
import { ApplicationService } from '../service/application.service';

const applicationResolve = (route: ActivatedRouteSnapshot): Observable<null | IApplication> => {
  const id = route.params['id'];
  if (id) {
    return inject(ApplicationService)
      .find(id)
      .pipe(
        mergeMap((application: HttpResponse<IApplication>) => {
          if (application.body) {
            return of(application.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default applicationResolve;
