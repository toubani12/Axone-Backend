import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmployer } from '../employer.model';
import { EmployerService } from '../service/employer.service';

const employerResolve = (route: ActivatedRouteSnapshot): Observable<null | IEmployer> => {
  const id = route.params['id'];
  if (id) {
    return inject(EmployerService)
      .find(id)
      .pipe(
        mergeMap((employer: HttpResponse<IEmployer>) => {
          if (employer.body) {
            return of(employer.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default employerResolve;
