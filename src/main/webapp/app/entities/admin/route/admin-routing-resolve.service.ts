import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAdmin } from '../admin.model';
import { AdminService } from '../service/admin.service';

const adminResolve = (route: ActivatedRouteSnapshot): Observable<null | IAdmin> => {
  const id = route.params['id'];
  if (id) {
    return inject(AdminService)
      .find(id)
      .pipe(
        mergeMap((admin: HttpResponse<IAdmin>) => {
          if (admin.body) {
            return of(admin.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default adminResolve;
