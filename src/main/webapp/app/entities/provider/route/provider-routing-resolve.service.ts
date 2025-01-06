import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProvider } from '../provider.model';
import { ProviderService } from '../service/provider.service';

const providerResolve = (route: ActivatedRouteSnapshot): Observable<null | IProvider> => {
  const id = route.params['id'];
  if (id) {
    return inject(ProviderService)
      .find(id)
      .pipe(
        mergeMap((provider: HttpResponse<IProvider>) => {
          if (provider.body) {
            return of(provider.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default providerResolve;
