import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITemplate } from '../template.model';
import { TemplateService } from '../service/template.service';

const templateResolve = (route: ActivatedRouteSnapshot): Observable<null | ITemplate> => {
  const id = route.params['id'];
  if (id) {
    return inject(TemplateService)
      .find(id)
      .pipe(
        mergeMap((template: HttpResponse<ITemplate>) => {
          if (template.body) {
            return of(template.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default templateResolve;
